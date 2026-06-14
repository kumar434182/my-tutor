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

    @Column(columnDefinition = "DECIMAL(5,2) DEFAULT 0")
    private Double progressPercentage;

    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer lessonsCompleted;

    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer totalLessons;

    @Column(columnDefinition = "DECIMAL(5,2) DEFAULT 0")
    private Double averageScore;

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
