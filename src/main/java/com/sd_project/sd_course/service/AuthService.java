package com.sd_project.sd_course.service;

import com.sd_project.sd_course.dto.request.LoginRequest;
import com.sd_project.sd_course.dto.request.RegisterRequest;
import com.sd_project.sd_course.dto.response.JwtResponse;
import com.sd_project.sd_course.dto.response.MessageResponse;
import com.sd_project.sd_course.entity.Role;
import com.sd_project.sd_course.entity.User;
import com.sd_project.sd_course.repository.RoleRepository;
import com.sd_project.sd_course.repository.UserRepository;
import com.sd_project.sd_course.security.JwtTokenProvider;
import com.sd_project.sd_course.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    @Transactional
    public MessageResponse registerUser(RegisterRequest registerRequest) {
        // Check if username exists
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            return new MessageResponse("Error: Username is already taken!");
        }

        // Check if email exists
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return new MessageResponse("Error: Email is already in use!");
        }

        // Create new user
        User user = User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .build();

        // Assign default USER role
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(Role.RoleName.USER)
                .orElseThrow(() -> new RuntimeException("Error: User Role is not found."));
        roles.add(userRole);
        user.setRoles(roles);

        userRepository.save(user);
        log.info("User registered successfully: {}", user.getUsername());

        return new MessageResponse("User registered successfully!");
    }

    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        String accessToken = tokenProvider.generateToken(authentication);
        String refreshToken = tokenProvider.generateRefreshToken(userPrincipal.getUsername());

        List<String> roles = userPrincipal.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        log.info("User authenticated successfully: {}", userPrincipal.getUsername());

        return new JwtResponse(
                accessToken,
                refreshToken,
                userPrincipal.getId(),
                userPrincipal.getUsername(),
                userPrincipal.getEmail(),
                roles
        );
    }

    public JwtResponse refreshToken(String refreshToken) {
        if (tokenProvider.validateToken(refreshToken)) {
            String username = tokenProvider.getUsernameFromToken(refreshToken);
            String newAccessToken = tokenProvider.generateTokenFromUsername(username);
            String newRefreshToken = tokenProvider.generateRefreshToken(username);

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            List<String> roles = user.getRoles().stream()
                    .map(role -> "ROLE_" + role.getName().name())
                    .collect(Collectors.toList());

            return new JwtResponse(
                    newAccessToken,
                    newRefreshToken,
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    roles
            );
        } else {
            throw new RuntimeException("Invalid refresh token");
        }
    }
} 