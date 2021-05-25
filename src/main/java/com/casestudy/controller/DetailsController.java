package com.casestudy.controller;

import com.casestudy.model.*;
import com.casestudy.service.category.CategoryService;
import com.casestudy.service.hastag.HastagService;
import com.casestudy.service.topic.TopicService;
import com.casestudy.service.user.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.*;

@Controller
public class DetailsController {
    @Autowired
    private TopicService topicService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private HastagService hastagService;
    @Autowired
    private AppUserService userService;

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


    @ModelAttribute
    private List<Category> categories() {
        List<Category> categories = new ArrayList<>();
        categoryService.findAll().forEach(categories::add);
        return categories;
    }

    @ModelAttribute
    private List<Hastag> hastags() {
        List<Hastag> hastags = new ArrayList<>();
        hastagService.getTheMostUsedHashtags().forEach(hastags::add);
        return hastags;
    }

    @GetMapping("/detail-user/{id}")
    public ModelAndView showProfileUser(@PathVariable Long id) {
        ModelAndView modelAndView;
        modelAndView = new ModelAndView("/views/profile-about");
        Optional<User> user = userService.findById(id);
        modelAndView.addObject("user", user.get());
        Optional<User> userCurrent = userService.findByUsername(getPrincipal());
        if (userCurrent.isPresent()) {
            modelAndView.addObject("userCurrent", userCurrent.get());
        }
        return modelAndView;
    }

    @GetMapping("/detail-user/topic/{id}")
    public ModelAndView showTopic(@PathVariable Long id, @PageableDefault(sort = {"title"}, value = 3) Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("/views/profile-topic");
        modelAndView.addObject("topic", new Topic());
        Optional<User> user = userService.findById(id);
        modelAndView.addObject("user", user.get());
        modelAndView.addObject("topics", topicService.findByUserId(id, pageable));
        modelAndView.addObject("categories", categories());
        modelAndView.addObject("hastags", hastags());
        Optional<User> userCurrent = userService.findByUsername(getPrincipal());
        if (userCurrent.isPresent()) {
            modelAndView.addObject("userCurrent", userCurrent.get());
        }
        return modelAndView;
    }


    @GetMapping("/edit-topic/{id}")
    public ModelAndView showEditTopic(@PathVariable Long id) {
        Optional<Topic> topic = topicService.findById(id);
        Optional<User> userCurrent = userService.findByUsername(getPrincipal());

        if (topic.isPresent()) {
            ModelAndView modelAndView = new ModelAndView("/views/edit-topic");
            if (userCurrent.isPresent()) {
                modelAndView.addObject("userCurrent", userCurrent.get());
            }
            modelAndView.addObject("topic", topic.get());
            modelAndView.addObject("categories", categories());
            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView("/views/404");
            return modelAndView;
        }

    }


    @PostMapping("/edit-topic")
    public ModelAndView editTopic(@Validated @ModelAttribute("topic") Topic topic, @RequestParam String inputHastag, BindingResult bindingResult) {
//        ModelAndView modelAndView = new ModelAndView("/views/edit-topic");
//        String[] arrayHastag = inputHastag.trim().split(",");
//        List<Hastag> hastagList = new ArrayList<>();
//        System.out.println("inputHastag:" +inputHastag);
//        boolean checkHastag = true,checkTopic = true;
//        // Check độ dài của mỗi hastag min = 1 ,max = 8, tối đa 5 hastag
//        for (int i = 0; i < arrayHastag.length; i++) {
//            if(arrayHastag[i].length() <1 || arrayHastag.length>8 || arrayHastag.length > 5){
//                checkHastag = false;
//                checkTopic = false;
//                break;
//            }
//        }
//        // Insert hastag mới
//        for (int i = 0; i < arrayHastag.length; i++) {
//            try {
//                Hastag hastag = hastagService.saveAndReturn(new Hastag(arrayHastag[1]));
//                hastagList.add(hastag);
//            }catch (Exception e){ checkHastag = false; checkTopic = false; break; }
//        }
//        // Insert topic mới
//        if(checkTopic){
//            topic.setHastags(hastagList);
//            topic.setTopicDate(LocalDateTime.now());
//            topicService.save(topic);
//        }
//
//        Optional<User> userCurrent = userService.findByUsername(getPrincipal());
//        modelAndView.addObject("userCurrent",userCurrent.get());
//        modelAndView.addObject("topic", new Topic());
//        modelAndView.addObject("checkTopic", checkTopic);
//        modelAndView.addObject("checkHastag", checkHastag);
//        return modelAndView;

        ModelAndView modelAndView = new ModelAndView("/views/create-topic");
        String[] arrayHastag = inputHastag.trim().split(",");
        List<Hastag> hastagSet = new ArrayList<>();
        Optional<User> userCurrent = userService.findByUsername(getPrincipal());
        System.out.println("inputHastag:" +inputHastag);
        boolean checkHastag = true,checkTopic = true;
        // Check độ dài của mỗi hastag min = 1 ,max = 8, tối đa 5 hastag
        for (int i = 0; i < arrayHastag.length; i++) {
            if(arrayHastag[i].length() <1 || arrayHastag[i].length()>8 || arrayHastag.length > 5){
                checkHastag = false;
                checkTopic = false;
                break;
            }
        }
        // Insert hastag mới
        for (int i = 0; i < arrayHastag.length; i++) {
            try {
                Hastag hastag = hastagService.saveAndReturn(new Hastag(arrayHastag[i]));
                hastagSet.add(hastag);
            }catch (Exception e){ checkHastag = false; checkTopic = false; break; }
        }
        // Insert topic mới
        if(checkTopic){
            topic.setHastags(hastagSet);
            topic.setTopicDate(LocalDateTime.now());
            topic.setUser(userCurrent.get());
            topicService.save(topic);
        }


        modelAndView.addObject("userCurrent",userCurrent.get());
        modelAndView.addObject("topic", new Topic());
        modelAndView.addObject("checkTopic", checkTopic);
        modelAndView.addObject("checkHastag", checkHastag);
        return modelAndView;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Topic> deleteTopic(@PathVariable Long id) {
        Optional<Topic> topic = topicService.findById(id);
        if (!topic.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        topicService.remove(id);
        return new ResponseEntity<>(topic.get(), HttpStatus.NO_CONTENT);
    }

    @GetMapping("/delete-topic-post/{id}")
    public ModelAndView showDeleteForm(@PathVariable Long id) {
        Optional<Topic> topic = topicService.findById(id);
//        Optional<User> userCurrent = userService.findByUsername(getPrincipal());
        if (topic.isPresent()) {
            ModelAndView modelAndView = new ModelAndView("/views/delete-topic");
            modelAndView.addObject("topic", topic.get());
            modelAndView.addObject("userCurrent", userService.findByUsername(getPrincipal()).get());
            return modelAndView;

        } else {
            ModelAndView modelAndView = new ModelAndView("views/404");
            return modelAndView;
        }
    }

    @PostMapping("/delete-topic-post")
    public String deleteCity(@ModelAttribute("topic") Topic topic) {
        topicService.remove(topic.getTopicId());
        return "redirect:/";
    }
}
