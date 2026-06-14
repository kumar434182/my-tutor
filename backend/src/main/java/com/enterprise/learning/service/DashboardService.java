package com.enterprise.learning.service;

import com.enterprise.learning.dto.DashboardStatsDTO;
import com.enterprise.learning.entity.Course;
import com.enterprise.learning.entity.LearnerProgress;
import com.enterprise.learning.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final LearnerProgressRepository learnerProgressRepository;
    private final AssessmentResultRepository assessmentResultRepository;

    public DashboardStatsDTO getAdminDashboard() {
        DashboardStatsDTO stats = new DashboardStatsDTO();
        
        stats.setTotalUsers(userRepository.count());
        stats.setTotalCourses(courseRepository.count());
        stats.setPublishedCourses((long) courseRepository.findPublishedCourses().size());
        stats.setTotalAssessments(assessmentResultRepository.count());
        
        return stats;
    }

    public DashboardStatsDTO getTrainerDashboard(Long trainerId) {
        DashboardStatsDTO stats = new DashboardStatsDTO();
        
        List<Course> trainerCourses = courseRepository.findByTrainerId(trainerId);
        stats.setTotalCourses((long) trainerCourses.size());
        stats.setPublishedCourses((long) trainerCourses.stream()
            .filter(c -> c.getStatus() == Course.CourseStatus.PUBLISHED)
            .count());
        
        long totalEnrolled = trainerCourses.stream()
            .mapToLong(c -> c.getEnrolledLearners().size())
            .sum();
        stats.setTotalLearners(totalEnrolled);
        
        return stats;
    }

    public DashboardStatsDTO getLearnerDashboard(Long learnerId) {
        DashboardStatsDTO stats = new DashboardStatsDTO();
        
        List<LearnerProgress> progress = learnerProgressRepository.findByLearnerId(learnerId);
        stats.setEnrolledCourses((long) progress.size());
        
        long completedCourses = progress.stream()
            .filter(p -> p.getStatus() == LearnerProgress.ProgressStatus.COMPLETED)
            .count();
        stats.setCompletedCourses(completedCourses);
        
        double avgProgress = progress.stream()
            .mapToDouble(LearnerProgress::getProgressPercentage)
            .average()
            .orElse(0);
        stats.setAverageProgress(avgProgress);
        
        return stats;
    }

    public List<LearnerProgress> getCourseProgress(Long courseId) {
        return learnerProgressRepository.findByCourseId(courseId);
    }

    public LearnerProgress updateLearnerProgress(Long learnerId, Long courseId, 
                                                 Integer lessonsCompleted, Integer totalLessons) {
        LearnerProgress progress = learnerProgressRepository
            .findByLearnerIdAndCourseId(learnerId, courseId)
            .orElseThrow(() -> new RuntimeException("Progress record not found"));

        progress.setLessonsCompleted(lessonsCompleted);
        progress.setTotalLessons(totalLessons);
        
        double progressPercentage = (double) lessonsCompleted / totalLessons * 100;
        progress.setProgressPercentage(progressPercentage);

        if (progressPercentage == 100) {
            progress.setStatus(LearnerProgress.ProgressStatus.COMPLETED);
        }

        return learnerProgressRepository.save(progress);
    }
}
