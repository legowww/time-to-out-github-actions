package com.quadint.app.web.controller.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LocationCoordinateRequest {
    private String sx;
    private String sy;
    private String ex;
    private String ey;

    public LocationCoordinateRequest(String sx, String sy, String ex, String ey) {
        this.sx = sx;
        this.sy = sy;
        this.ex = ex;
        this.ey = ey;
    }

    @Override
    public String toString() {
        return "LocationCoordinateRequest{" +
                "SX='" + sx + '\'' +
                ", SY='" + sy + '\'' +
                ", EX='" + ex + '\'' +
                ", EY='" + ey + '\'' +
                '}';
    }
}
