package com.enterprise.learning.controller;

import com.enterprise.learning.entity.Notification;
import com.enterprise.learning.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Tag(name = "Notifications", description = "Notification management endpoints")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    @PreAuthorize("hasRole('LEARNER') or hasRole('TRAINER') or hasRole('ADMIN')")
    @Operation(summary = "Get user notifications", description = "Get all notifications for current user")
    public ResponseEntity<List<Notification>> getUserNotifications(@RequestHeader("X-User-Id") Long userId) {
        List<Notification> notifications = notificationService.getUserNotifications(userId);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/unread")
    @PreAuthorize("hasRole('LEARNER') or hasRole('TRAINER') or hasRole('ADMIN')")
    @Operation(summary = "Get unread notifications", description = "Get all unread notifications for current user")
    public ResponseEntity<List<Notification>> getUnreadNotifications(@RequestHeader("X-User-Id") Long userId) {
        List<Notification> notifications = notificationService.getUnreadNotifications(userId);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/unread/count")
    @PreAuthorize("hasRole('LEARNER') or hasRole('TRAINER') or hasRole('ADMIN')")
    @Operation(summary = "Get unread notification count", description = "Get count of unread notifications")
    public ResponseEntity<Long> getUnreadCount(@RequestHeader("X-User-Id") Long userId) {
        long count = notificationService.getUnreadNotificationCount(userId);
        return ResponseEntity.ok(count);
    }

    @PutMapping("/{notificationId}/read")
    @PreAuthorize("hasRole('LEARNER') or hasRole('TRAINER') or hasRole('ADMIN')")
    @Operation(summary = "Mark notification as read", description = "Mark a notification as read")
    public ResponseEntity<Notification> markAsRead(@PathVariable Long notificationId) {
        Notification notification = notificationService.markAsRead(notificationId);
        return ResponseEntity.ok(notification);
    }

    @PutMapping("/read-all")
    @PreAuthorize("hasRole('LEARNER') or hasRole('TRAINER') or hasRole('ADMIN')")
    @Operation(summary = "Mark all notifications as read", description = "Mark all notifications as read for current user")
    public ResponseEntity<Void> markAllAsRead(@RequestHeader("X-User-Id") Long userId) {
        notificationService.markAllAsRead(userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{notificationId}")
    @PreAuthorize("hasRole('LEARNER') or hasRole('TRAINER') or hasRole('ADMIN')")
    @Operation(summary = "Delete notification", description = "Delete a notification")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long notificationId) {
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.noContent().build();
    }
}
