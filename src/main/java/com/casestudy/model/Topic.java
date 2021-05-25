package com.casestudy.model;



import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "topics")
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long topicId;

    @NotBlank
    @Size(max = 200)
    private String title;

    @NotBlank
    @Size(max = 1000)
    private String content;

    private LocalDateTime topicDate;

    private Long topicLike = 0l;

    private Long topicDislike = 0l;

    private Long topicStatus = 0l;

    private Long topicView = 0l;

    @ManyToOne
    private Category category;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "topics_hastag" ,
            joinColumns = @JoinColumn(name = "topicId"),
            inverseJoinColumns = @JoinColumn(name = "hastagId"))
    private List<Hastag> hastags = new ArrayList<>();

    @ManyToOne
    private User user;

    public Topic() {

    }

    public Topic(User user) {
        this.user = user;
    }

    public Topic(Long topicId, @NotBlank @Size(max = 200) String title, @NotBlank @Size(max = 1000) String content, LocalDateTime topicDate, Long topicLike, Long topicDislike, Long topicStatus, Long topicView, Category category, List<Hastag> hastags, User user) {
        this.topicId = topicId;
        this.title = title;
        this.content = content;
        this.topicDate = topicDate;
        this.topicLike = topicLike;
        this.topicDislike = topicDislike;
        this.topicStatus = topicStatus;
        this.topicView = topicView;
        this.category = category;
        this.hastags = hastags;
        this.user = user;
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long id) {
        this.topicId = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTopicDate() {
        return topicDate;
    }

    public void setTopicDate(LocalDateTime topicDate) {
        this.topicDate = topicDate;
    }

    public Long getTopicLike() {
        return topicLike;
    }

    public void setTopicLike(Long topicLike) {
        this.topicLike = topicLike;
    }

    public Long getTopicDislike() {
        return topicDislike;
    }

    public void setTopicDislike(Long topicDislike) {
        this.topicDislike = topicDislike;
    }

    public Long getTopicStatus() {
        return topicStatus;
    }

    public void setTopicStatus(Long topicStatus) {
        this.topicStatus = topicStatus;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Hastag> getRoles() {
        return hastags;
    }

    public void setRoles(List<Hastag> roles) {
        this.hastags = roles;
    }

    public List<Hastag> getHastags() {
        return hastags;
    }

    public void setHastags(List<Hastag> hastags) {
        this.hastags = hastags;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getTopicView() {
        return topicView;
    }

    public void setTopicView(Long topicView) {
        this.topicView = topicView;
    }

    public String getSimpleDate(){
        try {
            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String formatDateTime = this.topicDate.format(format);
            return formatDateTime;
        }catch (Exception e){
            return null;
        }
    }
}
