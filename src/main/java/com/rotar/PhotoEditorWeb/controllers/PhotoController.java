package com.rotar.PhotoEditorWeb.controllers;


import com.rotar.PhotoEditorWeb.Models.PhotoAlbumEntity;
import com.rotar.PhotoEditorWeb.Models.UserEntity;
import com.rotar.PhotoEditorWeb.Services.PhotoService;
import com.rotar.PhotoEditorWeb.Services.SecurityService;
import com.rotar.PhotoEditorWeb.Services.UserService;
import org.apache.commons.io.FileUtils;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
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

    @DeleteMapping("delete/{photoId}")
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

        return "edit";
    }

    @GetMapping("/edit/{photoId}-true")
    public String editTempPhoto(@ModelAttribute("red") Float r,
                                @ModelAttribute("green") Float g,
                                @ModelAttribute("blue") Float b,
                                @RequestParam("RGB") String rgb,
                                @PathVariable("photoId") Long photoId,
                                Model model) throws IOException {


        File f = new File("./src/main/resources/temp/"+photoId+".png");
        ByteArrayInputStream bais = new ByteArrayInputStream(FileUtils.readFileToByteArray(f));
        MBFImage image = ImageUtilities.readMBF(bais);
        MBFImage clone = image.clone();

        int h1 = image.getHeight();
        int w1 = image.getWidth();

        int bandRGB = (rgb.compareTo("red") == 0) ? 0 : ((rgb.compareTo("green") == 0) ? 1 : 2);
        Float color = (bandRGB == 0) ? r : ((bandRGB == 1) ? g : b);

        for (int y = 0; y < h1; y++) {
            for (int x = 0; x < w1; x++) {
                clone.getBand(bandRGB).pixels[y][x] = color; //from 0 to 1
                //clone.getBand(2).pixels[y][x] = color;
            }
        }

        File oldFile = new File("./src/main/resources/temp/"+photoId+".png");
        oldFile.delete();
        ImageUtilities.write(clone, new File("./src/main/resources/temp/"+photoId+".png"));

//для труе - типа фотка уже есть, здесь получаешь RGB значения, изменяешь временную фотку и
        // и редирект editPhoto который ниже -только ему добавить фолс
        // и в нем проверяешь если темп.жпег == текущему и прикрепляешь измененное фото уже
//можно именовать файл одинаково - чтоб сравнивал
        //а когда выходишь из главной страницы или сораняешь фотографию, то
        //то временный файл удаляешь


        model.addAttribute("red", r);
        model.addAttribute("green", g);
        model.addAttribute("blue", b);

        String strColor = (bandRGB == 0) ? "red" : ((bandRGB == 1) ? "green" : "blue");
        model.addAttribute(strColor, color);


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

}
