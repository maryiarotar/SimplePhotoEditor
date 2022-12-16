package com.rotar.PhotoEditorWeb.controllers;


import com.rotar.PhotoEditorWeb.Models.UserEntity;
import com.rotar.PhotoEditorWeb.Services.PhotoService;
import com.rotar.PhotoEditorWeb.Services.SecurityService;
import com.rotar.PhotoEditorWeb.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("mypage")
public class PhotoController {

    @Autowired
    SecurityService securityService;

    @Autowired
    UserService userService;

    @Autowired
    PhotoService photoService;

    @GetMapping
    public String myPage(Model model){
        String name = securityService.findLoggedInUsername();
        Long userId = userService.getUserEntity(name).get().getUserId();
        model.addAttribute("helloUser", name);
        model.addAttribute("id", userId);
        model.addAttribute("photos", userService.getPhotoAlbum(userId));
        return "myPage";
    }

    @PostMapping("download")
    public String download(@RequestParam("file") MultipartFile photo,
                           Model model) throws IOException {
        String name = securityService.findLoggedInUsername();
        Optional<UserEntity> userExist = userService.getUserEntity(name);
        if (userExist.isPresent()){
            UserEntity user = userExist.get();
            photoService.savePhoto(user, photo);
        }
        return "redirect:/mypage";
    }

    @PostMapping("download-avatar")
    public String downloadAvatar(@RequestParam("file") MultipartFile photo,
                           Model model) throws IOException {
        String name = securityService.findLoggedInUsername();
        Optional<UserEntity> userExist = userService.getUserEntity(name);
        if (userExist.isPresent()){
            UserEntity user = userExist.get();
            photoService.saveAvatar(user, photo);
        }
        return "redirect:/mypage";
    }

    @PostMapping("delete/{photoId}")
    public String downloadAvatar(@PathVariable("photoId") Long photoId,
                                 Model model) throws IOException {
        photoService.deletePhoto(photoId);

        return "redirect:/mypage";
    }


}
