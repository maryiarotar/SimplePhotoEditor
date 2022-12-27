package com.rotar.PhotoEditorWeb.controllers;

import com.rotar.PhotoEditorWeb.Models.PhotoAlbumEntity;
import com.rotar.PhotoEditorWeb.Services.PhotoService;
import com.rotar.PhotoEditorWeb.Services.SecurityService;
import com.rotar.PhotoEditorWeb.Services.UserService;

import org.apache.commons.io.FileUtils;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.*;


@RestController
@RequestMapping("album")
public class RestPhotoController {

    @Autowired
    SecurityService securityService;

    @Autowired
    UserService userService;

    @Autowired
    PhotoService photoService;


    @GetMapping("/display/{photoId}")
    public ResponseEntity<?> getPhoto(@PathVariable("photoId") Long photoId) {
        if (photoId != null) {
            PhotoAlbumEntity photo = photoService.getPhotoById(photoId).orElse(null);

            if (photo != null) {
                return ResponseEntity.ok()
                        .header("fileName", photo.getName())
                        .contentType(MediaType.valueOf(photo.getContentType()))
                        .contentLength(photo.getSize())
                        .body(new InputStreamResource(new ByteArrayInputStream(photo.getPhoto())));
            }
        }
            return (ResponseEntity<?>) ResponseEntity.noContent();
    }

    @GetMapping("/edit/{photoId}-true")
    public String editTempPhoto(@PathVariable("photoId") Long photoId) throws IOException {



//для труе - типа фотка уже есть, здесь получаешь RGB значения, изменяешь временную фотку и
        // и редирект editPhoto который ниже -только ему добавить фолс
        // и в нем проверяешь если темп.жпег == текущему и прикрепляешь измененное фото уже
//можно именовать файл одинаково - чтоб сравнивал
        //а когда выходишь из главной страницы или сораняешь фотографию, то
        //то временный файл удаляешь

        return "redirect:/edit/"+ photoId + "-false";
    }

    @GetMapping("/edit/{photoId}-false")
    public ResponseEntity<?> editPhoto(@PathVariable("photoId") Long photoId) throws IOException {

        if (photoId != null) {
            PhotoAlbumEntity photo = photoService.getPhotoById(photoId).orElse(null);
            File f;
            if (new File("./src/main/resources/temp/tmp.png").exists()) {
                f = new File("./src/main/resources/temp/tmp.png");
            } else {
                f = null;
            }

            if (photo != null) {

                System.out.println("________________________________________________" +
                        f.hashCode() + "__________________________________________"+
                        photo.getPhoto().hashCode() + "__________________________________________"+
                        f.getName() + " ____ "+ photo.getName());

                if (f.length() == photo.getSize()) {
                    ByteArrayInputStream bais = new ByteArrayInputStream(FileUtils.readFileToByteArray(f));

                    return ResponseEntity.ok()
                            .header("redactedImage", photo.getName())
                            .contentType(MediaType.IMAGE_JPEG)
                            .contentLength(f.length())
                            .body(new InputStreamResource(bais));
                } else {
                    InputStream imgStream = new ByteArrayInputStream(photo.getPhoto());
                    MBFImage image = ImageUtilities.readMBF(imgStream);
                    MBFImage clone = image.clone();

                    int h1 = image.getHeight();
                    int w1 = image.getWidth();

/*              //СЖАТИЕ ФОТОГРАФИИ алгоритм ближайшего соседа
                int h2=h1/2, w2=w1/2;
                if (h1 > 300 && w1 > 300) {
                    MBFImage mbf = new MBFImage(w2, h2);
                    int x_ratio = (int) ((w1 << 16) / w2) + 1;
                    int y_ratio = (int) ((h1 << 16) / h2) + 1;
                    int x2, y2;
                    for (int i = 0; i < h2; i++) {
                        for (int j = 0; j < w2; j++) {
                            x2 = ((j * x_ratio) >> 16);
                            y2 = ((i * y_ratio) >> 16);
                            mbf.setPixel(j, i, clone.getPixel(x2, y2));
                            mbf.getBand(1).pixels[i][j] = 0;
                            mbf.getBand(2).pixels[i][j] = 0;
                        }
                    }
                    clone = mbf;
                }*/

                    for (int y = 0; y < h1; y++) {
                        for (int x = 0; x < w1; x++) {
                            clone.getBand(1).pixels[y][x] = 0;
                            clone.getBand(2).pixels[y][x] = 0;
                        }
                    }


                    File oldFile = new File("./src/main/resources/temp/tmp.png");
                    oldFile.delete();
                    ImageUtilities.write(clone, new File("./src/main/resources/temp/tmp.png"));
                    File newF = new File("./src/main/resources/temp/tmp.png");

                    ByteArrayInputStream bais = new ByteArrayInputStream(FileUtils.readFileToByteArray(newF));

                    return ResponseEntity.ok()
                            .header("redactedImage", photo.getName())
                            .contentType(MediaType.IMAGE_JPEG)
                            .contentLength(newF.length())
                            .body(new InputStreamResource(bais));
                    //.body(new InputStreamResource(new ByteArrayInputStream(photo.getPhoto())));
                }
            }}

            return (ResponseEntity<?>) ResponseEntity.noContent();
        }



    @GetMapping("/{id}")
    public ResponseEntity<?> getAvatar(@PathVariable Long id){
        //String name = securityService.findLoggedInUsername();
        //Optional<UserEntity> userExist = userService.getUserEntity(name);
        //if (userExist.isPresent()){
         //   Long photoId = userExist.get().getUserId();


           PhotoAlbumEntity photo = photoService.getAvatarPhoto(id).orElse(null);
            if (photo != null){
                return ResponseEntity.ok()
                    .header("fileName", photo.getName())
                    .contentType(MediaType.valueOf(photo.getContentType()))
                    .contentLength(photo.getSize())
                    .body(new InputStreamResource(new ByteArrayInputStream(photo.getPhoto())));

            }
        //}

        return (ResponseEntity<?>) ResponseEntity.noContent();
    }








}
