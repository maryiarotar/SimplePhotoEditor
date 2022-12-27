package com.rotar.PhotoEditorWeb.Services;

import com.rotar.PhotoEditorWeb.Models.PhotoAlbumEntity;
import com.rotar.PhotoEditorWeb.Models.UserEntity;
import com.rotar.PhotoEditorWeb.Repository.PhotoAlbumRepository;
import com.rotar.PhotoEditorWeb.Repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class PhotoService {

    private static final Logger logger = LoggerFactory.getLogger(PhotoService.class);

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PhotoAlbumRepository photoRepository;

    public void savePhoto(UserEntity user, MultipartFile file) throws IOException {
        PhotoAlbumEntity photo;
        if (file.getSize() != 0){
            photo = toPhotoAlbumEntity(file);
            photo.setAvatarImage(false);
            user.addPhotoToProfile(photo);
            photoRepository.save(photo);
            userRepository.save(user); //должно обновлять строчку с фотографиями??
            logger.info("Saved new photo = {}, size = {}, author = {}", photo.getName(), photo.getSize(), user.getEmail());
        }
    }

    public Long saveAvatar(UserEntity user, MultipartFile file) throws IOException {
        PhotoAlbumEntity photo;
        Long id = null;
        if (file.getSize() != 0){
            //здесь переставить предыдуший аватар на false. в юзерэнтити обон-ся автомат-ки
            Long oldPhotoId = user.getAvatarId();

            if (oldPhotoId != null){
                PhotoAlbumEntity oldAvatar = photoRepository.findById(oldPhotoId).orElse(null);
                if (oldAvatar != null) {
                    oldAvatar.setAvatarImage(false); //Устаревшее, когда фото не удалялись
                    photoRepository.deleteById(oldPhotoId);
                }
            }

            photo = toPhotoAlbumEntity(file);
            photo.setAvatarImage(true);
            user.addPhotoToProfile(photo);
            photoRepository.save(photo);
            id = photoRepository.getAvatarId(user.getUserId());

            user.setAvatarId(id);
            System.out.println("______Id of avatar -> " + id + " -< from user id= " + user.getUserId());
            userRepository.save(user); //дб отдельная таблица с ид аватарок
            return id;
        }
        logger.info("Profile = {}, avatar_id = {}", user.getEmail(), id);
        return null;
    }

    public Optional<PhotoAlbumEntity> getPhotoById(Long id){
        return photoRepository.findById(id);
    }

    public Optional<PhotoAlbumEntity> getAvatarPhoto(Long userId){
        Long photoId = photoRepository.getAvatarId(userId);
        Optional<PhotoAlbumEntity> photo = getPhotoById(photoId);
        return photo;
    }

    // updateAvatar() с заменой ид аватарки и переставить на булеан в табл с фото



//    public List<PhotoAlbumEntity> findAll(Long id){
//        photoRepository.findById(id);
//
//    }


    private PhotoAlbumEntity toPhotoAlbumEntity(MultipartFile file) throws IOException {
        PhotoAlbumEntity photo = new PhotoAlbumEntity();
        photo.setName(file.getName());
        photo.setSize(file.getSize());
        photo.setContentType(file.getContentType());
        photo.setOriginalFilename(file.getOriginalFilename());
        photo.setPhoto(file.getBytes());
        return photo;
    }

    public void deletePhoto(Long photoId) {
        photoRepository.deleteById(photoId);
        logger.info("Photo with ID={} was deleted from Repository", photoId);
    }



/*    public void deletePhoto(Long photoId, Long userId) {

        Optional<UserEntity> user = userRepository.findById(userId);
        if (user.isPresent()) {
            List<PhotoAlbumEntity> photos = user.get().getPhotos();
            photos.remove(photos.indexOf(photoId));
            user.get().setPhotos(photos);
            userRepository.save();
            logger.info("User with ID={}", id);
            return true;
        }
        logger.info("User photo with email={} was not deleted", email);
        return false;
    }*/

}
