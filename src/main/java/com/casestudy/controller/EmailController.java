package com.casestudy.controller;

import com.casestudy.model.User;
import com.casestudy.service.user.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class EmailController {

    @Autowired
    public JavaMailSender emailSender;

    @Autowired
    private AppUserService userService;

    @GetMapping("/forgotPassword")
    public String form() {
        return "/views/forgot-password";
    }

    @ResponseBody
    @RequestMapping("/sendEmail")
    public ModelAndView sendSimpleEmail(@RequestParam("email") Optional<String> email, @RequestParam("username") Optional<String> username) {
        if (username.isPresent() && email.isPresent()) {
            Optional<User> user = userService.findByUsername(username.get());
            if (user.isPresent()) {
                if (user.get().getEmail().equalsIgnoreCase(email.get())) {
                    // Create a Simple MailMessage.
                    SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(MyConstants.FRIEND_EMAIL);
                    message.setTo(email.get());
                    message.setSubject("RETRIEVE YOUR PASSWORD");
                    message.setText("The password for the accoount '" + user.get().getUsername() + "': " + user.get().getPassword());

                    // Send Message!
                    this.emailSender.send(message);

                    ModelAndView modelAndView = new ModelAndView("/views/forgot-password");
                    modelAndView.addObject("success", "Please check your email to receive your password.");
                    return modelAndView;
                }
            }
        }

        ModelAndView modelAndView = new ModelAndView("/views/forgot-password");
        modelAndView.addObject("error", "Incorrect username or email !!!");
        return modelAndView;
    }

}