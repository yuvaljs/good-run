package com.example.goodrun;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.goodrun.utilities.Tools;
import com.example.goodrun.RcyclerView.MyColumnRcyclerView.MyColumnAdapter;
import com.example.goodrun.Models.dataStructures.RunItem;
import com.example.goodrun.Models.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class RunActivity extends AppCompatActivity {
    private TextView run_distance_numbers_Relative_Layout, run_duration_numbers_Relative_Layout, run_date_textview, run_pace_numbers_Relative_Layout;
    private RecyclerView my_runs_recycler_view;
    private Button app_navigation_bar_my_runs_Button, app_navigation_bar_new_run_Button, app_navigation_bar_profile_Button;
    private AppCompatButton delete;
    private RunItem runItem;
    User user;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);
        findViews();
        initViews();
    }

    private void findViews() {
        run_distance_numbers_Relative_Layout = findViewById(R.id.run_distance_numbers_Relative_Layout);
        run_duration_numbers_Relative_Layout = findViewById(R.id.run_duration_numbers_Relative_Layout);
        run_date_textview = findViewById(R.id.run_date_textview);
        run_pace_numbers_Relative_Layout = findViewById(R.id.run_pace_numbers_Relative_Layout);
        my_runs_recycler_view = findViewById(R.id.my_runs_recycler_view);
        app_navigation_bar_my_runs_Button = findViewById(R.id.app_navigation_bar_my_runs_Button);
        app_navigation_bar_new_run_Button = findViewById(R.id.app_navigation_bar_new_run_Button);
        app_navigation_bar_profile_Button = findViewById(R.id.app_navigation_bar_profile_Button);
        delete = findViewById(R.id.delete);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initViews() {
        user = Tools.getUser();


        initRunUI();
        initNavigationBar();
        initMap();
        initDeleteButtun();

    }

    private void initDeleteButtun() {
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.deleteRun(runItem);
                Tools.saveUser(user, false);
                Intent intent = new Intent(RunActivity.this, MyRunsActivity.class);
                intent.putExtra("save user to database", true);
                startActivity(intent);
                finish();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initRunUI() {
        Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues().create();
        runItem = user.getUserRuns().get(getIntent().getIntExtra("run index", 0));
        LocalDateTime dateTimeOfTheRun = Instant.ofEpochMilli(runItem.getStopTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();


        if (runItem.getKmItems() != null) {
            my_runs_recycler_view.setLayoutManager(new LinearLayoutManager(this));// Demonstrate to the user the different speeds of the run
            my_runs_recycler_view.setAdapter(new MyColumnAdapter(getApplicationContext(), runItem.getKmItems()));
        }


        run_distance_numbers_Relative_Layout.setText("" + Tools.getCleanNumber(runItem.getDistance()));
        run_duration_numbers_Relative_Layout.setText(Tools.timeMillisecondToTime_HMS(runItem.getRunTime()));
        run_date_textview.setText("date   " + dateTimeOfTheRun.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        run_pace_numbers_Relative_Layout.setText("" + runItem.getSpeed());

    }

    private void initMap() {

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);//show the user where he run
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {


                googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(runItem.getLines().get(0).get(0), 15, 0, 0)));

                for (ArrayList<LatLng> line : runItem.getLines()) {
                    Polyline polyline = googleMap.addPolyline(new PolylineOptions());
                    polyline.setWidth(30);
                    polyline.setPoints(line);
                }


            }
        });

    }

    private void initNavigationBar() {
        app_navigation_bar_my_runs_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RunActivity.this, MyRunsActivity.class);
                startActivity(intent);
                finish();
            }
        });
        app_navigation_bar_new_run_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RunActivity.this, NewRunActivity.class);
                startActivity(intent);
                finish();
            }
        });
        app_navigation_bar_profile_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RunActivity.this, ProfileActivity.class);
                intent.putExtra("userName", user.getUserName());

                startActivity(intent);
                finish();
            }
        });
    }


}