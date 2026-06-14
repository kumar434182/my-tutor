package com.enterprise.learning.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "analytics")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Analytics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String entityType;

    @Column
    private Long entityId;

    @Column(length = 100)
    private String eventType;

    @Column(columnDefinition = "INTEGER", nullable = false)
    private Integer eventCount;

    @Column(columnDefinition = "DECIMAL(10,2)", nullable = false)
    private Double eventValue;

    @Column(updatable = false)
    private LocalDateTime recordedAt = LocalDateTime.now();

    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
