package com.quadint.app.domain;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 나갈 시간
 */
@Getter
public class Time {
    private LocalDateTime time;

    private Time(LocalDateTime outTime) {
        this.time = outTime;
    }

    public static Time createTime(LocalDateTime time) {
        return new Time(time);
    }

    public static LocalDateTime calculateTime(LocalDateTime time, int arrivalTimeSec) {
        return time.plusSeconds(arrivalTimeSec);
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return "Time{" +
                "time=" + time.format(formatter) +
                '}';
    }
}
