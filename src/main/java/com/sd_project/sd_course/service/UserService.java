package com.sd_project.sd_course.service;

import com.sd_project.sd_course.dto.request.UserUpdateRequest;
import com.sd_project.sd_course.dto.response.MessageResponse;
import com.sd_project.sd_course.dto.response.UserResponse;
import com.sd_project.sd_course.entity.Role;
import com.sd_project.sd_course.entity.User;
import com.sd_project.sd_course.exception.ResourceNotFoundException;
import com.sd_project.sd_course.repository.RoleRepository;
import com.sd_project.sd_course.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public Page<UserResponse> getAllUsers(Pageable pageable) {
        log.info("Fetching all users with pagination: {}", pageable);
        Page<User> users = userRepository.findAll(pageable);
        return users.map(this::convertToUserResponse);
    }

    public List<UserResponse> getAllUsers() {
        log.info("Fetching all users");
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::convertToUserResponse)
                .collect(Collectors.toList());
    }

    public UserResponse getUserById(Long id) {
        log.info("Fetching user by id: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return convertToUserResponse(user);
    }

    public UserResponse getUserByUsername(String username) {
        log.info("Fetching user by username: {}", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        return convertToUserResponse(user);
    }

    @Transactional
    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        log.info("Updating user with id: {}", id);
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        // Update username if provided
        if (StringUtils.hasText(request.getUsername())) {
            if (!user.getUsername().equals(request.getUsername()) && 
                userRepository.existsByUsername(request.getUsername())) {
                throw new RuntimeException("Username is already taken!");
            }
            user.setUsername(request.getUsername());
        }

        // Update email if provided
        if (StringUtils.hasText(request.getEmail())) {
            if (!user.getEmail().equals(request.getEmail()) && 
                userRepository.existsByEmail(request.getEmail())) {
                throw new RuntimeException("Email is already in use!");
            }
            user.setEmail(request.getEmail());
        }

        // Update password if provided
        if (StringUtils.hasText(request.getPassword())) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        // Update roles if provided
        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            Set<Role> roles = new HashSet<>();
            for (String roleName : request.getRoles()) {
                Role.RoleName roleEnum;
                try {
                    roleEnum = Role.RoleName.valueOf(roleName.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("Invalid role: " + roleName);
                }
                
                Role role = roleRepository.findByName(roleEnum)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
                roles.add(role);
            }
            user.setRoles(roles);
        }

        User updatedUser = userRepository.save(user);
        log.info("User updated successfully: {}", updatedUser.getUsername());
        
        return convertToUserResponse(updatedUser);
    }

    @Transactional
    public MessageResponse deleteUser(Long id) {
        log.info("Deleting user with id: {}", id);
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        userRepository.delete(user);
        log.info("User deleted successfully: {}", user.getUsername());
        
        return new MessageResponse("User deleted successfully!");
    }

    @Transactional
    public UserResponse assignRoleToUser(Long userId, String roleName) {
        log.info("Assigning role {} to user {}", roleName, userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Role.RoleName roleEnum;
        try {
            roleEnum = Role.RoleName.valueOf(roleName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role: " + roleName);
        }

        Role role = roleRepository.findByName(roleEnum)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));

        user.getRoles().add(role);
        User updatedUser = userRepository.save(user);
        
        log.info("Role {} assigned to user {} successfully", roleName, user.getUsername());
        return convertToUserResponse(updatedUser);
    }

    @Transactional
    public UserResponse removeRoleFromUser(Long userId, String roleName) {
        log.info("Removing role {} from user {}", roleName, userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Role.RoleName roleEnum;
        try {
            roleEnum = Role.RoleName.valueOf(roleName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role: " + roleName);
        }

        Role role = roleRepository.findByName(roleEnum)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));

        user.getRoles().remove(role);
        User updatedUser = userRepository.save(user);
        
        log.info("Role {} removed from user {} successfully", roleName, user.getUsername());
        return convertToUserResponse(updatedUser);
    }

    private UserResponse convertToUserResponse(User user) {
        List<String> roles = user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toList());

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(roles)
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
} 