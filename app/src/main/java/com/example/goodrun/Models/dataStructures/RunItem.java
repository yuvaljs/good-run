package com.example.goodrun.Models.dataStructures;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class RunItem {
    private ArrayList<ArrayList<LatLng>> Lines = null;
    private float speed = 0.0f;//km/h  two zeros after the decimal point
    private long stopTime = 0;//Millisecond
    private long runTime = 0;//Millisecond
    private float distance = 0;//km  two zeros after the decimal point
    private ArrayList<KmItem> kmItems = null;
    private String image;


    public RunItem() {
    }

    public RunItem(ArrayList<ArrayList<LatLng>> lines, float speed, long stopTime, long runTime, float distance, ArrayList<KmItem> kmItems, String image) {
        Lines = lines;
        this.speed = speed;
        this.stopTime = stopTime;
        this.runTime = runTime;
        this.distance = distance;
        this.kmItems = kmItems;
        this.image = image;
    }

    public ArrayList<ArrayList<LatLng>> getLines() {
        return Lines;
    }

    public RunItem setLines(ArrayList<ArrayList<LatLng>> lines) {
        Lines = lines;
        return this;
    }

    public float getSpeed() {
        return speed;
    }

    public RunItem setSpeed(float speed) {
        this.speed = speed;
        return this;
    }

    public long getStopTime() {
        return stopTime;
    }

    public RunItem setStopTime(long stopTime) {
        this.stopTime = stopTime;
        return this;
    }

    public long getRunTime() {
        return runTime;
    }

    public RunItem setRunTime(long runTime) {
        this.runTime = runTime;
        return this;
    }

    public float getDistance() {
        return distance;
    }

    public RunItem setDistance(float distance) {
        this.distance = distance;
        return this;
    }

    public ArrayList<KmItem> getKmItems() {
        return kmItems;
    }

    public RunItem setKmItems(ArrayList<KmItem> kmItems) {
        this.kmItems = kmItems;
        return this;
    }

    public String getImage() {
        return image;
    }

    public RunItem setImage(String image) {
        this.image = image;
        return this;
    }
}