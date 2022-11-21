package com.rotar.PhotoEditorWeb.Models.Dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserDto {
    @NotNull(message = "Name is mandatory")
    private String userName;
    @NotNull(message = "Email is mandatory")
    private String email;
    @NotNull(message = "Pass is mandatory")
    private String pass;

}
