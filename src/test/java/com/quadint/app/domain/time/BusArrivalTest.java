package com.quadint.app.domain.time;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class BusArrivalTest {

    @Test
    public void localDateTimeToStr() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        String result = now.format(formatter);
        System.out.println("result = " + result);
    }


    @Test
    public void strToLocalDateTime() throws Exception {
        String str = "2022-11-24T17:26:32.0277346";

        LocalDateTime now = LocalDateTime.parse(str);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String result = now.format(formatter);
        System.out.println("result = " + result);
    }
}