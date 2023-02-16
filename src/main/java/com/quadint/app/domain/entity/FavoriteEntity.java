package com.quadint.app.domain.entity;


import com.quadint.app.domain.LocationCoordinate;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
public class FavoriteEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Integer id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "user_id") private UserEntity userEntity;
    @Embedded
    LocationCoordinate locationCoordinate;
    @Column @Setter private String name;

    protected FavoriteEntity() {}

    private FavoriteEntity(UserEntity userEntity, String name, LocationCoordinate locationCoordinate) {
        this.userEntity = userEntity;
        this.name = name;
        this.locationCoordinate = locationCoordinate;
    }

    public static FavoriteEntity of(UserEntity userEntity, String name, LocationCoordinate locationCoordinate) {
        return new FavoriteEntity(userEntity, name, locationCoordinate);
    }
}
