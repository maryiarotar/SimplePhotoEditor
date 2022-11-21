package com.rotar.PhotoEditorWeb.Models;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="photo_album")
public class PhotoAlbumEntity {

    //ЭТО ДБ ТАБЛ many to one - много фото один юзер
    // ЛИБО СДЕЛАТЬ ОДЕЛЬГУЮ ТАБЛ С ФОТАМИ, А ЭТА ПОСЕРЕДИНЕ ЧТОБЫ ОТ ЮЗЕРОВ ТОЖЕ ВИДНЫ БЫЛИ ФОТКИ



    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="photo_id")
    private long photoId;

    @Getter
    @Setter
    @Column(name="user_id")
    private long userId;

    @Getter
    @Setter
    @Basic(fetch= FetchType.LAZY)
    @Lob
    @Column(name = "photo")
    private byte photo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photos")
    private UserEntity t_user;

}