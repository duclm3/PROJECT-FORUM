package com.casestudy.repository;

import com.casestudy.model.Category;
import com.casestudy.model.Reply;
import com.casestudy.model.Topic;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IReplyRepository extends PagingAndSortingRepository<Reply,Long> {
    Iterable<Reply> findAllByTopic(Topic topic);
    int countReplyByTopic(Topic topic);
    Iterable<Reply> findAllByCommentId(Long id);

    @Query(nativeQuery = true,value = "select count(reply_id) from replies where topic_topic_id = ?1 group by topic_topic_id;")
    Optional<Long> countReplyByTopicId(Long topicId);
}
