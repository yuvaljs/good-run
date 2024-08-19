package com.example.goodrun;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.goodrun.appInterfaces.OnUserReady;
import com.example.goodrun.utilities.Tools;
import com.example.goodrun.Models.User;
import com.example.goodrun.utilities.SharedPreferencesManager;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.concurrent.locks.ReentrantLock;

public class ProfileActivity extends AppCompatActivity {
    private Button log_out, app_navigation_bar_new_run_Button, app_navigation_bar_my_runs_Button;
    private User appUser = null;
    private TextView user_name_text_TextView, distanceOfLongestRun, distanceRunThisWeek, runsSavedThisWeek, distanceRun, runsSaved;
    private final ReentrantLock lock = new ReentrantLock();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Gson gson = new Gson();


        findViews();
        initviews();
    }

    private void findViews() {
        log_out = findViewById(R.id.log_out);
        app_navigation_bar_new_run_Button = findViewById(R.id.app_navigation_bar_new_run_Button);
        app_navigation_bar_my_runs_Button = findViewById(R.id.app_navigation_bar_my_runs_Button);
        user_name_text_TextView = findViewById(R.id.user_name_text_TextView);
        distanceOfLongestRun = findViewById(R.id.distanceOfLongestRun);
        distanceRunThisWeek = findViewById(R.id.distanceRunThisWeek);
        runsSavedThisWeek = findViewById(R.id.runsSavedThisWeek);
        distanceRun = findViewById(R.id.distanceRun);
        runsSaved = findViewById(R.id.runsSaved);
    }

    private void initviews() {
        initNavigationBar();

        getUser();


    }

    private void getUser() {


        String userName = getIntent().getStringExtra("userName");


        Tools.getUser(userName, FirebaseDatabase.getInstance().getReference().child("users"), new OnUserReady() {//get app user
            @Override
            public void onUserReady(User user) {
                lock.lock();
                if (appUser == null) {
                    appUser = user;
                    Tools.saveUser(user, false);


                }

                lock.unlock();

                initUI();
            }
        });


    }


    private void initUI() {


        user_name_text_TextView.setText("" + appUser.getUserName());
        distanceOfLongestRun.setText("" + appUser.getDistanceOfLongestRun());
        distanceRunThisWeek.setText("" + appUser.getDistanceRunThisWeek());
        runsSavedThisWeek.setText("" + appUser.getRunsRavedThisWeek());
        distanceRun.setText("" + appUser.getDistanceRun());
        runsSaved.setText("" + appUser.getRunsSaved());

    }

    private void initNavigationBar() {


        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesManager.getInstance().putString("user", null);//log out

                Intent intent = new Intent(ProfileActivity.this, WelcomeActivity.class);//go to WelcomeActivity
                startActivity(intent);
                finish();
            }
        });
        app_navigation_bar_new_run_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ProfileActivity.this, NewRunActivity.class);
                startActivity(intent);
                finish();
            }
        });
        app_navigation_bar_my_runs_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, MyRunsActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }


}