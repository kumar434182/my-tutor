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
@Table(name = "courses", indexes = {
    @Index(name = "idx_course_status", columnList = "status"),
    @Index(name = "idx_course_trainer", columnList = "trainer_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id", nullable = false)
    private User trainer;

    @Column(length = 100)
    private String technology;

    @Enumerated(EnumType.STRING)
    private CourseLevel level;

    @Column(length = 50)
    private String category;

    @Column(columnDefinition = "DECIMAL(5,2)")
    private Double duration;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(20) DEFAULT 'DRAFT'")
    private CourseStatus status = CourseStatus.DRAFT;

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Module> modules = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "course_enrollments",
        joinColumns = @JoinColumn(name = "course_id"),
        inverseJoinColumns = @JoinColumn(name = "learner_id")
    )
    @Builder.Default
    private Set<User> enrolledLearners = new HashSet<>();

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum CourseLevel {
        BEGINNER, INTERMEDIATE, ADVANCED
    }

    public enum CourseStatus {
        DRAFT, PUBLISHED, ARCHIVED
    }
}
