package com.casestudy.service.topic;

import com.casestudy.model.Category;
import com.casestudy.model.Topic;
import com.casestudy.model.User;
import com.casestudy.service.IGeneralService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ITopicService extends IGeneralService<Topic> {
    Iterable<Topic> findAllByCategory(Category category);
    Page<Topic> findAll(Pageable pageable);
    Page<Topic> findAllByTitleContaining(String title, Pageable pageable);
    Iterable<Topic> findTopByTopicLike();
    Page<Topic> findByCategoryCateId(Long cateId, Pageable pageable);
    Page<Topic> findByUserId(Long id, Pageable pageable);
    Page<Topic> findTopicByHastagId(Long hastagId, Pageable pageable);
    Page<Topic> findTopicByTitle(String title,Pageable pageable);
}
