package com.example.goodrun.Models.dataStructures;

public class KmItem {

    private long  time ;
    private float distance; //km  two zeros after the decimal point
    private float speed;//km/h  two zeros after the decimal point
    private float km;//km  two zeros after the decimal point
    private float percentOfFastest = 1.0f;


    public KmItem() {
    }

    public KmItem(long time, float distance, float speed, float km, float percentOfFastest) {
        this.time = time;
        this.distance = distance;
        this.speed = speed;
        this.km = km;
        this.percentOfFastest = percentOfFastest;
    }

    public long getTime() {
        return time;
    }

    public KmItem setTime(long time) {
        this.time = time;
        return this;
    }

    public float getDistance() {
        return distance;
    }

    public KmItem setDistance(float distance) {
        this.distance = distance;
        return this;
    }

    public float getSpeed() {
        return speed;
    }

    public KmItem setSpeed(float speed) {
        this.speed = speed;
        return this;
    }

    public float getKm() {
        return km;
    }

    public KmItem setKm(float km) {
        this.km = km;
        return this;
    }

    public float getPercentOfFastest() {
        return percentOfFastest;
    }

    public KmItem setPercentOfFastest(float percentOfFastest) {
        this.percentOfFastest = percentOfFastest;
        return this;
    }
}
