package com.quadint.app.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
public class TokenEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Integer id;

    @Column(nullable = false) private Integer userId;
    @Column(nullable = false) @Setter private String token;

    protected TokenEntity() {}

    public TokenEntity(Integer userId, String token) {
        this.userId = userId;
        this.token = token;
    }
}
