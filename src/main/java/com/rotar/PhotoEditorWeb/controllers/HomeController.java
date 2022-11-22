package com.rotar.PhotoEditorWeb.controllers;

import com.rotar.PhotoEditorWeb.Models.Dto.UserDto;
import com.rotar.PhotoEditorWeb.Services.SecurityService;
import com.rotar.PhotoEditorWeb.Services.UserService;
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
            return "registration"; //ИЗМЕНИТЬ ТК ВЫЛЕТАЕТ ОШИБКА!!!!!!
        }
        Optional<UserDto> isExist = userService.getByEmail(user.getEmail());
        if (isExist.isPresent()){
            UserDto userExist= new UserDto();
            userExist.setUserName("USER IS ALREADY EXISTS!!! ------:  "
                    + isExist.get().getUserName());
            userExist.setEmail("USER IS ALREADY EXISTS!!! ------:  " +
                    isExist.get().getEmail());
            userExist.setPass("USER IS ALREADY EXISTS!!! ------:  " +
                    isExist.get().getPass());
            model.addAttribute("user", userExist);
            model.addAttribute("message", "THIS USER EXISTS!"); //>??????
        }
        else {
            userService.addUser(user, true, true, true, true);
            model.addAttribute("message", "USER IS ADDED SUCCESSFULLY");
            model.addAttribute("user", user);
        }

        System.out.println("------ HELLO TO ....... " + user.getUserName());
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
        System.out.println("name is ----------> " + us);
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
                return "welcome";
            } else {
                System.out.println("PASSWORD NOT VALID!");
                model.addAttribute("message", "PASSWORD NOT VALID!");
                return login(model);
            }
        }
        return "welcome";

    }


}
