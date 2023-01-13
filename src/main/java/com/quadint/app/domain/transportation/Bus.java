package com.quadint.app.domain.transportation;

import lombok.Getter;

@Getter
public class Bus extends Transportation{
    private String routeId;
    private String busNum;
    private String startLocalStationID;
    private String startName;
    private String endLocalStationID;
    private String endName;

    public Bus(int time, String routeId, String busNum, String startLocalStationID, String startName, String endLocalStationID, String endName) {
        super(TransportationType.BUS, time);
        this.routeId = routeId;
        this.busNum = busNum;
        this.startLocalStationID = startLocalStationID;
        this.startName = startName;
        this.endLocalStationID = endLocalStationID;
        this.endName = endName;
    }

    //todo: 지하철 식별자 반환 개수 확인하고 부모클래스에 abstract 메서드 사용
    @Override
    public String getId() {
        return getRouteId();
    }

    @Override
    public String toString() {
        return busNum + "(" + routeId + ")" + "번 버스로 " + super.getTime() + "분 소요하여 " + startName + "(" + startLocalStationID + ")정류장 에서 " + endName + "(" + endLocalStationID + ")정류장 까지 이동";
    }
}
