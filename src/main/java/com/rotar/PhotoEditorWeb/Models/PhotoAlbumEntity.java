package com.rotar.PhotoEditorWeb.Models;


import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="photo_album")
public class PhotoAlbumEntity {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    private Long size;

    private String contentType;

    private String originalFilename;

    private boolean isAvatarImage;

    @Basic(fetch= FetchType.LAZY)
    @Lob
    @Type(type = "org.hibernate.type.ImageType")
    @Column(name = "photo")
    private byte[] photo;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "photos")
    private UserEntity t_user;

}
