package com.rotar.PhotoEditorWeb.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Set;

@NoArgsConstructor
@Entity
@Table(name = "t_role")
public class Role implements GrantedAuthority {

    @Getter
    @Setter
    @Id
    private Long id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    @Transient
    @ManyToMany(mappedBy = "roles")
    private Set<UserEntity> users;



    public Role(Long id) {
        this.id = id;
    }

    public Role(long id, String role) {
        this.id = id;
        this.name = role;
    }



    @Override
    public String getAuthority() {
        return getName();
    }
}
