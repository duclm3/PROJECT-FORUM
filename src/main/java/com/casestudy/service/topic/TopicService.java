package com.casestudy.service.topic;

import com.casestudy.model.Category;
import com.casestudy.model.Topic;
import com.casestudy.model.User;
import com.casestudy.repository.ITopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TopicService implements ITopicService {
    @Autowired
    ITopicRepository topicRepository;
    @Override
    public Iterable<Topic> findAllByCategory(Category category) {
        return topicRepository.findAllByCategory(category);
    }


    @Override
    public Page<Topic> findAll(Pageable pageable) {
        return topicRepository.findAll(pageable);
    }

    public Iterable<Topic> findAllWithoutPage() {
        return topicRepository.findAll();
    }


    @Override
    public Page<Topic> findAllByTitleContaining(String title, Pageable pageable) {
        return topicRepository.findAllByTitleContaining(title,pageable);
    }

    @Override
    public Iterable<Topic> findTopByTopicLike() {
        return topicRepository.findTopByTopicLike();
    }


    @Override
    public Iterable<Topic> findAll() {
        return topicRepository.findAll();
    }

    @Override
    public Optional<Topic> findById(Long id) {
        return topicRepository.findById(id);
    }

    @Override
    public void save(Topic topic) {
        topicRepository.save(topic);
    }

    @Override
    public void remove(Long id) {
        topicRepository.deleteById(id);
    }

    @Override
    public Page<Topic> findByCategoryCateId(Long cateId, Pageable pageable) {
        return topicRepository.findByCategoryCateId(cateId,pageable);
    }

    @Override
    public Page<Topic> findByUserId(Long id, Pageable pageable) {
        return topicRepository.findAllByUserId(id,pageable);
    }

    @Override
    public Page<Topic> findTopicByHastagId(Long hastagId,Pageable pageable) {
        return topicRepository.findTopicByHastagId(hastagId,pageable);
    }

    @Override
    public Page<Topic> findTopicByTitle(String title,Pageable pageable) {
        return topicRepository.findTopicByTitleContains(title,pageable);
    }
}
