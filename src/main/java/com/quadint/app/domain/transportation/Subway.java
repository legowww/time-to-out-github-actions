package com.quadint.app.domain.transportation;

import lombok.Getter;

@Getter
public class Subway extends Transportation{
    private String startID;
    private String wayCode;
    private String startName;
    private String endName;

    public Subway(int time, String startID, String wayCode, String startName, String endName) {
        super(TransportationType.SUBWAY, time);
        this.startID = startID;
        this.wayCode = wayCode;
        this.startName = startName;
        this.endName = endName;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public String toString() {
        return wayCode + "행 " + startName + "(" + startID + ")역 에서 " + super.getTime() + "분 소요하여 " + endName + "역 까지 이동";
    }
}
