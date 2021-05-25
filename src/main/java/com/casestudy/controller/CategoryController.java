package com.casestudy.controller;

import com.casestudy.model.Category;
import com.casestudy.model.User;
import com.casestudy.service.category.CategoryService;
import com.casestudy.service.user.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;


@Controller
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

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

    @GetMapping("/categories")
    public ModelAndView listCategory(@RequestParam("text") Optional<String> search, @PageableDefault(value = 10) Pageable pageable) {
//        Page<city> categories = cityService.findAll(pageable);
//        Iterable<city> categories = citieservice.findAll();
        Page<Category> categories;
        if(search.isPresent()){
            categories = categoryService.findAllByCateNameContaining(search.get(), pageable);
        } else {
            categories = categoryService.findAll(pageable);
        }
        ModelAndView modelAndView = new ModelAndView("/front-dashboard/categories");
        modelAndView.addObject("userCurrent", userService.findByUsername(getPrincipal()).get());
        modelAndView.addObject("categories", categories);
        return modelAndView;
    }

    @GetMapping(value = {"/view-create-cate"})
    public ModelAndView viewCreateCategory() {
        ModelAndView modelAndView = new ModelAndView("/views/create-category");
        modelAndView.addObject("category", new Category());
        modelAndView.addObject("message","");
        modelAndView.addObject("userCurrent", userService.findByUsername(getPrincipal()).get());
        return modelAndView;
    }

    @PostMapping(value = {"/create-or-update-cate"})
    public ModelAndView createCategory(@Validated  @ModelAttribute Category category, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            ModelAndView modelAndView = new ModelAndView("/views/create-category");
            modelAndView.addObject("userCurrent", userService.findByUsername(getPrincipal()).get());
            return modelAndView;
        }

        if (categoryService.existsByCateName(category.getCateName())) {
                ModelAndView modelAndView = new ModelAndView("/views/create-category");
                modelAndView.addObject("error", "Category already exist !!!");
                modelAndView.addObject("userCurrent", userService.findByUsername(getPrincipal()).get());
                return modelAndView;
        }

        ModelAndView modelAndView = new ModelAndView("/views/create-category");
        String message = (category.getCateId() == null) ? "Successfully added new !" : "Update successful!";
        categoryService.save(category);
        modelAndView.addObject("category", new Category());
        modelAndView.addObject("message",message);
        modelAndView.addObject("userCurrent", userService.findByUsername(getPrincipal()).get());
        return modelAndView;
    }

    @GetMapping(value = {"/edit-cate/{id}"})
    public ModelAndView viewEditCategory(@PathVariable String id) {
        ModelAndView modelAndView;Category category;
        try {
            category = categoryService.findById(Long.parseLong(id)).get();
            modelAndView = new ModelAndView("/views/create-category");
            modelAndView.addObject("category", category);
            modelAndView.addObject("userCurrent", userService.findByUsername(getPrincipal()).get());
        }catch (Exception e){  modelAndView = new ModelAndView("/views/404");}
        return modelAndView;
    }

    @GetMapping("/delete-cate/{id}")
    public ModelAndView showDeleteForm(@PathVariable Long id) {
        Optional<Category> category = categoryService.findById(id);
        if (category.isPresent()) {
            ModelAndView modelAndView = new ModelAndView("/views/delete-category");
            modelAndView.addObject("category", category.get());
            modelAndView.addObject("userCurrent", userService.findByUsername(getPrincipal()).get());
            return modelAndView;

        } else {
            ModelAndView modelAndView = new ModelAndView("views/404");
            return modelAndView;
        }
    }

    @PostMapping("/delete-cate")
    public String deleteCity(@ModelAttribute("category") Category category) {
        categoryService.remove(category.getCateId());
        return "redirect:categories";
    }
}
