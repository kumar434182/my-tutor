package com.enterprise.learning.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "certificates")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Certificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "learner_id", nullable = false)
    private User learner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(nullable = false, length = 100)
    private String certificateNumber;

    @Column(columnDefinition = "DECIMAL(5,2)")
    private Double scoreObtained;

    @Column(updatable = false)
    private LocalDateTime issuedAt = LocalDateTime.now();

    private LocalDateTime expiresAt;

    @Column(length = 500)
    private String certificateUrl;

    @Enumerated(EnumType.STRING)
    private CertificateStatus status;

    public enum CertificateStatus {
        ISSUED, REVOKED, EXPIRED
    }
}
