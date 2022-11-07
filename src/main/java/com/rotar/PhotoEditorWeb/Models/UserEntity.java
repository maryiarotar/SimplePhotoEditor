package com.rotar.PhotoEditorWeb.Models;


import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "user")
public class UserEntity {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long userId;

    @Column(name = "user_name")
    @NotBlank(message = "Enter name!")
    private String userName;

    @Column(name = "email")
    @Email
    private String email;

    @Column(name = "password")
    @NotBlank(message = "Enter password!")
    private String pass;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<PhotoAlbumEntity> photos;
}
