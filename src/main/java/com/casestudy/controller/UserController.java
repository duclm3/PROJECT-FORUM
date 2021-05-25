package com.casestudy.controller;

import com.casestudy.model.Role;
import com.casestudy.model.User;
import com.casestudy.model.UserForm;
import com.casestudy.service.user.AppUserService;
import com.casestudy.service.user.RoleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
public class UserController {
    @Autowired
    private AppUserService userService;

    @Autowired
    RoleServiceImpl roleService;

    @Value("${upload.path}")
    private String fileUpload;

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

    @GetMapping("/create-user")
    public ModelAndView showCreateForm() {
        ModelAndView modelAndView = new ModelAndView("/views/simple-signup");
//        modelAndView.addObject("user", new User());
        modelAndView.addObject("user", new UserForm());
        return modelAndView;
    }

    @PostMapping("/create-user")
    public ModelAndView saveUser(@Validated @ModelAttribute("user") UserForm userRequest, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            System.out.println("fail validate 1111111111111111111111111111111");
            return new ModelAndView("/views/simple-signup");
        }

        if (userService.existsByUsername(userRequest.getUsername())) {
            ModelAndView modelAndView = new ModelAndView("/views/simple-signup");
            modelAndView.addObject("error", "Username exist!!!");
            return modelAndView;
        }

        MultipartFile multipartFile = userRequest.getAvatar();
        String fileName = multipartFile.getOriginalFilename();
        try {
            FileCopyUtils.copy(userRequest.getAvatar().getBytes(), new File(this.fileUpload + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Creating user's account
        User user;
        if (fileName.equals("")) {
            user = new User(userRequest.getUsername(), userRequest.getPassword(), userRequest.getFullName(), userRequest.getEmail());
        } else {
            user = new User(userRequest.getUsername(), userRequest.getPassword(), userRequest.getFullName(), userRequest.getEmail(),fileName);
        }

//        Set<String> strRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        Role userRole = new Role(2L, "ROLE_MEMBER");
//        userRole.setName("ROLE_MEMBER");
//        userRole.setId(2L);
        roles.add(userRole);
        user.setRoles(roles);
        userService.save(user);

        System.out.println("fail gan end 111111111111111111111111");
//        userService.save(userRequest);
//        ModelAndView modelAndView = new ModelAndView("redirect:home");
        ModelAndView modelAndView = new ModelAndView("/views/simple-signup");
//        modelAndView.addObject("city", new City());
        modelAndView.addObject("success", "New user created successfully!");
//        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @GetMapping("/list-user")
    public ModelAndView listUser(@RequestParam("text") Optional<String> search, @PageableDefault(value = 10) Pageable pageable) {
//        Page<city> users = cityService.findAll(pageable);
//        Iterable<city> users = citieservice.findAll();
        Page<User> users;
        if(search.isPresent()){
            users = userService.findAllByUsernameContaining(search.get(), pageable);
        } else {
            users = userService.findAll(pageable);
        }

        Map<Long, String> listUser = new HashMap();
        for (User user: users) {
            boolean isAdmin = false;
            boolean isMod = false;
            boolean isMember = false;
            boolean isSus = false;
            for (Role role: user.getRoles()) {
                switch (role.getName()) {
                    case "ROLE_ADMIN":
                        isAdmin = true;
                        break;
                    case "ROLE_MOD":
                        isMod = true;
                        break;
                    case "ROLE_SUS":
                        isSus = true;
                        break;
                    case "ROLE_MEMBER":
                        isMember = true;
                        break;
                }
            }
            if (isAdmin) {
                listUser.put(user.getId(), "ADMIN");
            } else if (isMod) {
                listUser.put(user.getId(), "MOD");
            } else if (isSus){
                listUser.put(user.getId(), "SUS");
            } else if (isMember){
                listUser.put(user.getId(), "MEMBER");
            }
        }

        ModelAndView modelAndView = new ModelAndView("/front-dashboard/users");
        modelAndView.addObject("users", users);
        modelAndView.addObject("listUser", listUser);
        modelAndView.addObject("userCurrent", userService.findByUsername(getPrincipal()).get());
        return modelAndView;
    }

    @GetMapping("/edit-user/{id}")
    public ModelAndView showEditForm(@PathVariable Long id) {
        Optional<User> userCurrent = userService.findByUsername(getPrincipal());
        if (!userCurrent.get().getId().equals(id)) {
            for (Role role: userCurrent.get().getRoles()) {
                if (role.getName().equals("ROLE_ADMIN")) {
                    break;
                }
                ModelAndView modelAndView = new ModelAndView("/views/access-Denied");
                return modelAndView;
            }
        }

        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            String userRole = user.get().getRoles().toString();
            for (Role role: user.get().getRoles()) {
//                if (role.getName().equals("ROLE_SUS")) {
//                    userRole = "ROLE_SUS";
//                    break;
//                }
                userRole = role.getName();
            }

            ModelAndView modelAndView = new ModelAndView("/front-dashboard/edit-user");
            modelAndView.addObject("user", user.get());
            modelAndView.addObject("userRole", userRole);
            modelAndView.addObject("userCurrent", userService.findByUsername(getPrincipal()).get());
            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView("/views/404");
            return modelAndView;
        }
    }

    @PostMapping("/edit-user")
    public ModelAndView updateCity(@Validated @ModelAttribute("user") UserForm userRequest, BindingResult bindingResult,
                                   @RequestParam("role") Optional<String> roleText, @RequestParam("confirmPassword") Optional<String> confirmPassword) {
        Optional<User> userTemp = userService.findById(userRequest.getId());

        MultipartFile multipartFile = userRequest.getAvatar();
        String fileName = multipartFile.getOriginalFilename();
        try {
            FileCopyUtils.copy(userRequest.getAvatar().getBytes(), new File(this.fileUpload + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (Objects.equals(fileName, "")) {
            if (userTemp.isPresent()) {
                fileName = userTemp.get().getAvatar();
            }
        }

        if (bindingResult.hasFieldErrors()) {
            ModelAndView modelAndView = new ModelAndView("/front-dashboard/edit-user");
            modelAndView.addObject("userCurrent", userService.findByUsername(getPrincipal()).get());
            modelAndView.addObject("userAvatar", fileName);
            modelAndView.addObject("userRole", roleText.get());
            return modelAndView;
        }

        if (userService.existsByUsername(userRequest.getUsername())) {
            if (!userRequest.getUsername().equals(userTemp.get().getUsername())) {
                ModelAndView modelAndView = new ModelAndView("/front-dashboard/edit-user");
                modelAndView.addObject("error", "Username exist!!!");
                modelAndView.addObject("userAvatar", fileName);
                modelAndView.addObject("userCurrent", userService.findByUsername(getPrincipal()).get());
                modelAndView.addObject("userRole", roleText.get());
                return modelAndView;
            }
        }

        if (confirmPassword.isPresent()) {
            if (!confirmPassword.get().equals(userRequest.getPassword())) {
                ModelAndView modelAndView = new ModelAndView("/front-dashboard/edit-user");
                modelAndView.addObject("userCurrent", userService.findByUsername(getPrincipal()).get());
                modelAndView.addObject("error", "Confirmation password is not correct!!!");
                modelAndView.addObject("userAvatar", fileName);
                modelAndView.addObject("userRole", roleText.get());
                return modelAndView;
            }
        }

        User user = new User(userRequest.getId(),userRequest.getUsername(), userRequest.getPassword(), userRequest.getFullName(), fileName,
                userRequest.getLevel(), userRequest.getDateCreate(), userRequest.getEmail());


        if (roleText.isPresent()) {
            Set<Role> roles = new HashSet<>();
            List<Role> userRole = new ArrayList<>();

            if (roleText.get().equals("ROLE_ADMIN")) {
                userRole.add(new Role(1L, "ROLE_ADMIN"));
//            userRole.add(new Role(4L, "ROLE_MOD"));
//            userRole.add(new Role(2L, "ROLE_MEMBER"));
            } else if (roleText.get().equals("ROLE_MOD")) {
                userRole.add(new Role(4L, "ROLE_MOD"));
//            userRole.add(new Role(2L, "ROLE_MEMBER"));
            } else if (roleText.get().equals("ROLE_MEMBER")) {
                userRole.add(new Role(2L, "ROLE_MEMBER"));
            } else {
                userRole.add(new Role(5L, "ROLE_SUS"));
//                userRole.add(new Role(2L, "ROLE_MEMBER"));
            }

            roles.addAll(userRole);

            user.setRoles(roles);
        }

        userService.save(user);
        ModelAndView modelAndView = new ModelAndView("/front-dashboard/edit-user");
        modelAndView.addObject("user", user);
        modelAndView.addObject("userCurrent", userService.findByUsername(getPrincipal()).get());
        modelAndView.addObject("userRole", roleText.get());
        modelAndView.addObject("success", "User updated successfully");
        return modelAndView;
    }

    @GetMapping("/delete-user/{id}")
    public ModelAndView showDeleteForm(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            ModelAndView modelAndView = new ModelAndView("/front-dashboard/delete-user");
            modelAndView.addObject("user", user.get());
            modelAndView.addObject("userCurrent", userService.findByUsername(getPrincipal()).get());
            return modelAndView;

        } else {
            ModelAndView modelAndView = new ModelAndView("views/404");
            return modelAndView;
        }
    }

    @PostMapping("/delete-user")
    public String deleteCity(@ModelAttribute("user") User user) {
        userService.remove(user.getId());
        return "redirect:list-user";
    }
}
