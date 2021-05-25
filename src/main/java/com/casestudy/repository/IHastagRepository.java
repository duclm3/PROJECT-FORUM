package com.casestudy.repository;

import com.casestudy.model.Hastag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface IHastagRepository extends JpaRepository<Hastag,Long> {
    @Query(nativeQuery = true,value = "select h.hastag_id,max(hastag_name) as hastag_name,max(color) as color\n" +
            "from hastag as h inner join topics_hastag as th on h.hastag_id = th.hastag_id\n" +
            "group by hastag_id\n" +
            "order by count(topic_id) desc\n" +
            "limit 10;")
    Iterable<Hastag> getTheMostUsedHashtags();

    @Query(nativeQuery = true,value = "select h.hastag_id,hastag_name,color\n" +
            "from topics_hastag as th inner join hastag as h on th.hastag_id = h.hastag_id\n" +
            "where th.topic_id = ?1")
    Iterable<Hastag> getHastagByTopicId(Long topicId);
}

