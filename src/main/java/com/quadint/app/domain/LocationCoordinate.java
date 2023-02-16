package com.quadint.app.domain;

import com.quadint.app.web.controller.request.FavoriteLocationCoordinateRequest;
import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class LocationCoordinate {
    private String sx;
    private String sy;
    private String ex;
    private String ey;

    protected LocationCoordinate() {}

    private LocationCoordinate(String sx, String sy, String ex, String ey) {
        this.sx = sx;
        this.sy = sy;
        this.ex = ex;
        this.ey = ey;
    }

    public static LocationCoordinate fromRequest(FavoriteLocationCoordinateRequest request) {
        return new LocationCoordinate(request.getSx(), request.getSy(), request.getEx(), request.getEy());
    }
}
