package com.enterprise.learning.repository;

import com.enterprise.learning.entity.DiscussionForum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiscussionForumRepository extends JpaRepository<DiscussionForum, Long> {
    List<DiscussionForum> findByCourseId(Long courseId);
    List<DiscussionForum> findByCreatedById(Long userId);
}
