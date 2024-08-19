package com.example.goodrun;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.goodrun.utilities.Tools;
import com.example.goodrun.RcyclerView.MyRunRcyclerView.MyRunAdapter;
import com.example.goodrun.Models.User;
import com.example.goodrun.appInterfaces.OnClick;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

public class MyRunsActivity extends AppCompatActivity {
    private User user;

    private RecyclerView my_runs_recycler_view;



    private Button  new_run_Button, profile_Button;
    private DatabaseReference rootDatabaseReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_runs);


        findViews();
        initViews();





    }

    private void findViews() {
        my_runs_recycler_view = findViewById(R.id.my_runs_recycler_view);
        new_run_Button = findViewById(R.id.new_run_Button);
        profile_Button= findViewById(R.id.profile_Button);
    }

    private void initViews() {
        Gson gson = new Gson();

        user = Tools.getUser();

        if (getIntent().getBooleanExtra( "save user to database",false))
            Tools.saveUser(user,true);

        my_runs_recycler_view.setLayoutManager(new LinearLayoutManager(this));
        my_runs_recycler_view.setAdapter(new MyRunAdapter(getApplicationContext(), user.getUserRuns(), new OnClick() {
            @Override
            public void click(int num) {
                Intent intent = new Intent(MyRunsActivity.this, RunActivity.class);
                intent.putExtra("run index", num);
                startActivity(intent);
                finish();
            }
        }));


        rootDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users");

        profile_Button.setOnClickListener(v -> goToProfile());
        new_run_Button.setOnClickListener(v -> goToNewRun());

    }

    private void goToNewRun() {
        Intent intent = new Intent(MyRunsActivity.this, NewRunActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToProfile() {
        Intent intent = new Intent(MyRunsActivity.this, ProfileActivity.class);
        intent.putExtra("userName", user.getUserName());
        startActivity(intent);
        finish();
    }
}






