package com.quadint.app.domain.time;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;

@Getter @Setter
@NoArgsConstructor
public class SubwayTimeDto {
    private String idx;

    private String[][] list;

    public SubwayTimeDto(String idx, String[][] list) {
        this.idx = idx;
        this.list = list;
    }

    @Override
    public String toString() {
        return "SubwayTimeDto{" +
                "idx='" + idx + '\'' +
                ", list=" + Arrays.toString(list) +
                '}';
    }
}
