package com.sd_project.sd_course.service;

import com.sd_project.sd_course.entity.Role;
import com.sd_project.sd_course.entity.Category;
import com.sd_project.sd_course.repository.RoleRepository;
import com.sd_project.sd_course.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataInitializationService implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public void run(String... args) throws Exception {
        initializeRoles();
        initializeCategories();
    }

    private void initializeRoles() {
        log.info("Initializing roles...");
        
        for (Role.RoleName roleName : Role.RoleName.values()) {
            if (!roleRepository.existsByName(roleName)) {
                Role role = Role.builder()
                        .name(roleName)
                        .build();
                roleRepository.save(role);
                log.info("Created role: {}", roleName);
            }
        }
        
        log.info("Roles initialization completed");
    }

    private void initializeCategories() {
        log.info("Initializing sample categories...");
        
        if (categoryRepository.count() == 0) {
            String[][] categoryData = {
                    {"Electronics", "Electronic devices and gadgets"},
                    {"Clothing", "Fashion and apparel"},
                    {"Books", "Books and literature"},
                    {"Home & Garden", "Home improvement and gardening supplies"},
                    {"Sports", "Sports equipment and accessories"},
                    {"Beauty & Health", "Beauty products and health supplements"},
                    {"Toys & Games", "Toys, games, and entertainment"},
                    {"Automotive", "Car parts and automotive accessories"}
            };

            for (String[] data : categoryData) {
                Category category = Category.builder()
                        .name(data[0])
                        .description(data[1])
                        .build();
                categoryRepository.save(category);
                log.info("Created category: {}", data[0]);
            }
        }
        
        log.info("Categories initialization completed");
    }
} 