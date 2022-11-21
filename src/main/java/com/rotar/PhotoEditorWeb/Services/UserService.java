package com.rotar.PhotoEditorWeb.Services;

import com.rotar.PhotoEditorWeb.Models.Dto.UserDto;
import com.rotar.PhotoEditorWeb.Models.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {
    void addUser(UserDto userDto, boolean nExp,boolean nLck, boolean crdts, boolean en);
    List<UserDto> getAll(Long limit);
   Optional<UserDto> getByEmail(String email);
    boolean deleteUser(String email);
    // методы возвращения листа с фотками???



}
