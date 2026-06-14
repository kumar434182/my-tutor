package com.enterprise.learning.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "roles", indexes = {
    @Index(name = "idx_role_name", columnList = "name", unique = true)
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false, length = 50)
    private String name;

    @Column(length = 255)
    private String description;

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum RoleType {
        SUPER_ADMIN("SUPER_ADMIN"),
        ADMIN("ADMIN"),
        TRAINER("TRAINER"),
        LEARNER("LEARNER"),
        REVIEWER("REVIEWER"),
        CONTENT_AUTHOR("CONTENT_AUTHOR");

        private final String value;

        RoleType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
