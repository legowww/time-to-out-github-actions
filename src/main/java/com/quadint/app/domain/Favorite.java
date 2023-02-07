package com.quadint.app.domain;

import com.quadint.app.domain.entity.FavoriteEntity;
import com.quadint.app.domain.entity.LocationCoordinate;
import lombok.Getter;

@Getter
public class Favorite {
    private Integer id;
    private String name;
    private LocationCoordinate lc;

    public Favorite(FavoriteEntity entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.lc = entity.getLocationCoordinate();
    }
}
