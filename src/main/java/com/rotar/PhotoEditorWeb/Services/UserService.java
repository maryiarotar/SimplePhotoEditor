package com.rotar.PhotoEditorWeb.Services;

import com.rotar.PhotoEditorWeb.Models.Dto.UserDto;

import java.util.List;

public interface UserService {
    void add(UserDto userDto);
    List<UserDto> getAll(Long limit);
    UserDto getById(Long id);
    void delete(Long id);
    // методы возвращения листа с фотками???

}
