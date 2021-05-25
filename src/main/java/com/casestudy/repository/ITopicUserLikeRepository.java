package com.casestudy.repository;

import com.casestudy.model.TopicUserLikeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ITopicUserLikeRepository extends JpaRepository<TopicUserLikeStatus, Long> {
    Optional<TopicUserLikeStatus> findByTopicIdAndUserId(Long userId, Long topicId);
}
