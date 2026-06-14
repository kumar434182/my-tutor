package com.enterprise.learning.controller;

import com.enterprise.learning.dto.ReportDTO;
import com.enterprise.learning.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
@Tag(name = "Reports", description = "Report generation endpoints")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasRole('TRAINER') or hasRole('ADMIN')")
    @Operation(summary = "Generate course report", description = "Generate a comprehensive course report")
    public ResponseEntity<ReportDTO> generateCourseReport(@PathVariable Long courseId) {
        ReportDTO report = reportService.generateCourseReport(courseId);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/learner/{learnerId}")
    @PreAuthorize("hasRole('LEARNER') or hasRole('ADMIN')")
    @Operation(summary = "Generate learner report", description = "Generate learner progress report")
    public ResponseEntity<ReportDTO> generateLearnerReport(@PathVariable Long learnerId) {
        ReportDTO report = reportService.generateLearnerReport(learnerId);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/platform")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Generate platform report", description = "Generate platform analytics report")
    public ResponseEntity<ReportDTO> generatePlatformReport() {
        ReportDTO report = reportService.generatePlatformReport();
        return ResponseEntity.ok(report);
    }
}
