package com.quadint.app.domain.entity;

import com.quadint.app.domain.UserRole;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
public class UserEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Integer id;
    @Setter @Column(nullable = false) String name;
    @Setter @Column(nullable = false) String username;
    @Setter @Column(nullable = false) String password;

    @Enumerated(EnumType.STRING) @Column(nullable = false) UserRole role = UserRole.ROLE_USER;

    protected UserEntity() {}
    private UserEntity(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }
    public static UserEntity of(String name, String username, String password) {
        return new UserEntity(name, username, password);
    }
}
