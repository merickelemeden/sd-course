package com.sd_project.sd_course.repository;

import com.sd_project.sd_course.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Find role by name
     */
    Optional<Role> findByName(Role.RoleName name);

    /**
     * Check if role exists by name
     */
    Boolean existsByName(Role.RoleName name);
} 