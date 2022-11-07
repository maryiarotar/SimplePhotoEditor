package com.rotar.PhotoEditorWeb.Repository;

import com.rotar.PhotoEditorWeb.Models.PhotoAlbumEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoAlbumRepository extends JpaRepository<PhotoAlbumEntity, Long> {
}
