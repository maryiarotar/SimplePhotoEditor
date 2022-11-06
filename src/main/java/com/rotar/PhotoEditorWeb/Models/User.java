package com.rotar.PhotoEditorWeb.Models;


import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Data
@Entity
@Table(name = "user")
public class User {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "user_name")
    @NotBlank(message = "Enter name!")
    private String userName;

    @Column(name = "email")
    @Email
    private String email;

    @Column(name = "password")
    @NotBlank(message = "Enter password!")
    private String pass;

}
