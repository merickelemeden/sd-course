package com.sd_project.sd_course.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @NotBlank
    @Size(min = 3, max = 50)
    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @NotBlank
    @Size(max = 100)
    @Email
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @NotBlank
    @Size(min = 6, max = 255)
    @Column(name = "password", nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @Builder.Default
    private Set<Role> roles = new HashSet<>();

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public boolean hasRole(Role.RoleName roleName) {
        return roles.stream()
                .anyMatch(role -> role.getName() == roleName);
    }

    public boolean isAdmin() {
        return hasRole(Role.RoleName.ADMIN);
    }
} 