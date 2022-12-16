package com.rotar.PhotoEditorWeb.Services;

import com.rotar.PhotoEditorWeb.Models.Dto.UserDto;
import com.rotar.PhotoEditorWeb.Models.PhotoAlbumEntity;
import com.rotar.PhotoEditorWeb.Models.Role;
import com.rotar.PhotoEditorWeb.Models.UserEntity;
import com.rotar.PhotoEditorWeb.Repository.RoleRepository;
import com.rotar.PhotoEditorWeb.Repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service("UserServiceImpl")
public class UserServiceImpl implements UserService{

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }


    @Override
    public void addUser(UserDto userDto, boolean nExp,boolean nLck, boolean crdts, boolean en) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName(userDto.getUserName());
        userEntity.setEmail(userDto.getEmail());
        userEntity.setPass(bCryptPasswordEncoder.encode(userDto.getPass()));
        //userEntity.setPhotos(null); //??????????????
        userEntity.setAccountNonExpired(nExp);
        userEntity.setAccountNonLocked(nLck);
        userEntity.setCredentialsNonExpired(crdts);
        userEntity.setEnabled(en);
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.getReferenceById(2l));
        userEntity.setRoles(roles);
        //userEntity.setRoles(Collections.singleton(new Role(1L, "ROLE_ADMIN")));
        userRepository.save(userEntity);
        logger.info("User {} was saved.", userEntity);

    }

    @Override
    public List<UserDto> getAll(Long limit) {
        logger.info("Getting all users...");
        List<UserDto> userDtoList = new ArrayList<>();
        List<UserEntity> userEntities = userRepository.findAll();
        if (limit != null) {
            for (int i = 0; i < limit; i++) {
                UserDto userDto = new UserDto();
                userDto.setUserName(userEntities.get(i).getUsername());
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
                userDto.setUserName(user.getUsername());
                userDto.setEmail(user.getEmail());
                userDto.setPass(user.getPass());
                userDtoList.add(userDto);
            }
        }
        logger.info("There are {} users in the list", userDtoList.size());
        return userDtoList;
    }

    @Override
    public Optional<UserEntity> getUserEntity(String name){
        Optional<UserEntity> user = userRepository.findById(userRepository.findByUsername(name).getUserId());
        return user;
    }

    @Override
    public Optional<UserDto> getByEmail(String email) {
        Optional<UserEntity> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()){
            UserDto userDto = new UserDto();
            userDto.setUserName(optionalUser.get().getUsername());
            userDto.setEmail(optionalUser.get().getEmail());
            userDto.setPass(optionalUser.get().getPass());
            logger.info("Was got user: {} by email {}", userDto, email);
            return Optional.of(userDto);
        }
        logger.info("Was got empty user by email={}", email);
        return Optional.empty();
    }


    @Override
    public boolean deleteUser(String email) {
        Optional<UserEntity> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            Long id = user.get().getUserId();
            userRepository.deleteById(id);
            logger.info("User with ID={} was deleted", id);
            return true;
        }
        logger.info("User with email={} was not deleted", email);
        return false;
    }

/*
    public void updateUser(UserEntity user){
        userRepository.update...
    }
*/

    public List<PhotoAlbumEntity> getPhotoAlbum(Long id){
        Optional<UserEntity> user = userRepository.findById(id);
        List<PhotoAlbumEntity> photos;
        if (user.isPresent()) {
            photos = user.get().getPhotos();
            return photos;
        }

        return null;
    }


    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {

        logger.info("Loadind user by username...");

        UserEntity user = userRepository.findByUsername(name);

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

        if (user == null){
            throw new UsernameNotFoundException("User <" + name + "> not found");
        }

        user = userRepository.getReferenceById(user.getUserId());

        for (Role role : user.getRoles()){
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));

        }

        logger.info("User {} loaded", user);

        return new User(user.getUsername(), user.getPassword(), grantedAuthorities);
    }
}
