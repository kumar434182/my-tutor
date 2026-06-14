package com.enterprise.learning.repository;

import com.enterprise.learning.entity.CodeSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodeSubmissionRepository extends JpaRepository<CodeSubmission, Long> {
    List<CodeSubmission> findByLearnerId(Long learnerId);
    List<CodeSubmission> findByQuestionId(Long questionId);
    List<CodeSubmission> findByStatus(CodeSubmission.ExecutionStatus status);
}
