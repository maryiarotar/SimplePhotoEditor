package com.rotar.PhotoEditorWeb.Repository;

import com.rotar.PhotoEditorWeb.Models.PhotoAlbumEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoAlbumRepository extends JpaRepository<PhotoAlbumEntity, Long> {

    @Query("SELECT id FROM PhotoAlbumEntity WHERE isAvatarImage = true AND t_user.id=?1") //либо true t_user
    Long getAvatarId(Long userId);

}
