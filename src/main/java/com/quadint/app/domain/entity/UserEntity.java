package com.quadint.app.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
public class UserEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Integer id;
    @Setter @Column(nullable = false) String username;
    @Setter @Column(nullable = false) String password;

    protected UserEntity() {}
    private UserEntity(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public static UserEntity of(String username, String password) {
        return new UserEntity(username, password);
    }
}
