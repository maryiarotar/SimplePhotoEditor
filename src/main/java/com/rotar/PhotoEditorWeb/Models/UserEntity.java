package com.rotar.PhotoEditorWeb.Models;


import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "t_user")
@NoArgsConstructor
public class UserEntity  implements UserDetails  {

    public UserEntity(Long id, String name, String email, String pass){
        this.userId = id;
        this.userName = name;
        this.pass = pass;
        this.email = email;
        this.photos = null;
        isAccountNonExpired = true;
        isAccountNonLocked = true;
        isCredentialsNonExpired = true;
        isEnabled = true;
    }

    public UserEntity(Long id, String name, String email, String pass,
                      boolean isEx, boolean nLck, boolean cnE, boolean en
                      ){
        this.userId = id;
        this.userName = name;
        this.pass = pass;
        this.email = email;
        this.photos = null;
        this.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));
        isAccountNonExpired = isEx;
        isAccountNonLocked = nLck;
        isCredentialsNonExpired = cnE;
        isEnabled = en;
    }


    @Getter
    @Id
    @Column(name="user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY) //при остальных стратегиях надо уточнять sequence tables
    private Long userId;

    @Column(name = "user_name")
    @NonNull//ЗДЕСЬ НЕ НУЖНЫ
    private String userName;

    @NonNull
    @Column(name = "email")
    @Email
    private String email;

    @Column(name = "pass")
    @NonNull
    private String pass;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "t_user", cascade = CascadeType.ALL)
    private List<PhotoAlbumEntity> photos;
    private Long avatarId;



    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinTable(name = "role", joinColumns = @JoinColumn(name="user_id"),
                inverseJoinColumns = @JoinColumn(name="role_id"))
   private Set<Role> roles;



    @Column
    boolean isAccountNonExpired;
    boolean isAccountNonLocked;
    boolean isCredentialsNonExpired;
    boolean isEnabled;


    //---------------------------METHODS--------------------------//
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public String getPassword() {
        return pass;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    public void addPhotoToProfile(PhotoAlbumEntity photo){
        photo.setT_user(this);
        photos.add(photo);
    }


}
