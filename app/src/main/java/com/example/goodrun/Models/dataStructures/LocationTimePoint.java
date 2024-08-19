package com.example.goodrun.Models.dataStructures;

import android.location.Location;

public class LocationTimePoint {
   private long timeMillisecond;
   private Location location;


    public LocationTimePoint(Location location, long timeMillisecond) {
        this.location = location;
        this.timeMillisecond = this.timeMillisecond;
    }


    public long getTimeMillisecond() {
        return timeMillisecond;
    }

    public Location getLocation() {
        return location;
    }
}
