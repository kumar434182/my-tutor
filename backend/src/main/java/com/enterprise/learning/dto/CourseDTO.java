package com.enterprise.learning.dto;

import com.enterprise.learning.entity.Course;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseDTO {
    private Long id;
    private String title;
    private String description;
    private String technology;
    private Course.CourseLevel level;
    private String category;
    private Double duration;
    private Course.CourseStatus status;
    private String trainerName;
    private Integer enrolledCount;
}
