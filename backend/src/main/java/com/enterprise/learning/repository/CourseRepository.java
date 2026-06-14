package com.enterprise.learning.repository;

import com.enterprise.learning.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByStatus(Course.CourseStatus status);
    List<Course> findByTrainerId(Long trainerId);
    List<Course> findByTechnology(String technology);
    List<Course> findByLevel(Course.CourseLevel level);
    
    @Query("SELECT c FROM Course c WHERE c.status = 'PUBLISHED' ORDER BY c.createdAt DESC")
    List<Course> findPublishedCourses();
}
