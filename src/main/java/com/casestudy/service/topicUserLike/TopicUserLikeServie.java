package com.casestudy.service.topicUserLike;

import com.casestudy.model.TopicUserLikeStatus;
import com.casestudy.repository.ITopicUserLikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TopicUserLikeServie implements ITopicUserLikeService {
    @Autowired
    private ITopicUserLikeRepository topicUserLikeRepository;

    @Override
    public Iterable<TopicUserLikeStatus> findAll() {
        return topicUserLikeRepository.findAll();
    }

    @Override
    public Optional<TopicUserLikeStatus> findById(Long id) {
        return topicUserLikeRepository.findById(id);
    }

    @Override
    public void save(TopicUserLikeStatus topicUserLikeStatus) {
        topicUserLikeRepository.save(topicUserLikeStatus);
    }

    @Override
    public void remove(Long id) {
        topicUserLikeRepository.deleteById(id);
    }
    public Optional<TopicUserLikeStatus> findByTopicIdAndUserId(Long topicId, Long userId){
        return topicUserLikeRepository.findByTopicIdAndUserId(topicId,userId);
    }
}
