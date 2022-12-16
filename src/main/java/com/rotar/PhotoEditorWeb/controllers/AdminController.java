package com.rotar.PhotoEditorWeb.controllers;

import com.rotar.PhotoEditorWeb.Models.Dto.UserDto;
import com.rotar.PhotoEditorWeb.Services.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserServiceImpl userService;

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @GetMapping("show_users")
    public String userList(Model model) {
        model.addAttribute("allUsers", userService.getAll(null));
        return "show_users";
    }


    @PostMapping("delete/{email}")
    public String  deleteUser(@PathVariable("email") String email, Model model) {
        userService.deleteUser(email);
        model.addAttribute("allUsers", userService.getAll(null));
        logger.info("User with email={} was deleted", email);
        return "show_users";
    }

}
