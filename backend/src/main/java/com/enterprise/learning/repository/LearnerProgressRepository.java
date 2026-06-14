package com.enterprise.learning.repository;

import com.enterprise.learning.entity.LearnerProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LearnerProgressRepository extends JpaRepository<LearnerProgress, Long> {
    Optional<LearnerProgress> findByLearnerIdAndCourseId(Long learnerId, Long courseId);
    List<LearnerProgress> findByLearnerId(Long learnerId);
    List<LearnerProgress> findByCourseId(Long courseId);
    List<LearnerProgress> findByStatus(LearnerProgress.ProgressStatus status);
    
    @Query("SELECT AVG(lp.progressPercentage) FROM LearnerProgress lp WHERE lp.course.id = ?1")
    Double getAverageProgressByCourse(Long courseId);
}
