package com.casestudy.repository;

import com.casestudy.model.Category;
import com.casestudy.model.Topic;
import com.casestudy.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;




@Repository
public interface ITopicRepository extends PagingAndSortingRepository<Topic,Long> {
    @Query(nativeQuery = true,value = "select * from topics t order by topic_like desc limit 5")
    Iterable<Topic> findTopByTopicLike();
    Iterable<Topic> findAllByCategory(Category category);

    Page<Topic> findAllByTitleContaining(String firstname, Pageable pageable);

    Page<Topic> findByCategoryCateId(Long cateId,Pageable pageable);
    Page<Topic> findAllByUserId(Long id,Pageable pageable);

    @Query(nativeQuery = true,value = "select * from topics where topic_id in" +
            "(select topic_id from topics_hastag as th inner join hastag as h on th.hastag_id = h.hastag_id where th.hastag_id = ?1)")
    Page<Topic> findTopicByHastagId(Long hastagId,Pageable pageable);

    Page<Topic> findTopicByTitleContains(String title,Pageable pageable);
}
