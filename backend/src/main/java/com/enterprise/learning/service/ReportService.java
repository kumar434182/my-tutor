package com.enterprise.learning.service;

import com.enterprise.learning.dto.ReportDTO;
import com.enterprise.learning.entity.Course;
import com.enterprise.learning.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final AssessmentResultRepository assessmentResultRepository;
    private final LearnerProgressRepository learnerProgressRepository;
    private final FeedbackRepository feedbackRepository;

    public ReportDTO generateCourseReport(Long courseId) {
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new RuntimeException("Course not found"));

        ReportDTO report = new ReportDTO();
        report.setTitle("Course Report: " + course.getTitle());
        report.setGeneratedAt(LocalDateTime.now());

        Map<String, Object> data = new HashMap<>();
        data.put("courseTitle", course.getTitle());
        data.put("trainer", course.getTrainer().getFirstName() + " " + course.getTrainer().getLastName());
        data.put("enrolledLearners", course.getEnrolledLearners().size());
        data.put("totalModules", course.getModules().size());
        
        Double avgProgress = learnerProgressRepository.getAverageProgressByCourse(courseId);
        data.put("averageProgress", avgProgress != null ? avgProgress : 0);
        
        Double avgRating = feedbackRepository.getAverageRatingByCourse(courseId);
        data.put("averageRating", avgRating != null ? avgRating : 0);

        report.setData(data);
        return report;
    }

    public ReportDTO generateLearnerReport(Long learnerId) {
        userRepository.findById(learnerId)
            .orElseThrow(() -> new RuntimeException("Learner not found"));

        ReportDTO report = new ReportDTO();
        report.setTitle("Learner Progress Report");
        report.setGeneratedAt(LocalDateTime.now());

        Map<String, Object> data = new HashMap<>();
        
        long enrolledCourses = learnerProgressRepository.findByLearnerId(learnerId).size();
        data.put("enrolledCourses", enrolledCourses);
        
        long completedCourses = learnerProgressRepository.findByLearnerId(learnerId).stream()
            .filter(p -> "COMPLETED".equals(p.getStatus().toString()))
            .count();
        data.put("completedCourses", completedCourses);
        
        long assessmentsTaken = assessmentResultRepository.findByLearnerId(learnerId).size();
        data.put("assessmentsTaken", assessmentsTaken);

        report.setData(data);
        return report;
    }

    public ReportDTO generatePlatformReport() {
        ReportDTO report = new ReportDTO();
        report.setTitle("Platform Analytics Report");
        report.setGeneratedAt(LocalDateTime.now());

        Map<String, Object> data = new HashMap<>();
        data.put("totalUsers", userRepository.count());
        data.put("totalCourses", courseRepository.count());
        data.put("publishedCourses", courseRepository.findPublishedCourses().size());
        data.put("totalAssessments", assessmentResultRepository.count());

        report.setData(data);
        return report;
    }
}
