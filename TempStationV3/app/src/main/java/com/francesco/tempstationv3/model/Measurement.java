package com.francesco.tempstationv3.model;

public class Measurement {
    private final long timestamp;
    private final float value;

    public Measurement() {
        this.timestamp = 0;
        this.value = 0;
    }

    public Measurement(float value) {
        this.timestamp = (System.currentTimeMillis() / 1000) + 7200;
        this.value = value;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public float getValue() {
        return this.value;
    }
}
