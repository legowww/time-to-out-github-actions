package com.quadint.app.domain.time;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SubwayTimeDto {
    private String idx;
    private String[] list;

    public SubwayTimeDto(String idx, String[] list) {
        this.idx = idx;
        this.list = list;
    }
}