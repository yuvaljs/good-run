package com.example.goodrun.RcyclerView.MyRunRcyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.goodrun.R;
import com.example.goodrun.appInterfaces.OnClick;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;


public class MyRunViewHolder extends RecyclerView.ViewHolder {
    //speed
    //stopTime
    //runTime
    //distance
    public TextView stopTime;
    public TextView speed;
    public TextView runTime ;
    public TextView distance;
    public Button button;
    public OnClick onClick;
    public ImageView image;
    public int num;
//    public MapView map;
//    public GoogleMap myGoogleMap = null;

    //map
public int index;


    public MyRunViewHolder(@NonNull View itemView) {
        super(itemView);
        stopTime = itemView.findViewById(R.id.stopTime);
        speed = itemView.findViewById(R.id.speed);
        runTime = itemView.findViewById(R.id.runTime);
        distance = itemView.findViewById(R.id.distance);
        image = itemView.findViewById(R.id.image);
//        map = itemView.findViewById(R.id.map);
        //mapView.onCreate(savedInstanceState);
        //        mapView.getMapAsync(this);
        button = itemView.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.click(num);
            }
        });

    }


}
