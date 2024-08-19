package com.example.goodrun.utilities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;

public class LocationUpdates {

    private Activity activity;
    private  LocationManager locationManager;
    private FusedLocationProviderClient fusedLocationProviderClient;

    public LocationUpdates(Activity activity, LocationListener locationListener)
    {
        this.activity = activity;
        if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,4,locationListener);



    }
    @SuppressLint("MissingPermission")
    public Location getLocation(Context context)
    {
        return   locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }








}
