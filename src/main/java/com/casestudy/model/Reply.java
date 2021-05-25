package com.casestudy.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Entity
@Table(name = "replies")
public class Reply{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long replyId;

    @Size(max = 1000)
    private String content;

    private Long replyLike =0l;

    private Long replyDislike =0l;

    private LocalDateTime replyDate;

    private String simpleDate = getSimpleDate();

    private Long commentId;

    @ManyToOne
    private User user;

    @ManyToOne
    private Topic topic;

    public Reply() {
    }

    public Reply(String content) {
        this.content = content;
    }

    public Reply(Long replyId, @Size(max = 1000) String content, Long replyLike, Long replyDislike, LocalDateTime replyDate, Long commentId, User user, Topic topic) {
        this.replyId = replyId;
        this.content = content;
        this.replyLike = replyLike;
        this.replyDislike = replyDislike;
        this.replyDate = replyDate;
        this.commentId = commentId;
        this.user = user;
        this.topic = topic;
    }

    public Long getReplyId() {
        return replyId;
    }

    public void setReplyId(Long replyId) {
        this.replyId = replyId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getReplyLike() {
        return replyLike;
    }

    public void setReplyLike(Long topicLike) {
        this.replyLike = topicLike;
    }

    public Long getReplyDislike() {
        return replyDislike;
    }

    public void setReplyDislike(Long topicDislike) {
        this.replyDislike = topicDislike;
    }

    public LocalDateTime getReplyDate() {
        return replyDate;
    }

    public void setReplyDate(LocalDateTime topicDate) {
        this.replyDate = topicDate;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public String getSimpleDate(){
        try {
            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String formatDateTime = this.replyDate.format(format);
            return formatDateTime;
        }catch (Exception e){
            return null;
        }
    }
}
