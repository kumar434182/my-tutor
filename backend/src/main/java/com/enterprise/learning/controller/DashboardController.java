package com.enterprise.learning.controller;

import com.enterprise.learning.dto.DashboardStatsDTO;
import com.enterprise.learning.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "Dashboard and analytics endpoints")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get admin dashboard", description = "Get admin dashboard statistics")
    public ResponseEntity<DashboardStatsDTO> getAdminDashboard() {
        DashboardStatsDTO stats = dashboardService.getAdminDashboard();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/trainer/{trainerId}")
    @PreAuthorize("hasRole('TRAINER') or hasRole('ADMIN')")
    @Operation(summary = "Get trainer dashboard", description = "Get trainer dashboard statistics")
    public ResponseEntity<DashboardStatsDTO> getTrainerDashboard(@PathVariable Long trainerId) {
        DashboardStatsDTO stats = dashboardService.getTrainerDashboard(trainerId);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/learner/{learnerId}")
    @PreAuthorize("hasRole('LEARNER')")
    @Operation(summary = "Get learner dashboard", description = "Get learner dashboard statistics")
    public ResponseEntity<DashboardStatsDTO> getLearnerDashboard(@PathVariable Long learnerId) {
        DashboardStatsDTO stats = dashboardService.getLearnerDashboard(learnerId);
        return ResponseEntity.ok(stats);
    }
}
