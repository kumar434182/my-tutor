package com.enterprise.learning.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "code_submissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CodeSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "learner_id", nullable = false)
    private User learner;

    @Column(columnDefinition = "LONGTEXT")
    private String sourceCode;

    @Enumerated(EnumType.STRING)
    private ProgrammingLanguage language;

    @Enumerated(EnumType.STRING)
    private ExecutionStatus status;

    @Column(columnDefinition = "TEXT")
    private String output;

    @Column(columnDefinition = "TEXT")
    private String errorMessage;

    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer testCasesPassed;

    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer totalTestCases;

    @Column(columnDefinition = "DECIMAL(5,2)")
    private Double executionTime;

    @Column(columnDefinition = "DECIMAL(5,2)")
    private Double marksObtained;

    @Column(updatable = false)
    private LocalDateTime submittedAt = LocalDateTime.now();

    public enum ProgrammingLanguage {
        JAVA, PYTHON, JAVASCRIPT, HTML_CSS, REACT, ANGULAR
    }

    public enum ExecutionStatus {
        PENDING, RUNNING, SUCCESS, COMPILATION_ERROR, RUNTIME_ERROR, TIMEOUT
    }
}
