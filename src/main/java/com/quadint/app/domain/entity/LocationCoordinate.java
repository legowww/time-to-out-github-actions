package com.quadint.app.domain.entity;

import com.quadint.app.web.controller.request.FavoriteLocationCoordinateRequest;
import lombok.Getter;

import javax.persistence.Embeddable;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LocationCoordinate)) return false;
        LocationCoordinate that = (LocationCoordinate) o;
        return Objects.equals(sx, that.sx) && Objects.equals(sy, that.sy) && Objects.equals(ex, that.ex) && Objects.equals(ey, that.ey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sx, sy, ex, ey);
    }
}
