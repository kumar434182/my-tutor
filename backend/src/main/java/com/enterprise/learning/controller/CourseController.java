package com.enterprise.learning.controller;

import com.enterprise.learning.dto.CourseDTO;
import com.enterprise.learning.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
@Tag(name = "Courses", description = "Course management endpoints")
public class CourseController {

    private final CourseService courseService;

    @PostMapping
    @PreAuthorize("hasRole('TRAINER') or hasRole('ADMIN')")
    @Operation(summary = "Create a new course", description = "Create a new course (Trainer/Admin only)")
    public ResponseEntity<CourseDTO> createCourse(@Valid @RequestBody CourseDTO courseDTO,
                                                   @RequestHeader("X-User-Id") Long trainerId) {
        CourseDTO created = courseService.createCourse(courseDTO, trainerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{courseId}")
    @PreAuthorize("hasRole('TRAINER') or hasRole('ADMIN')")
    @Operation(summary = "Update a course", description = "Update course details")
    public ResponseEntity<CourseDTO> updateCourse(@PathVariable Long courseId,
                                                  @Valid @RequestBody CourseDTO courseDTO) {
        CourseDTO updated = courseService.updateCourse(courseId, courseDTO);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/{courseId}/publish")
    @PreAuthorize("hasRole('TRAINER') or hasRole('ADMIN')")
    @Operation(summary = "Publish a course", description = "Publish a course to make it available to learners")
    public ResponseEntity<CourseDTO> publishCourse(@PathVariable Long courseId) {
        CourseDTO published = courseService.publishCourse(courseId);
        return ResponseEntity.ok(published);
    }

    @GetMapping("/published")
    @Operation(summary = "Get all published courses", description = "Retrieve all published courses")
    public ResponseEntity<List<CourseDTO>> getPublishedCourses() {
        List<CourseDTO> courses = courseService.getPublishedCourses();
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/trainer/{trainerId}")
    @PreAuthorize("hasRole('TRAINER') or hasRole('ADMIN')")
    @Operation(summary = "Get courses by trainer", description = "Get all courses created by a specific trainer")
    public ResponseEntity<List<CourseDTO>> getCoursesByTrainer(@PathVariable Long trainerId) {
        List<CourseDTO> courses = courseService.getCoursesByTrainer(trainerId);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/technology/{technology}")
    @Operation(summary = "Get courses by technology", description = "Get courses for a specific technology")
    public ResponseEntity<List<CourseDTO>> getCoursesByTechnology(@PathVariable String technology) {
        List<CourseDTO> courses = courseService.getCoursesByTechnology(technology);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/{courseId}")
    @Operation(summary = "Get course details", description = "Get detailed information about a specific course")
    public ResponseEntity<CourseDTO> getCourseById(@PathVariable Long courseId) {
        CourseDTO course = courseService.getCourseById(courseId);
        return ResponseEntity.ok(course);
    }

    @DeleteMapping("/{courseId}")
    @PreAuthorize("hasRole('TRAINER') or hasRole('ADMIN')")
    @Operation(summary = "Delete a course", description = "Delete a course (Trainer/Admin only)")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long courseId) {
        courseService.deleteCourse(courseId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{courseId}/enroll")
    @PreAuthorize("hasRole('LEARNER')")
    @Operation(summary = "Enroll in a course", description = "Enroll current user in a course")
    public ResponseEntity<CourseDTO> enrollCourse(@PathVariable Long courseId,
                                                  @RequestHeader("X-User-Id") Long learnerId) {
        CourseDTO enrolled = courseService.enrollLearner(courseId, learnerId);
        return ResponseEntity.ok(enrolled);
    }
}
