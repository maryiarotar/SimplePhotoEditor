package com.rotar.PhotoEditorWeb.controllers;

import com.rotar.PhotoEditorWeb.Models.PhotoAlbumEntity;
import com.rotar.PhotoEditorWeb.Models.UserEntity;
import com.rotar.PhotoEditorWeb.Services.PhotoService;
import com.rotar.PhotoEditorWeb.Services.SecurityService;
import com.rotar.PhotoEditorWeb.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Optional;

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
