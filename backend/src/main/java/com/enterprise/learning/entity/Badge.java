package com.enterprise.learning.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "badges")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Badge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 500)
    private String iconUrl;

    @Column(columnDefinition = "INT", nullable = false)
    private Integer pointsRequired;

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Relationship managed by UserBadge join table if needed
    // For now, keeping it simple without explicit earners collection
}
