package com.quadint.app.domain.transportation;


public class Walk extends Transportation{
    public Walk(int time) {
        super(TransportationType.WALK, time);
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public String toString() {
        return "도보로 " + super.getTime() + "분 이동";
    }
}
