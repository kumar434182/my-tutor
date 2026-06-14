package com.enterprise.learning.repository;

import com.enterprise.learning.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByAssessmentId(Long assessmentId);
    List<Question> findByType(Question.QuestionType type);
    List<Question> findByDifficulty(Question.DifficultyLevel difficulty);
}
