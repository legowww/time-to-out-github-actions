package com.quadint.app.domain.transportation;

public class Subway extends Transportation{
    public Subway(TransportationType transportationType, int time) {
        super(transportationType, time);
    }

    @Override
    public String getId() {
        return null;
    }
}
