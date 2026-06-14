package com.enterprise.learning.repository;

import com.enterprise.learning.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByCourseId(Long courseId);
    List<Feedback> findByType(Feedback.FeedbackType type);
    
    @Query("SELECT AVG(f.rating) FROM Feedback f WHERE f.course.id = ?1")
    Double getAverageRatingByCourse(Long courseId);
}
