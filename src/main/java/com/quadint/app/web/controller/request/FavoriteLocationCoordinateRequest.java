package com.quadint.app.web.controller.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FavoriteLocationCoordinateRequest {
    private String name;
    private String sx;
    private String sy;
    private String ex;
    private String ey;
}
