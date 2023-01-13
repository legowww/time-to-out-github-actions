package com.quadint.app.domain.transportation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

@Getter
public abstract class Transportation {
    private TransportationType transportationType;
    private int time;

    public Transportation(TransportationType transportationType, int time) {
        this.transportationType = transportationType;
        this.time = time;
    }

    @JsonIgnore
    public abstract String getId();
}
