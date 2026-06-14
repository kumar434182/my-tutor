package com.enterprise.learning.repository;

import com.enterprise.learning.entity.AssessmentResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssessmentResultRepository extends JpaRepository<AssessmentResult, Long> {
    List<AssessmentResult> findByLearnerId(Long learnerId);
    List<AssessmentResult> findByAssessmentId(Long assessmentId);
    Optional<AssessmentResult> findByAssessmentIdAndLearnerId(Long assessmentId, Long learnerId);
    List<AssessmentResult> findByStatus(AssessmentResult.ResultStatus status);
}
