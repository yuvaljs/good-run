package com.example.goodrun.RcyclerView.MyColumnRcyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.goodrun.Models.dataStructures.KmItem;
import com.example.goodrun.R;
import com.example.goodrun.utilities.Tools;

import java.util.List;

public class MyColumnAdapter extends RecyclerView.Adapter<MyColumnViewHolder> {
   private Context context;
    private  List<KmItem> items;


    public MyColumnAdapter(Context context, List<KmItem> items) {
        this.context = context;
        this.items = items;


    }

    @NonNull
    @Override
    public MyColumnViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return  new MyColumnViewHolder(LayoutInflater.from(context).inflate(R.layout.column_fragment,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyColumnViewHolder holder, int position) {
        KmItem kmItem = items.get(position);

        holder.speed.setText(""+kmItem.getSpeed());
        holder.km.setText(""+kmItem.getKm());
        holder.time.setText(""+ Tools.timeMillisecondToTime_MS(kmItem.getTime()));
        holder.distance.setText(""+kmItem.getDistance());
        holder.percentOfFastest.setScaleX(kmItem.getPercentOfFastest());

//        KmItem columnItem = items.get(position);
//        RunItem runItem = columnItem.getRunItem();
//        List<Long> time_for_kilometers = runItem.getTime_for_kilometers();
//        int kmIndex = columnItem.getKmIndex();
//        long time = time_for_kilometers.get(kmIndex);
//        String timeStr, distanceStr ,pace_textStr, kmStr;
//
//        if (kmIndex == time_for_kilometers.size() - 1) {
//            distanceStr = ""+ runItem.getDistance_m()%1000/10d/100.0f;
//
//            kmStr = ""+ runItem.getDistance_m()/10d/100.0f;
//            pace_textStr = ""+Tools.getSpeed_kmh(time,runItem.getDistance_m()%1000)*100/100d;
//        } else {
//            distanceStr = "1.0";
//            kmStr = ""+kmIndex+1+".0";
//            pace_textStr = "" + Tools.getSpeed_kmh(time,1000.0)*100/100d;
//        }


//        timeStr =Tools.timeMillisecondToTime_MS(time);
//        holder.distance.setText("distanceStr");
//       // holder.distance.setText(distanceStr);
//        holder.km.setText(kmStr);
//        holder.pace_text.setText(pace_textStr);
//        holder.time.setText(timeStr);
//        holder.pace_column.setScaleX((1/(float)time)/(1/(float)bestKm ));
//        LocalDateTime dateTimeOfTheRun =Instant.ofEpochMilli(items.get(position).getStopTimeMillisecond()).atZone(ZoneId.systemDefault()).toLocalDateTime();
//
//
//
//        holder.run_fragment_date_textview.setText(dateTimeOfTheRun.format( DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
//        holder.run_fragment_distance_numbers_Relative_Layout.setText(""+items.get(position).getDistance_m()/100d/10.0f);
//        holder.run_fragment_duration_numbers_Relative_Layout.setText(Tools.timeMillisecondToTime(items.get(position).getRunTimeMillisecond()));
//        holder.run_fragment_pace_numbers_Relative_Layout.setText(Tools.km_HToM_kmStr(items.get(position).getAverageSpeed_kmh()));

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
