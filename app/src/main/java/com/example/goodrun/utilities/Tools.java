package com.example.goodrun.utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.goodrun.Models.dataStructures.RunItem;
import com.example.goodrun.Models.User;
import com.example.goodrun.appInterfaces.OnUserReady;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class Tools {

    public static boolean checksUserNameExists(String username, List<String> usernames) {
        for (String str: usernames)
        {
            if (str.equals(username))
                return true;
        }
        return false;

    }



    public static String timeMillisecondToTime_HMS(long TimeMillisecond)
    {
        String str ;

        long  hour,  minute  , second;
        String strHour, strMinute, strSecond;

        hour = TimeMillisecond / (1000 *60*60);
        minute = TimeMillisecond % (1000 *60*60) /(1000 *60) ;
        second = TimeMillisecond %(1000 *60)/ 1000;

        strHour = hour>9 ? ""+hour: " "+hour;
        strMinute = minute>9 ? ""+minute: "0"+minute;
        strSecond = second>9 ? ""+second: "0"+second;

        str = ""+ strHour+":"+strMinute+":"+strSecond;

        return str;
    }
    public static String timeMillisecondToTime_MS(long TimeMillisecond)
    {
        String str ;

        long    minute  , second;
        String  strMinute, strSecond;

        minute = TimeMillisecond /(1000 *60) ;
        second = TimeMillisecond %(1000 *60)/ 1000;

        strMinute = minute>9 ? ""+minute: "0"+minute;
        strSecond = second>9 ? ""+second: "0"+second;

        str = ""+strMinute+":"+strSecond;

        return str;
    }



    public static String km_HToM_kmStr(double km_H)//Converts between kilometer to hour and minutes to kilometer and then produces a string that displays the result
    {
        double M_km  = 60/km_H;
        String strMinute = M_km>9 ? ""+(int)M_km : "0"+(int)M_km;
        int second = (int)(M_km * 60 % 60);
        String strSecond =  second > 9 ? "" +second : "0"+second;

        return "" + strMinute+ ":"+  strSecond;
    }

    public static float getSpeed_kmh(long timeMillisecond, double distance_m)//return float which represents speed in km_h
    {
        try {
            double mh = distance_m * (60 *60*1000/timeMillisecond);
            return getCleanNumber(mh/1000.0);
        }
        catch (ArithmeticException arithmeticException)
        {
            return 0.0f;
        }
    }


    public static void saveUser(User user, DatabaseReference rootDatabaseReference, boolean saveOnDatabase) {

        Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues().create();
        rootDatabaseReference.child(user.getUserName()).child("password").setValue(user.getPassword());

        List<RunItem> runItemList = user.getUserRuns();

        if (saveOnDatabase) {


           DatabaseReference runsReference = rootDatabaseReference.child(user.getUserName()).child("runs");//.setValue(gson.toJson(runItemList, ArrayList.class).toString());
            for (RunItem run:runItemList )
            {
                runsReference.child("run"+runItemList.indexOf(run)).setValue(gson.toJson(run,RunItem.class));
            }
            deleteUnnecessaryRuns(user,runsReference);
        }

        SharedPreferencesManager.getInstance().putString("user", gson.toJson(user, User.class));


    }
    private static void deleteUnnecessaryRuns(User user, DatabaseReference rootDatabaseReference)
    {

        rootDatabaseReference.addValueEventListener(new ValueEventListener() {
            ReentrantLock lock = new ReentrantLock();
            boolean firstTime = true;

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {



                lock.lock();
                if (firstTime)
                {
                    firstTime = false;
                    int index = user.getUserRuns().size();
                    while(snapshot.child("run"+index).exists())
                    {

                        try {
                            rootDatabaseReference.child("run"+index).removeValue();
                            index++;


                        } catch (Exception e) {




                            break;
                        }
                    }


                }
                lock.unlock();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
    public static void saveUser(User user, boolean saveOnDatabase) {
        saveUser(user, FirebaseDatabase.getInstance().getReference().child("users"),saveOnDatabase);
    }

    public static void getUser(String userName, DatabaseReference rootDatabaseReference, OnUserReady onUserReady){
        Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues().create();
        rootDatabaseReference.child(userName).addValueEventListener(new ValueEventListener() {

            ReentrantLock lock = new ReentrantLock();
            boolean firstTime = true;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lock.lock();
                if(firstTime)
                {
                    firstTime = false;
                    User user = new User();
                    user.setUserName(userName);
                    try {
                        user.setPassword(snapshot.child("password").getValue().toString());
                        if (snapshot.child("runs").exists())
                        {
                            for (DataSnapshot run:snapshot.child("runs").getChildren())
                                user.addRun(gson.fromJson(run.getValue().toString(), RunItem.class));
                            //                    user.setUserRuns(gson.fromJson(snapshot.child("runs").getValue().toString(), ArrayList.class));
                        }

                    } catch (Exception e) {
                        user = Tools.getUser();
                        Tools.saveUser(user,true);
                    }finally {
                        onUserReady.onUserReady(user);
                    }

                }
                lock.unlock();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public static User getUser ()
    {
        Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues().create();
        return gson.fromJson(SharedPreferencesManager.getInstance().getString("user") , User.class);
    }


//Returns a number with two zeros after the decimal point
public static float getCleanNumber(double num)
{
    num *= 100;
    int numInt = (int)num;
    return numInt/100.0f;
}



    public static Bitmap getBitmapFromView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    public static Bitmap base64ToBitmap(String base64String) {
        byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }


    public static String bitmapToBase64(Bitmap bitmap) {
        byte[] byteArray = bitmapToByteArray(bitmap);
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
    private static byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }



}
