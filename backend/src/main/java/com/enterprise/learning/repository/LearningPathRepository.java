package com.enterprise.learning.repository;

import com.enterprise.learning.entity.LearningPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LearningPathRepository extends JpaRepository<LearningPath, Long> {
    List<LearningPath> findByTechnology(String technology);
    List<LearningPath> findByStatus(LearningPath.PathStatus status);
}
