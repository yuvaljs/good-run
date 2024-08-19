package com.example.goodrun.RcyclerView.MyColumnRcyclerView;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.goodrun.R;


public class MyColumnViewHolder extends RecyclerView.ViewHolder {
    public TextView time;
    public TextView distance;
    public TextView speed;
    public TextView km;
    public View percentOfFastest;







    public MyColumnViewHolder(@NonNull View itemView) {
        super(itemView);
        time = itemView.findViewById(R.id.time);
        distance = itemView.findViewById(R.id.distance);
        speed = itemView.findViewById(R.id.speed);
        km = itemView.findViewById(R.id.km);
        percentOfFastest = itemView.findViewById(R.id.percentOfFastest);


    }
}
