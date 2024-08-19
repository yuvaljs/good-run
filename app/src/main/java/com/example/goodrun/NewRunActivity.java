package com.example.goodrun;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.goodrun.utilities.LocationUpdates;
import com.example.goodrun.utilities.Tools;
import com.example.goodrun.Models.dataStructures.RunItem;
import com.example.goodrun.Models.Run;
import com.example.goodrun.Models.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.concurrent.locks.ReentrantLock;


public class NewRunActivity extends AppCompatActivity /*implements OnMapReadyCallback*/ {


    private GoogleMap myGoogleMap;

    private ImageButton start_button, pause_button, stop_button;
    private Run run ;
    SupportMapFragment mapFragment;
    private Location myLocation;
    private Polyline polyline = null;
    private float iconSize;//50dp == screen width / 8
    private TextView distance, speed, averageSpeed,time;
    private User user;
    private Handler handler;
    private Runnable updateTextViewRunnable = new Runnable() {
        @Override
        public void run() {
            // Update the TextView with the current counter value
           updateUIDate();

            // Post the Runnable again with a delay of 1 second (1000 milliseconds)
            handler.postDelayed(this, 1000);

        }
    };



    private final ReentrantLock lock = new ReentrantLock();





    private OnMapReadyCallback onMapReady = new OnMapReadyCallback() {
        @SuppressLint("MissingPermission")
        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            getPermission();


            myGoogleMap = googleMap;
            myGoogleMap.setMyLocationEnabled(true);

            if (myLocation != null)
                myGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(myLocation.getLatitude(), myLocation.getLongitude()), 15, 0, 0)));





        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_run);




        findViews();
        initViews();



        }
    private void findViews() {
        start_button = findViewById(R.id.start_button);
         pause_button = findViewById(R.id.pause_button);
         stop_button = findViewById(R.id.stop_button);

        distance = findViewById(R.id.distance);
        speed = findViewById(R.id.speed);
        averageSpeed = findViewById(R.id.averageSpeed);
        time = findViewById(R.id.time);

    }
    private void initViews() {

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);//show the user where he run
        mapFragment.getMapAsync(onMapReady);


        run = new Run();//Creates a new run


        Point screenSize = getScreenSize();//got screenSize for UI
        iconSize = screenSize.x / 8.0f;
        updateUI();

        //init the activity button
        start_button.setOnClickListener(v -> start());
        pause_button.setOnClickListener(v -> pause());
        stop_button.setOnClickListener(v -> stop());


        user = Tools.getUser();//get app user


        //update UI and data on location changed
        LocationUpdates gatLocationUpdates = new LocationUpdates(this, new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                update(location);
            }
        });


        myLocation = gatLocationUpdates.getLocation(this);//get location to be able to start the run.

        handler = new Handler();
    }

    public void update( Location location) {//update UI and data


        run.update(location);
        if(run.getRunStatus() == Run.RunStatus.start)
        {

            if (run.getLine().size()>2)
            {
                polyline.setPoints(run.getLine());


            }
            else if (run.getLine().size()==2)
            {
                polyline = myGoogleMap.addPolyline(new PolylineOptions());
                polyline.setWidth(30);
            }
        }


        myLocation = location;
        updateUIDate();
    }



    private void start() {//start the run
        if (run.start(myLocation))
        {
            startRepeatingTask();

            updateUI();

        }


    }
    private void startRepeatingTask() {
        // Start the Runnable immediately
        updateTextViewRunnable.run();
    }

    private void stopRepeatingTask() {
        // Remove the Runnable to stop the updates
        handler.removeCallbacks(updateTextViewRunnable);
    }
    private void pause() {//pause the run
        if(run.pause())
        {
            stopRepeatingTask();
            updateUI();
        }


    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Make sure to stop the Runnable when the activity is destroyed
        stopRepeatingTask();
    }

    private void stop() {//stop the run
        myGoogleMap.snapshot(new GoogleMap.SnapshotReadyCallback() {
            @Override
            public void onSnapshotReady(@Nullable Bitmap bitmap) {
                if(run.stop(bitmap))
                {
                    updateUI();
                    saveRun();

                }
            }
        });



    }

    private void saveRun() {//save the run then go to RunActivity to show the run

        RunItem runItem = run.getRunSave();

            user.addRun(runItem);



            Tools.saveUser(user,true);



        Intent intent = new Intent(NewRunActivity.this, RunActivity.class);
        intent.putExtra("run index",user.getUserRuns().indexOf(runItem));
        startActivity(intent);
        finish();



    }

    private void updateUI() {// update UI in accordance with RunStatus
        switch (run.getRunStatus()) {
            case neverStart:
                start_button.setVisibility(View.VISIBLE);
                pause_button.setVisibility(View.GONE);
                stop_button.setVisibility(View.GONE);

                start_button.setX(3.5f *iconSize);
                break;

            case start:
                start_button.setVisibility(View.GONE);
                pause_button.setVisibility(View.VISIBLE);
                stop_button.setVisibility(View.VISIBLE);

                pause_button.setX(2 *iconSize);
                stop_button.setX(5 *iconSize);
                break;

            case onPause:
                start_button.setVisibility(View.VISIBLE);
                pause_button.setVisibility(View.GONE);
                stop_button.setVisibility(View.VISIBLE);

                start_button.setX(2 *iconSize);
                stop_button.setX(5 *iconSize);
                break;

            case finish:
                start_button.setVisibility(View.GONE);
                pause_button.setVisibility(View.GONE);
                stop_button.setVisibility(View.GONE);
                break;
        }

        updateUIDate();
    }

    private void updateUIDate()
    {
        lock.lock();
        Log.d(" handler.postDelayed(this, 1000);"," handler.postDelayed(this, 1000);");
        distance.setText(""+Tools.getCleanNumber(run.getDistance_m()/1000.0));
        speed.setText(""+Tools.getCleanNumber(run.getSpeed_kmh()));
        averageSpeed.setText(""+Tools.getCleanNumber(run.getAverageSpeed_kmh()));
        time.setText(Tools.timeMillisecondToTime_MS(run.getTime()));
        lock.unlock();
    }


    private void getPermission()// get permission to use GPS
    {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }


    private Point getScreenSize()// return screen size
    {
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        return size;
    }


}