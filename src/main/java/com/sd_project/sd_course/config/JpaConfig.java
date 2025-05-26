package com.sd_project.sd_course.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class JpaConfig {
    // JPA Auditing configuration for automatic timestamp management
} 