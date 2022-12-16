package com.rotar.PhotoEditorWeb.controllers;

import com.rotar.PhotoEditorWeb.Models.Dto.UserDto;
import com.rotar.PhotoEditorWeb.Services.SecurityService;
import com.rotar.PhotoEditorWeb.Services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserService userService;

@Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("home")
    public String goHome2() {
        return "index";
    }

/*
    @ModelAttribute("model")
    public UserDto userDto(){
        return new UserDto();
    }
*/
    @GetMapping(value = "registration")
    public String goAuthorization(Model model) {
        model.addAttribute("user", new UserDto());
        return "registration";
    }

    @PostMapping("registration-done") //ПОМЕНЯТЬ НА GET??
    public String makeRegistration(@Valid UserDto user, BindingResult result, Model model) {
        //add model to show name of user
        if (result.hasErrors()) {
            return "registration";
        }
        Optional<UserDto> isExist = userService.getByEmail(user.getEmail());
        if (isExist.isPresent()){
            UserDto userExist= new UserDto();
            userExist.setUserName("USER IS ALREADY EXISTS!!! ------:  "
                    + isExist.get().getUserName());
            userExist.setEmail("USER IS ALREADY EXISTS!!! ------:  " +
                    isExist.get().getEmail());
            userExist.setPass("USER IS ALREADY EXISTS!!! ------:  " +
                    "**********");
            model.addAttribute("user", userExist);
            model.addAttribute("message", "THIS USER EXISTS!"); //>??????
            logger.error("User with email={} is not registrated", user.getEmail());
        }
        else {
            userService.addUser(user, true, true, true, true);
            model.addAttribute("message", "USER IS ADDED SUCCESSFULLY");
            model.addAttribute("user", user);
            logger.info("User with email={} was registrated", user.getEmail());
        }

        return "result";

    }

    @GetMapping("login")
    public String login(Model model){
        model.addAttribute("signUser", new UserDto());
        return "login";
    }

    @GetMapping("welcome")
    public String welcome(Model model){
        String us = securityService.findLoggedInUsername();
        model.addAttribute("signUser", us);

        return "welcome";
    }


    @PostMapping("login-done")
    public String loginDone(@Valid UserDto signUser,
                        BindingResult result, Model model){
        if (result.hasErrors()) {
            return "login";
        }
        Optional<UserDto> isExist = userService.getByEmail(signUser.getEmail());
        if (isExist.isPresent()){
            String thisPass = isExist.get().getPass();
            if (bCryptPasswordEncoder.matches(signUser.getPass(), thisPass)){
                System.out.println("------ PASSWORD IS TRUE...------");
                model.addAttribute("signUser", isExist.get());
                securityService.autoLogin(isExist.get().getUserName(), thisPass);
                System.out.println("-------USER IS LOGGENED----------");
                logger.info("User with email={} was logged in", signUser.getEmail());
                return "welcome";
            } else {
                System.out.println("PASSWORD NOT VALID!");
                model.addAttribute("message", "PASSWORD NOT VALID!");
                logger.info("User with email={} was not logged in! PASSWORD is NOT VALID!");
                return login(model);
            }
        }
        return "welcome";

    }


}
