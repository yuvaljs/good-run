package com.example.goodrun.Models;

import android.graphics.Bitmap;
import android.location.Location;
import android.util.Log;

import com.example.goodrun.Models.dataStructures.KmItem;
import com.example.goodrun.Models.dataStructures.RunItem;
import com.example.goodrun.Models.dataStructures.DistanceTimePoint;
import com.example.goodrun.Models.dataStructures.LocationTimePoint;
import com.example.goodrun.utilities.Timer;
import com.example.goodrun.utilities.Tools;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;


public class Run  {
    private final  long  HOUR = 1000*60*60;
    public enum RunStatus {neverStart, start, onPause, finish  }

    private RunStatus runStatus = RunStatus.neverStart;
    private ArrayList<LocationTimePoint> runningTrack ;//Locally only
    private ArrayList<LatLng> line;
    private ArrayList<ArrayList<LatLng>>lines;
    private DistanceTimePoint last_50_distanceTrack = new DistanceTimePoint(0,0);
    private ArrayList<Long>time_for_kilometers ;
    private DistanceTimePoint lastKilometer = new DistanceTimePoint(0,0);



   private Timer timer;


    private double Speed_kmh = 0;
    private double averageSpeed_kmh = 0.0;

    private double distance_m = 0;

    private RunItem runItem = null;



    public Run()
    {
        runningTrack = new ArrayList<LocationTimePoint>();
        lines = new ArrayList<ArrayList<LatLng>>();
        time_for_kilometers = new ArrayList<Long>();
        timer = new Timer();

    }



    public boolean start(Location location)
    {
        if( runStatus != RunStatus.finish && runStatus != RunStatus.start )
        {
            timer.start();
            runStatus = RunStatus.start;
            line = new ArrayList<LatLng>();
            update(location);
            return true;

        }

        return false;

    }

    public boolean pause() {
        if (runStatus == RunStatus.start) {
            timer.pause();
            updateSpeed(timer.getTime());
            runStatus = RunStatus.onPause;
            lines.add(line);
            return true;
        }


        return false;

    }

    public boolean stop(Bitmap image)
    {
        if(runStatus == RunStatus.start)
            pause();

        if (runStatus == RunStatus.onPause)
        {
            timer.stop();
            long time = timer.getTime();
            runStatus = RunStatus.finish;
            time_for_kilometers.add(time-lastKilometer.getTime());
            saveRunToRunItem(image,time);
            return true;

        }
        return false;
    }



    public void update(Location location)
    {
        if (  runStatus == RunStatus.start)
        {
           long time = timer.getTime();
            line.add(new LatLng(location.getLatitude(),location.getLongitude()));
            //if(location.getTime() > startTimeMillisecond )
            if (runningTrack.size()>0)
            {
                distance_m += haversine(runningTrack.get(runningTrack.size()-1).getLocation(), location) ;
            }

            runningTrack.add(new LocationTimePoint(location, time ) );
            updateSpeed(time);

        }

    }
    public long getTime()
    {
        return timer.getTime();
    }

    private void updateSpeed(long time) {
        averageSpeed_kmh = gatSpeed_kmh(distance_m , time);//update average speed
        if (distance_m<50)
        {
            Speed_kmh = 0;
            averageSpeed_kmh = 0;
        }

        if (distance_m-50> last_50_distanceTrack.getDistance())//update speed
        {

            Speed_kmh = gatSpeed_kmh(distance_m-last_50_distanceTrack.getDistance(),time - last_50_distanceTrack.getTime());
            last_50_distanceTrack = new DistanceTimePoint(distance_m,time);
        }
        if( distance_m -1000>lastKilometer.getDistance())//update time_for_kilometers
        {
            time_for_kilometers.add(time-lastKilometer.getTime());
            lastKilometer = new DistanceTimePoint(distance_m, time);
        }
    }
    private double gatSpeed_kmh(double distance_m, long time)
    {
        return (distance_m * 3600)/((double)(time)) ;
    }




    private double haversine (Location location1,Location location2) {
        double lat1 = location1.getLatitude(),  lat2 =location2.getLatitude(),  lon1 =location1.getLongitude(),
         lon2 =location2.getLongitude(),  el1 = location1.getAltitude(), el2 =location2.getAltitude();
        final int R = 6371; // Radius of the earth
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }

    private void saveRunToRunItem(Bitmap image, long time) {



        ArrayList<ArrayList<LatLng>> lines;
        float speed;
        long stopTime;
        long runTime;
        float distance;
        ArrayList<KmItem> kmItems;


        lines = this.lines;
        stopTime = timer.getGlobalStopTime();
        runTime = time;
        kmItems  = getKmItemsList(time);

        if (distance_m<20)
        {
            speed = 0.0f;
            distance = 0.0f;
        }else{

            speed = Tools.getCleanNumber(averageSpeed_kmh);
            distance = Tools.getCleanNumber(this.distance_m/1000.0);
        }


        runItem = new RunItem(lines, speed, stopTime, runTime, distance, kmItems, Tools.bitmapToBase64(image) );

    }

    private ArrayList<KmItem> getKmItemsList(long runTime) {

        ArrayList<KmItem> kmItems =  new ArrayList<KmItem>();
        if(distance_m<20)
        {

            kmItems.add(new KmItem(runTime,0.0f,0.0f,0.0f,1.0f) );
            return kmItems;
        }


        long time ;
        float distance;
        float speed ;
        float km  ;
        float percentOfFastest ;
        float maxSpeed =  0.0f;

        for ( int i = 0;i <  this.time_for_kilometers.size();i++ )
        {

            time = this.time_for_kilometers.get(i);
            distance = this.time_for_kilometers.size()> i+1 ? 1.0f : Tools.getCleanNumber((this.distance_m % 1000.0) / 1000.0);
            speed = Tools.getCleanNumber(distance/(time/(double)HOUR));
            Log.d("speed = "+ speed,"speed = "+ speed);
            Log.d("speed = "+ speed,"speed = "+ speed);
            Log.d("speed = "+ speed,"speed = "+ speed);
            Log.d("speed = "+ speed,"speed = "+ speed);
            Log.d("speed = "+ speed,"speed = "+ speed);
            Log.d("speed = "+ speed,"speed = "+ speed);
            Log.d("speed = "+ speed,"speed = "+ speed);
            Log.d("speed = "+ speed,"speed = "+ speed);
            Log.d("speed = "+ speed,"speed = "+ speed);
            Log.d("speed = "+ speed,"speed = "+ speed);
            Log.d("speed = "+ speed,"speed = "+ speed);

            km  = this.time_for_kilometers.size()> i+1 ? (float)(i+1): Tools.getCleanNumber(this.distance_m/1000.0);
            percentOfFastest  = 1.0f;
            kmItems.add(new KmItem(time, distance, speed, km, percentOfFastest));

            if (speed > maxSpeed)
                maxSpeed = speed;


        }

        for(int i = 0;i<kmItems.size();i++ )
        {
            if (maxSpeed>0)
            {
                kmItems.get(i).setPercentOfFastest( kmItems.get(i).getSpeed()/maxSpeed);

            }
        }

        return kmItems;

    }
    public double getAverageSpeed_kmh() {
        return averageSpeed_kmh;
    }

    public double getDistance_m() {
        return distance_m;
    }



//    public long getStopTimeMillisecond() {
//        return timer.getGlobalStopTime();
//    }

    public ArrayList<LatLng> getLine() {
        return line;
    }
    public RunStatus getRunStatus() {
        return runStatus;
    }

    public double getSpeed_kmh() {
        return Speed_kmh;
    }

    public RunItem getRunSave() {
        return runItem;
    }
}
