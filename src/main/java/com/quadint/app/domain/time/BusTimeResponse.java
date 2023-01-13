package com.quadint.app.domain.time;

import com.quadint.app.domain.Time;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * SUBWAY 클래스 추가 후
 * 상속 관계로 리팩토링 예정
 *              TimeResponse
 *      BusTimeResponse  SubwayTimeResponse
 */
public class BusTimeResponse {
    @Getter
    private String routeId;
    @Getter
    private List<Time> times = new ArrayList<>();

    private BusTimeResponse(String routeId) {
        this.routeId = routeId;
    }

    public static BusTimeResponse createBusTimeResponse(String routeId) {
        return new BusTimeResponse(routeId);
    }

    public int getTimeSize() {
        return times.size();
    }

    public Time getTime(int index) {
        return times.get(index);
    }

    public void addTime(Time time) {
        times.add(time);
    }

    @Override
    public String toString() {
        return "BusTimeResponse{" +
                "routeId='" + routeId + '\'' +
                ", times=" + times +
                '}';
    }
}
