package com.casestudy.service.reply;

import com.casestudy.model.Reply;
import com.casestudy.model.Topic;
import com.casestudy.repository.IReplyRepository;
import com.casestudy.service.user.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jws.WebMethod;
import java.util.Optional;

@Service
public class ReplyService implements IReplyService {
    @Autowired
    IReplyRepository replyRepository;
    @Autowired
    AppUserService userService;

    @Override
    public Iterable<Reply> findAll() {
        return replyRepository.findAll();
    }

    @Override
    public Optional<Reply> findById(Long id) {
        return replyRepository.findById(id);
    }

    @Override
    public Reply save(Reply reply) {
        return replyRepository.save(reply);
    }


    @Override
    public void remove(Long id) {
        replyRepository.deleteById(id);
    }

    @Override
    public Iterable<Reply> findAllByTopic(Topic topic) {
        return replyRepository.findAllByTopic(topic);
    }

    @Override
    public int countReplyByTopic(Topic topic) {
        return replyRepository.countReplyByTopic(topic);
    }

    @Override
    public Iterable<Reply> findAllByCommentId(Long id) {
        return replyRepository.findAllByCommentId(id);
    }

    @Override
    public Optional<Long> countReplyByTopicId(Long topicId) {
        return replyRepository.countReplyByTopicId(topicId);
    }
}
