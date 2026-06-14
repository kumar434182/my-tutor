package com.enterprise.learning.service;

import com.enterprise.learning.dto.CourseDTO;
import com.enterprise.learning.entity.Course;
import com.enterprise.learning.entity.User;
import com.enterprise.learning.repository.CourseRepository;
import com.enterprise.learning.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public CourseDTO createCourse(CourseDTO courseDTO, Long trainerId) {
        User trainer = userRepository.findById(trainerId)
            .orElseThrow(() -> new RuntimeException("Trainer not found"));

        Course course = Course.builder()
            .title(courseDTO.getTitle())
            .description(courseDTO.getDescription())
            .trainer(trainer)
            .technology(courseDTO.getTechnology())
            .level(courseDTO.getLevel())
            .category(courseDTO.getCategory())
            .duration(courseDTO.getDuration())
            .status(Course.CourseStatus.DRAFT)
            .build();

        Course savedCourse = courseRepository.save(course);
        log.info("Course created: {}", savedCourse.getId());
        return mapToDTO(savedCourse);
    }

    public CourseDTO updateCourse(Long courseId, CourseDTO courseDTO) {
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new RuntimeException("Course not found"));

        course.setTitle(courseDTO.getTitle());
        course.setDescription(courseDTO.getDescription());
        course.setTechnology(courseDTO.getTechnology());
        course.setLevel(courseDTO.getLevel());
        course.setCategory(courseDTO.getCategory());
        course.setDuration(courseDTO.getDuration());

        Course updatedCourse = courseRepository.save(course);
        return mapToDTO(updatedCourse);
    }

    public CourseDTO publishCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new RuntimeException("Course not found"));

        course.setStatus(Course.CourseStatus.PUBLISHED);
        Course publishedCourse = courseRepository.save(course);
        log.info("Course published: {}", courseId);
        return mapToDTO(publishedCourse);
    }

    public List<CourseDTO> getPublishedCourses() {
        return courseRepository.findPublishedCourses()
            .stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    public List<CourseDTO> getCoursesByTrainer(Long trainerId) {
        return courseRepository.findByTrainerId(trainerId)
            .stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    public List<CourseDTO> getCoursesByTechnology(String technology) {
        return courseRepository.findByTechnology(technology)
            .stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    public CourseDTO getCourseById(Long courseId) {
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new RuntimeException("Course not found"));
        return mapToDTO(course);
    }

    public void deleteCourse(Long courseId) {
        courseRepository.deleteById(courseId);
        log.info("Course deleted: {}", courseId);
    }

    public CourseDTO enrollLearner(Long courseId, Long learnerId) {
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new RuntimeException("Course not found"));
        User learner = userRepository.findById(learnerId)
            .orElseThrow(() -> new RuntimeException("Learner not found"));

        course.getEnrolledLearners().add(learner);
        Course updatedCourse = courseRepository.save(course);
        log.info("Learner {} enrolled in course {}", learnerId, courseId);
        return mapToDTO(updatedCourse);
    }

    private CourseDTO mapToDTO(Course course) {
        return CourseDTO.builder()
            .id(course.getId())
            .title(course.getTitle())
            .description(course.getDescription())
            .technology(course.getTechnology())
            .level(course.getLevel())
            .category(course.getCategory())
            .duration(course.getDuration())
            .status(course.getStatus())
            .trainerName(course.getTrainer().getFirstName() + " " + course.getTrainer().getLastName())
            .enrolledCount(course.getEnrolledLearners().size())
            .build();
    }
}
