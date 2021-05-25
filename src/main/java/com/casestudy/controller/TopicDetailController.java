package com.casestudy.controller;

import com.casestudy.model.Reply;
import com.casestudy.model.Topic;
import com.casestudy.model.TopicUserLikeStatus;
import com.casestudy.model.User;
import com.casestudy.service.reply.IReplyService;
import com.casestudy.service.topic.ITopicService;
import com.casestudy.service.topicUserLike.TopicUserLikeServie;
import com.casestudy.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/detail/{id}")
public class TopicDetailController {
    @Autowired
    private ITopicService topicService;
    @Autowired
    private IReplyService replyService;
    @Autowired
    private IUserService userService;
    @Autowired
    private TopicUserLikeServie topicUserLikeService;

    private String getPrincipal() {
        String userName = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            userName = ((UserDetails) principal).getUsername();
        } else {
            userName = principal.toString();
        }
        return userName;
    }
    @GetMapping("/list")
    public ModelAndView showDetailTopic(@PathVariable Long id) {
        Optional<User> userCurrent = userService.findByUsername(getPrincipal());

        Optional<Topic> topic = topicService.findById(id);
        int a = replyService.countReplyByTopic(topic.get());
        Iterable<Reply> replies = replyService.findAllByTopic(topic.get());
        Iterable<Topic> topTopics = topicService.findTopByTopicLike();
        topic.get().setTopicView(topic.get().getTopicView() + 1); // tang moi khi an vao detail topic
        topicService.save(topic.get());
        ModelAndView modelAndView = new ModelAndView("/views/single-topic");
        modelAndView.addObject("topic", topic.get());
        modelAndView.addObject("topTopic", topTopics);
        modelAndView.addObject("replies", replies);
        modelAndView.addObject("replyCount",a);
        if (userCurrent.isPresent()){
            modelAndView.addObject("userCurrent", userCurrent.get());
            try {
                modelAndView.addObject("StatusLike",topicUserLikeService.findByTopicIdAndUserId(id,userCurrent.get().getId()).get().getStatus());
            }catch (Exception e){
                modelAndView.addObject("StatusLike","null");
                System.out.println("Loi nay");
            }
        }
        return modelAndView;
    }

    @PostMapping
    public ResponseEntity<Reply> createReply(@RequestBody Reply reply, @PathVariable Long id) {
        Optional<User> userCurrent = userService.findByUsername(getPrincipal());
        Optional<Topic> topic = topicService.findById(id);
        reply.setTopic(topic.get());
        reply.setUser(userCurrent.get());
        reply.setReplyLike(0L);
        reply.setReplyDislike(0L);
        reply.setCommentId(0L);
        reply.setReplyDate(LocalDateTime.now());

        return new ResponseEntity<>(replyService.save(reply), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Iterable<Reply>> allReply(@PathVariable Long id) {
        Optional<User> userCurrent = userService.findByUsername(getPrincipal());
        Optional<Topic> topic = topicService.findById(id);

        return new ResponseEntity<>(replyService.findAllByTopic(topic.get()),HttpStatus.OK);
    }
    @GetMapping("/haddleLikeTopic/{statusLike}")
    public ResponseEntity<Optional<Topic>> haddleLikeTopic(@PathVariable Long id,@PathVariable String statusLike) {
        Optional<User> userCurrent = userService.findByUsername(getPrincipal());
        Optional<Topic> topic = topicService.findById(id);
        if(userCurrent.isPresent()){
            if(statusLike.equals("like")){
                topicUserLikeService.save(new TopicUserLikeStatus(topic.get().getTopicId(),userCurrent.get().getId(),"like"));
            }else{
                topicUserLikeService.save(new TopicUserLikeStatus(topic.get().getTopicId(),userCurrent.get().getId(),"unlike"));
            }
        }
        try {
            if(statusLike.equals("like")){
                topic.get().setTopicLike(topic.get().getTopicLike() + 1l);
                topicService.save(topic.get());
            }else{
                topic.get().setTopicLike(topic.get().getTopicLike() - 1l);
                topicService.save(topic.get());
            }
        }catch (Exception e){}
        return new ResponseEntity<>(topicService.findById(id),HttpStatus.OK);
    }
    @GetMapping("/haddleLikeReply/{statusLike}/{replyId}")
    public ResponseEntity<Optional<Reply>> haddleLikeReply(@PathVariable String statusLike,@PathVariable Long replyId) {
        Optional<User> userCurrent = userService.findByUsername(getPrincipal());
        Optional<Reply> reply = replyService.findById(replyId);
        try {
            if(statusLike.equals("like")){
                reply.get().setReplyLike(reply.get().getReplyLike() + 1l);
                replyService.save(reply.get());
            }else{
                reply.get().setReplyLike(reply.get().getReplyLike() - 1l);
                replyService.save(reply.get());
            }
        }catch (Exception e){}
        return new ResponseEntity<>(replyService.findById(replyId),HttpStatus.OK);
    }
}