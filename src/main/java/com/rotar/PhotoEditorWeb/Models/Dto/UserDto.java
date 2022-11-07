package com.rotar.PhotoEditorWeb.Models.Dto;

import lombok.Data;

@Data
public class UserDto {
    private long userId;
    private String userName;
    private String email;
    private String pass;

}
