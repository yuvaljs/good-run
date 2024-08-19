package com.example.goodrun.Models;

import android.util.Log;

import com.example.goodrun.Models.dataStructures.RunItem;
import com.example.goodrun.utilities.Tools;

import java.util.ArrayList;

public class User {
    private String userName = "";
    private String password = "";
    private ArrayList<RunItem> userRuns  = new ArrayList<RunItem>();

    public User() {

    }

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public User setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public User addRun(RunItem run)
    {
        userRuns.add(0,run);
        return this;
    }
    public User deleteRun(int index){
        userRuns.remove(index);
        return this;
    }
    public User deleteRun(RunItem run){
        userRuns.remove(run);
        return this;
    }
    public ArrayList<RunItem> getUserRuns() {
        return userRuns;
    }

    public User setUserRuns(ArrayList<RunItem> userRuns) {
        this.userRuns = userRuns;
        return this;
    }

    public float getDistanceRun() {
        float sum = 0;

        for(RunItem run : userRuns)
        {
            sum += run.getDistance();
        }
        return sum;
    }

    public float getDistanceOfLongestRun() {
        float max = 0.0f;
        float distance = 0.0f;


        for(RunItem run : userRuns){
            distance = run.getDistance();
            if (distance>max)
            {
                max = distance;
            }
        }
        return Tools.getCleanNumber(max);
    }

    public float getDistanceRunThisWeek() {

        float sum = 0.0f;


        long lastWeekTime = System.currentTimeMillis()-(1000*60*60*24*7);
        for(RunItem run : userRuns){

            if (run.getStopTime()>lastWeekTime)
            {
                sum += run.getDistance();
            }
        }
        return sum;
    }

    public int getRunsRavedThisWeek() {
        int sum = 0;
//        if (userRuns.size() < 1)
//            return sum;

        long lastWeekTime = System.currentTimeMillis()-(1000*60*60*24*7);
        for(RunItem run : userRuns){

            if (run.getStopTime()>lastWeekTime)
            {
                sum ++;
            }
        }
        return sum;
    }

    public int getRunsSaved() {
        return userRuns.size();
    }
}