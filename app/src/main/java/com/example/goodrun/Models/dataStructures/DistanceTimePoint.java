package com.example.goodrun.Models.dataStructures;

public class DistanceTimePoint {
    private double distance;
    private long time;

    public DistanceTimePoint(double distance, long time) {
        this.distance = distance;
        this.time = time;
    }

    public double getDistance() {
        return distance;
    }

    public long getTime() {
        return time;
    }
}
