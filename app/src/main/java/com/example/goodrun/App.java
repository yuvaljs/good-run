package com.example.goodrun;

import android.app.Application;

import com.example.goodrun.utilities.SharedPreferencesManager;


public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferencesManager.init(this);
    }

}
