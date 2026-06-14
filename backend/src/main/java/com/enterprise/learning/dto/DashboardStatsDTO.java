package com.enterprise.learning.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardStatsDTO {
    private Long totalUsers;
    private Long totalCourses;
    private Long publishedCourses;
    private Long totalLearners;
    private Long enrolledCourses;
    private Long completedCourses;
    private Long totalAssessments;
    private Double averageProgress;
    private Double averageScore;
}
