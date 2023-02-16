package com.quadint.app.domain.route;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TimeRoute implements Comparable<TimeRoute>{
    @JsonFormat(pattern = "HH:mm") //JSON 값 반환할 때 값을 설정한 포맷 형식의 String 반환
    private LocalDateTime time;
    private Route route;

    private TimeRoute(LocalDateTime time, Route route) {
        this.time = time;
        this.route = route;
    }

    public static TimeRoute createTimeRoute(LocalDateTime time, Route route) {
        return new TimeRoute(time, route);
    }

    @Override
    public int compareTo(TimeRoute o) {
        return this.time.compareTo(o.time);
    }

    @Override
    public String toString() {
        return "TimeRoute{" +
                "time=" + time +
                ", route=" + route +
                '}';
    }
}
