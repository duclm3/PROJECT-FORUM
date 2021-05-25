package com.casestudy.controller;

import com.casestudy.model.Category;
import com.casestudy.model.Hastag;
import com.casestudy.model.Topic;
import com.casestudy.model.User;
import com.casestudy.service.category.CategoryService;
import com.casestudy.service.hastag.HastagService;
import com.casestudy.service.reply.ReplyService;
import com.casestudy.service.topic.TopicService;
import com.casestudy.service.user.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.jws.Oneway;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;


@Controller
public class HomePageController {
    @Autowired
    private TopicService topicService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private HastagService hastagService;

    @Autowired
    private AppUserService userService;

    @Autowired
    private ReplyService replyService;

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
    private List<Category> categories(){
        List<Category> categories = new ArrayList<>();
        categoryService.findAll().forEach(categories::add);
        return categories;
    }

    @ModelAttribute
    private List<Hastag> hastags(){
        List<Hastag> hastags = new ArrayList<>();
        hastagService.getTheMostUsedHashtags().forEach(hastags::add);
        return hastags;
    }

    @ModelAttribute
    private Map<Long,List<Hastag>> listHastagWithTopicId(){
        Map<Long,List<Hastag>> listHastagWithTopicId = new HashMap<>();
        for (Topic topic: topicService.findAll()) {
            listHastagWithTopicId.put(topic.getTopicId(),topic.getHastags());
        }
        return  listHastagWithTopicId;
    }
    @ModelAttribute
    private Map<Long,Long> countReplyByTopicId(){
        Map<Long,Long> mapTopicIdAmountRepy = new HashMap<>();
        for (Topic topic : topicService.findAll()){
            if(replyService.countReplyByTopicId(topic.getTopicId()).isPresent()){
                mapTopicIdAmountRepy.put(topic.getTopicId(),replyService.countReplyByTopicId(topic.getTopicId()).get());
            }else{
                mapTopicIdAmountRepy.put(topic.getTopicId(),0l);
            }
        }
        return mapTopicIdAmountRepy;
    }

    @GetMapping(value = {"/"})
    public ModelAndView listTopic(@PageableDefault(sort = {"topicDate"}, direction = Sort.Direction.DESC, value = 5) Pageable pageable, @CookieValue(value = "setUser", defaultValue = "") String setUser,
                                  HttpServletResponse response, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("/views/index");
        modelAndView.addObject("categories",categories());
        modelAndView.addObject("hastags",hastags());
        modelAndView.addObject("listHastagWithTopicId", listHastagWithTopicId());
        modelAndView.addObject("topics", topicService.findAll(pageable));
        modelAndView.addObject("valueForPagination",new String[]{"homepage",""});
        modelAndView.addObject("countReplyByTopicId",countReplyByTopicId());
        Optional<User> userCurrent = userService.findByUsername(getPrincipal());
        if(userCurrent.isPresent()){
            modelAndView.addObject("userCurrent", userCurrent.get());

            if (userCurrent.get().getUsername() != null)
                setUser = userCurrent.get().getUsername();

            // create cookie and set it in response
            Cookie cookie = new Cookie("setUser", setUser);
            cookie.setMaxAge(24 * 60 * 60 * 10);
            response.addCookie(cookie);

            //get all cookies
            Cookie[] cookies = request.getCookies();
            //iterate each cookie
            for (Cookie ck : cookies) {
                //display only the cookie with the name 'setUser'
                if (ck.getName().equals("setUser")) {
                    modelAndView.addObject("cookieValue", ck);
                    break;
                } else {
                    ck.setValue("");
                    modelAndView.addObject("cookieValue", ck);
                    break;
                }
            }
//            model.addAttribute("message", "Login success. Welcome ");
        }


        return modelAndView;
    }

    @GetMapping(value = {"/get-topic-by-cate"})
    public ModelAndView getListByCategory(@RequestParam String id,@PageableDefault(sort = {"title"}, value = 3) Pageable pageable) {
        ModelAndView modelAndView;
        try {
            modelAndView = new ModelAndView("/views/index");
            modelAndView.addObject("topics", topicService.findByCategoryCateId(Long.parseLong(id),pageable));
            modelAndView.addObject("categories",categories());
            modelAndView.addObject("hastags",hastags());
            modelAndView.addObject("listHastagWithTopicId", listHastagWithTopicId());
            modelAndView.addObject("valueForPagination",new String[]{"categorypage",id});
            modelAndView.addObject("countReplyByTopicId",countReplyByTopicId());
        }catch (Exception e){  modelAndView = new ModelAndView("/views/404");}
        Optional<User> userCurrent = userService.findByUsername(getPrincipal());
        if(userCurrent.isPresent()){
            modelAndView.addObject("userCurrent", userCurrent.get());
        }
        return modelAndView;
    }

    @GetMapping(value = {"/get-topic-by-hastag"})
    public ModelAndView getListByHastag(@RequestParam String id,@PageableDefault(value = 3) Pageable pageable) {
        ModelAndView modelAndView;
        try {
            modelAndView = new ModelAndView("/views/index");
            modelAndView.addObject("topics", topicService.findTopicByHastagId(Long.parseLong(id),pageable));
            modelAndView.addObject("categories",categories());
            modelAndView.addObject("hastags",hastags());
            modelAndView.addObject("listHastagWithTopicId", listHastagWithTopicId());
            modelAndView.addObject("valueForPagination",new String[]{"hastagpage",id});
            modelAndView.addObject("countReplyByTopicId",countReplyByTopicId());
        }catch (Exception e){
            e.printStackTrace();
            modelAndView = new ModelAndView("/views/404");}
        Optional<User> userCurrent = userService.findByUsername(getPrincipal());
        if(userCurrent.isPresent()){
            modelAndView.addObject("userCurrent", userCurrent.get());
        }
        return modelAndView;
    }

    @GetMapping(value = {"/search"})
    public ModelAndView searchTopic(@PageableDefault(sort = {"title"}, value = 3) Pageable pageable,@RequestParam String  searchTopic) {
        ModelAndView modelAndView = new ModelAndView("/views/index");
        modelAndView.addObject("categories",categories());
        modelAndView.addObject("hastags",hastags());
        modelAndView.addObject("listHastagWithTopicId", listHastagWithTopicId());
        modelAndView.addObject("topics", topicService.findTopicByTitle(searchTopic,pageable));
        modelAndView.addObject("valueForPagination", new String[]{"searchpage",""});
        modelAndView.addObject("searchTopic",searchTopic);
        modelAndView.addObject("countReplyByTopicId",countReplyByTopicId());
        Optional<User> userCurrent = userService.findByUsername(getPrincipal());
        if(userCurrent.isPresent()){
            modelAndView.addObject("userCurrent", userCurrent.get());
        }
        return modelAndView;
    }

    @GetMapping(value = {"/login"})
    public ModelAndView test(@CookieValue(value = "setUser", defaultValue = "") String setUser) {
        ModelAndView modelAndView = new ModelAndView("/views/signin");
        Cookie cookie = new Cookie("setUser", setUser);
        modelAndView.addObject("cookieValue", cookie);
        return modelAndView;
    }

    @GetMapping(value = {"/login_error"})
    public ModelAndView loginError(@CookieValue(value = "setUser", defaultValue = "") String setUser) {
        ModelAndView modelAndView = new ModelAndView("/views/signin");
        modelAndView.addObject("error", "Incorrect username or password !!!");
        Cookie cookie = new Cookie("setUser", setUser);
        modelAndView.addObject("cookieValue", cookie);
        return modelAndView;
    }



}

