package com.enterprise.learning.config;

import com.enterprise.learning.entity.Role;
import com.enterprise.learning.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    
    private final RoleRepository roleRepository;
    
    @Override
    public void run(String... args) throws Exception {
        log.info("Starting database initialization...");
        
        // Define all required roles
        List<Role> requiredRoles = Arrays.asList(
            Role.builder()
                .name("SUPER_ADMIN")
                .description("Super Administrator with full system access")
                .build(),
            Role.builder()
                .name("ADMIN")
                .description("Administrator with platform management access")
                .build(),
            Role.builder()
                .name("TRAINER")
                .description("Trainer who can create and manage courses")
                .build(),
            Role.builder()
                .name("LEARNER")
                .description("Learner who can enroll in courses")
                .build(),
            Role.builder()
                .name("REVIEWER")
                .description("Reviewer who can evaluate submissions")
                .build(),
            Role.builder()
                .name("CONTENT_AUTHOR")
                .description("Content author who can create course materials")
                .build()
        );
        
        // Seed roles if they don't exist
        for (Role role : requiredRoles) {
            if (!roleRepository.existsByName(role.getName())) {
                roleRepository.save(role);
                log.info("Created role: {}", role.getName());
            } else {
                log.debug("Role already exists: {}", role.getName());
            }
        }
        
        log.info("Database initialization completed successfully");
    }
}
