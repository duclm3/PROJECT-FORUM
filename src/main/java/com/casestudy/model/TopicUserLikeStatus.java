package com.casestudy.model;

import javax.persistence.*;

@Entity @IdClass(TopicUserLike.class)
@Table(name = "topic_user_like")
public class TopicUserLikeStatus {
    @Id
    private Long topicId;
    @Id
    private Long userId;
    private String status;
    public TopicUserLikeStatus() {
    }

    public TopicUserLikeStatus(Long topicId, Long userId, String status) {
        this.topicId = topicId;
        this.userId = userId;
        this.status = status;
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
