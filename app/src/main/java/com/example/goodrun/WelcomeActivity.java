package com.example.goodrun;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.example.goodrun.Models.User;
import com.example.goodrun.utilities.SharedPreferencesManager;
import com.google.gson.Gson;

public class WelcomeActivity extends AppCompatActivity {
    private User user;
    private Button log_in_Button, sign_in_Button;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);



        profileAlreadyExists();//If a profile already exists, then enter it

        findViews();
        initViews();

    }

    private void profileAlreadyExists() {//If a profile already exists, then enter it
        Gson gson = new Gson();

        try {
            user = gson.fromJson(SharedPreferencesManager.getInstance().getString("user"), User.class);

        }
        catch (RuntimeException e)
        {
            user = null;
        }
        if (user != null)
        {
            Intent intent  = new Intent(WelcomeActivity.this, ProfileActivity.class);
            intent.putExtra("userName",user.getUserName());
            startActivity(intent);
            finish();
        }
    }

    private void initViews() {
        sign_in_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {// move to activity Already exist creit new user
                Intent intent  = new Intent(WelcomeActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });
        log_in_Button.setOnClickListener(new View.OnClickListener() {// move to activity Already exist log to Already exist user
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void findViews() {
        sign_in_Button = findViewById(R.id.sign_in_Button);
        log_in_Button = findViewById(R.id.log_in_Button);
    }
}