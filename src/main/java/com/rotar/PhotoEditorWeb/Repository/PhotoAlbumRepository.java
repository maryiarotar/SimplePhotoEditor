package com.rotar.PhotoEditorWeb.Repository;

import com.rotar.PhotoEditorWeb.Models.PhotoAlbum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoAlbumRepository extends JpaRepository<PhotoAlbum, Long> {
}
