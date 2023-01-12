package com.rotar.PhotoEditorWeb.controllers;


import com.rotar.PhotoEditorWeb.Models.Filling;
import com.rotar.PhotoEditorWeb.Models.PhotoAlbumEntity;
import com.rotar.PhotoEditorWeb.Models.UserEntity;
import com.rotar.PhotoEditorWeb.Services.PhotoService;
import com.rotar.PhotoEditorWeb.Services.SecurityService;
import com.rotar.PhotoEditorWeb.Services.UserService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
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

    private static final Logger logger = LoggerFactory.getLogger(PhotoController.class);

    @GetMapping
    public String myPage(Model model){
        String name = securityService.findLoggedInUsername();
        Long userId = userService.getUserEntity(name).get().getUserId();
        model.addAttribute("helloUser", name);
        model.addAttribute("id", userId);
        model.addAttribute("photos", userService.getPhotoAlbum(userId));

        //при переходе на домашнюю страницу чистить папку со временными файлами
        //хотя если много пользователей то надо по id фотографии удалять
      //  File oldFile = new File("./src/main/resources/temp/"+*+".png");
       // oldFile.delete();

        List<PhotoAlbumEntity> photoAlbum = userService.getPhotoAlbum(userId);

        for (PhotoAlbumEntity photo : photoAlbum){
            Long photoId = photo.getId();
            if (new File("./src/main/resources/temp/"+photoId+".png").exists()) {
                File f = new File("./src/main/resources/temp/" + photoId + ".png");
                f.delete();
            }

        }


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

    @GetMapping("delete/{photoId}")
    public String deletePhoto(@PathVariable("photoId") Long photoId,
                                 Model model) throws IOException {
        photoService.deletePhoto(photoId);

        return "redirect:/mypage";
    }

    @GetMapping("edit/{photoId}")
    public String editPhoto(@PathVariable("photoId") Long photoId,
                               Model model) throws IOException {
        model.addAttribute("photoId", photoId);

        model.addAttribute("red", 0);
        model.addAttribute("green", 0);
        model.addAttribute("blue", 0);
        model.addAttribute("dark", 0);
        model.addAttribute("filling", "FLAT");

        return "edit";
    }

    @GetMapping("/edit/{photoId}-true")
    public String editTempPhoto(@ModelAttribute("red") Float r,
                                @ModelAttribute("green") Float g,
                                @ModelAttribute("blue") Float b,
                                @ModelAttribute("dark") Float dark,
                                @RequestParam("RGB") String rgb,
                                @ModelAttribute("radio") String filling,
                                @PathVariable("photoId") Long photoId,
                                Model model) throws IOException {


        File f = new File("./src/main/resources/temp/"+photoId+".png");
        ByteArrayInputStream bais = new ByteArrayInputStream(FileUtils.readFileToByteArray(f));
        MBFImage image = ImageUtilities.readMBF(bais);
        MBFImage clone = image.clone();

        int h = image.getHeight();
        int w = image.getWidth();

        int bandRGB;
        Float color = 0f;
        if (rgb.compareTo("darker")==0){
            bandRGB = 3;
            clone = clone.add(dark);
        }
        else {
            bandRGB = (rgb.compareTo("red") == 0) ? 0 : ((rgb.compareTo("green") == 0) ? 1 : 2);
            color = (bandRGB == 0) ? r : ((bandRGB == 1) ? g : b);
        }
System.out.println("bandRgb { "+bandRGB+"} _ "+rgb+", color {" + color+ "} _ " +
        "r="+r+", g="+g+", b="+b+", fill as "+filling);


        //САМУ ОБРАБОТКУ ПЕРЕНЕСТИ В ДР КЛАСС И ИСПРАВИТЬ НА ЗАВИСИМОСТЬ ОТ ШИРИНЫ И ВЫСОТЫ ГРАДИЕНТЫ
        float d=0;
        if (filling.compareTo("VERTICAL") == 0){

            for (int y=0; y<(h / 2)+1; y++) {
                for (int x = 0; x < (w / 2)+1; x++) {
                    clone.getBand(2).pixels[y][x] = d;
                    clone.getBand(2).pixels[y][w - x - 1] = d;
                    clone.getBand(2).pixels[h - y - 1][x] = d;
                    clone.getBand(2).pixels[h - y - 1][w - x - 1] = d;
                    if (d < 1.0) {d += 0.01;}
                }
                d=0f;
            }
        }
        else if (filling.compareTo("HORIZONTAL") == 0){

            for (int y=0; y<(h / 2)+1; y++) {
                for (int x = 0; x < (w / 2)+1; x++) {
                    clone.getBand(2).pixels[y][x] = d;
                    clone.getBand(2).pixels[y][w - x - 1] = d;
                    clone.getBand(2).pixels[h - y - 1][x] = d;
                    clone.getBand(2).pixels[h - y - 1][w - x - 1] = d;
                }
                if (d < 1.0) {d += 0.01;}
            }
        }
        else {
            if (rgb.compareTo("darker")!=0) {
                clone.getBand(bandRGB).fill(color);
            }
        }

        File oldFile = new File("./src/main/resources/temp/"+photoId+".png");
        oldFile.delete();
        ImageUtilities.write(clone, new File("./src/main/resources/temp/"+photoId+".png"));



        model.addAttribute("red", r);
        model.addAttribute("green", g);
        model.addAttribute("blue", b);
        model.addAttribute("dark", dark);
        model.addAttribute("filling", "FLAT");


        return "edit";
    }

    @PostMapping("edit/refresh/{photoId}")
    public String refresh(@PathVariable("photoId") Long photoId){
        if (new File("./src/main/resources/temp/"+photoId+".png").exists()) {
            File f = new File("./src/main/resources/temp/"+photoId+".png");
            f.delete();
        }

        return "redirect:/mypage/edit/"+photoId;
    }

    @PostMapping("edit/save/{photoId}")
    public String saveEditedPhoto(@PathVariable("photoId") Long photoId) throws IOException {
        if (new File("./src/main/resources/temp/"+photoId+".png").exists()) {
            File f = new File("./src/main/resources/temp/"+photoId+".png");
            String name = securityService.findLoggedInUsername();
            Optional<UserEntity> user = userService.getUserEntity(name);
            logger.info("trying to save edited photo...");
            if (user.isPresent()) {

                FileInputStream input = new FileInputStream(f);
                MultipartFile multipartFile = new MockMultipartFile("ed-"+photoId,
                        f.getName(), "image/jpeg", IOUtils.toByteArray(input));

                photoService.savePhoto(user.get(), multipartFile);
                logger.info("A photo {} for user {} saved SUCCESSFULLY", f.getName(), user.get().getUsername());
            }
            else {
                logger.info("Some problems with saving a photo");
            }
        }

        return "redirect:/mypage/edit/"+photoId;
    }

}
