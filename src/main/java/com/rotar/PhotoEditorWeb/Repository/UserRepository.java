package com.rotar.PhotoEditorWeb.Repository;


import com.rotar.PhotoEditorWeb.Models.PhotoAlbumEntity;
import com.rotar.PhotoEditorWeb.Models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query ("SELECT new UserEntity" +
            "(ue.userId, ue.userName, ue.email, ue.pass)" +
            " FROM UserEntity ue WHERE email=?1")
    Optional<UserEntity> findByEmail(String email);

    @Query ("SELECT new UserEntity" +
            "(ue.userId, ue.userName, ue.email, ue.pass, ue.isAccountNonExpired, " +
            "ue.isAccountNonLocked, ue.isCredentialsNonExpired, ue.isEnabled)" +
            " FROM UserEntity ue WHERE userName=?1")
    UserEntity findByUsername(String userName);

//    @Query("update UserEntity set photos = ?2")
//    void updateById(Long id, PhotoAlbumEntity photos);
//

}
