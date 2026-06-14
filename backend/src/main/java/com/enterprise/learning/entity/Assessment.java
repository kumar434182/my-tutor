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
@Table(name = "assessments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Assessment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(columnDefinition = "DECIMAL(5,2)")
    private Double totalMarks;

    @Column(columnDefinition = "DECIMAL(5,2)")
    private Double passingMarks;

    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer timeLimit;

    @Column(columnDefinition = "INT DEFAULT 1")
    private Integer maxAttempts;

    @Column(columnDefinition = "DECIMAL(5,2) DEFAULT 0")
    private Double negativeMarking;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(20) DEFAULT 'DRAFT'")
    private AssessmentStatus status = AssessmentStatus.DRAFT;

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "assessment", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Question> questions = new HashSet<>();

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum AssessmentStatus {
        DRAFT, PUBLISHED, ARCHIVED
    }
}
