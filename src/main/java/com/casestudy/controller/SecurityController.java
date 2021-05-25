package com.casestudy.controller;

import com.casestudy.model.Reply;
import com.casestudy.model.Topic;
import com.casestudy.model.User;
import com.casestudy.service.reply.IReplyService;
import com.casestudy.service.topic.ITopicService;
import com.casestudy.service.user.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class SecurityController {
    @Autowired
    private ITopicService topicService;
    @Autowired
    private IReplyService replyService;
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

    @ModelAttribute("userCurrent")
    public User userOptional(){
        return userService.findByUsername(getPrincipal()).get();
    }

    @GetMapping(value = {"/home"})
    public ModelAndView Homepage(RedirectAttributes redirectAttributes) {
//        model.addAttribute("user", getPrincipal());
        Optional<User> userCurrent = userService.findByUsername(getPrincipal());
        redirectAttributes.addFlashAttribute("user",getPrincipal());
        redirectAttributes.addFlashAttribute("userCurrent",userCurrent.get());
//        return "/views/index";
        return new ModelAndView("redirect:/");
    }

    @GetMapping("/admin")
    public String adminPage(ModelMap modelMap) {
        modelMap.addAttribute("user", getPrincipal());
        return "/views/index";
    }

    @GetMapping("/accessDenied")
    public String accessDeniedPage(ModelMap model) {
        model.addAttribute("user", getPrincipal());
        return "/views/access-Denied";
    }

    @GetMapping( "/dba")
    public String dbaPage(ModelMap model) {
        model.addAttribute("user", getPrincipal());
        return "redirect:cities";
    }

}
