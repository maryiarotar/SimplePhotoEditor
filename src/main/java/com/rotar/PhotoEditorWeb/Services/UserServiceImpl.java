package com.rotar.PhotoEditorWeb.Services;

import com.rotar.PhotoEditorWeb.Models.Dto.PhotoAlbumDto;
import com.rotar.PhotoEditorWeb.Models.Dto.UserDto;
import com.rotar.PhotoEditorWeb.Models.UserEntity;
import com.rotar.PhotoEditorWeb.Repository.PhotoAlbumRepository;
import com.rotar.PhotoEditorWeb.Repository.UserRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Data
@Service
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;

    @Override
    public void add(UserDto userDto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName(userDto.getUserName());
        userEntity.setEmail(userDto.getEmail());
        userEntity.setPass(userDto.getPass());
        userRepository.save(userEntity);
    }

    @Override
    public List<UserDto> getAll(Long limit) {
        List<UserDto> userDtoList = new ArrayList<>();
        List<UserEntity> userEntities = userRepository.findAll();
        if (limit != null) {
            for (int i = 0; i < limit; i++) {
                UserDto userDto = new UserDto();
                userDto.setUserId(userEntities.get(i).getUserId());
                userDto.setUserName(userEntities.get(i).getUserName());
                userDto.setEmail(userEntities.get(i).getEmail());
                userDto.setPass(userEntities.get(i).getPass());
                userDtoList.add(userDto);
                if (i== userEntities.size()-1){
                    break;
                }
            }
        } else {
            for (UserEntity user : userEntities){
                UserDto userDto = new UserDto();
                userDto.setUserId(user.getUserId());
                userDto.setUserName(user.getUserName());
                userDto.setEmail(user.getEmail());
                userDto.setPass(user.getPass());
                userDtoList.add(userDto);
            }
        }
        return null;
    }

    @Override
    public UserDto getById(Long id) {

        return null;
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }


}
