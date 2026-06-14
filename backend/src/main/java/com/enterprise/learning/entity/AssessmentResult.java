package com.enterprise.learning.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "assessment_results")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssessmentResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assessment_id", nullable = false)
    private Assessment assessment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "learner_id", nullable = false)
    private User learner;

    @Column(columnDefinition = "DECIMAL(5,2)")
    private Double obtainedMarks;

    @Column(columnDefinition = "DECIMAL(5,2)")
    private Double totalMarks;

    @Column(columnDefinition = "DECIMAL(5,2)")
    private Double percentage;

    @Enumerated(EnumType.STRING)
    private ResultStatus status;

    @Column(columnDefinition = "INT DEFAULT 1")
    private Integer attemptNumber;

    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer timeTaken;

    @Column(updatable = false)
    private LocalDateTime submittedAt = LocalDateTime.now();

    private LocalDateTime reviewedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id")
    private User reviewer;

    @Column(columnDefinition = "TEXT")
    private String reviewerComments;

    public enum ResultStatus {
        SUBMITTED, PASSED, FAILED, PENDING_REVIEW
    }
}
