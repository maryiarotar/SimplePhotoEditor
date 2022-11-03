package com.rotar.PhotoEditorWeb.Models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import net.bytebuddy.dynamic.loading.InjectionClassLoader;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Data
@Entity
@Table(name = "user")
public class User {

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
