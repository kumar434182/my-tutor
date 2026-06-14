package com.enterprise.learning.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "learner_progress")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LearnerProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "learner_id", nullable = false)
    private User learner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(columnDefinition = "DECIMAL(5,2)", nullable = false)
    private Double progressPercentage = 0.0;

    @Column(columnDefinition = "INTEGER", nullable = false)
    private Integer lessonsCompleted = 0;

    @Column(columnDefinition = "INTEGER", nullable = false)
    private Integer totalLessons = 0;

    @Column(columnDefinition = "DECIMAL(5,2)", nullable = false)
    private Double averageScore = 0.0;

    @Enumerated(EnumType.STRING)
    private ProgressStatus status;

    @Column(updatable = false)
    private LocalDateTime enrolledAt = LocalDateTime.now();

    private LocalDateTime lastAccessedAt;

    private LocalDateTime completedAt;

    public enum ProgressStatus {
        NOT_STARTED, IN_PROGRESS, COMPLETED, DROPPED
    }
}
