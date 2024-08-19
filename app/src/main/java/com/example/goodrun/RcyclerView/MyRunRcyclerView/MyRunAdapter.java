package com.example.goodrun.RcyclerView.MyRunRcyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.goodrun.utilities.Tools;
import com.example.goodrun.R;
import com.example.goodrun.Models.dataStructures.RunItem;
import com.example.goodrun.appInterfaces.OnClick;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MyRunAdapter extends RecyclerView.Adapter<MyRunViewHolder>  {
    private Context context;
    private ArrayList<RunItem> userRuns;
    private  OnClick onClick;


    public MyRunAdapter(Context context, ArrayList<RunItem> userRuns, OnClick onClick) {
        this.context = context;
        this.userRuns = userRuns;
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public MyRunViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return  new MyRunViewHolder(LayoutInflater.from(context).inflate(R.layout.fragment_run,parent,false));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull MyRunViewHolder holder, @SuppressLint("RecyclerView") int position) {
        RunItem runItem = userRuns.get(position);

        LocalDateTime dateTimeOfTheRun = Instant.ofEpochMilli(runItem.getStopTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();//get the date of the run
        holder.stopTime.setText(dateTimeOfTheRun.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));

        holder.distance.setText("" + runItem.getDistance());
        holder.runTime.setText("" + Tools.timeMillisecondToTime_HMS(runItem.getRunTime()));
        holder.speed.setText("" + runItem.getSpeed());
        holder.onClick = onClick;
        holder.num = position;
        holder.image.setImageBitmap(Tools.base64ToBitmap(runItem.getImage()));
    }


    @Override
    public int getItemCount() {
        return userRuns.size();
    }


}
