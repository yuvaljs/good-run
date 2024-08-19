package com.example.goodrun;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.goodrun.utilities.Tools;
import com.example.goodrun.Models.User;
import com.example.goodrun.appInterfaces.OnUserReady;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LoginActivity extends AppCompatActivity {
    private EditText enter_your_name_EditText, enter_password_EditText;
    private TextView user_name_or_password_is_not_correct_TextView;
    private Button log_in_Button, sign_in_Button;
    private DatabaseReference rootDatabaseReference;
    private ArrayList<String> childrenWhoExist;
    private final ReentrantLock lock = new ReentrantLock();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViews();
        initViews();


    }

    private void findViews() {
        enter_your_name_EditText = findViewById(R.id.enter_your_name_EditText);
        enter_password_EditText= findViewById(R.id.enter_password_EditText);
        user_name_or_password_is_not_correct_TextView = findViewById(R.id.user_name_or_password_is_not_correct_TextView);
        log_in_Button= findViewById(R.id.log_in_Button);
        sign_in_Button= findViewById(R.id.sign_in_Button);
    }

    private void initViews() {
        rootDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        rootDatabaseReference.addValueEventListener(new ValueEventListener() {//get list of all the userNames
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                childrenWhoExist = new ArrayList<String>();
                for (DataSnapshot snap :snapshot.getChildren() )
                {
                    childrenWhoExist.add(snap.getKey().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        sign_in_Button.setOnClickListener(new View.OnClickListener() {//go to SignInActivity
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(LoginActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });


        log_in_Button.setOnClickListener(v-> logIn());

    }

    private void logIn() {

        String username = enter_your_name_EditText.getText().toString();
        String password = enter_password_EditText.getText().toString();
        if(!Tools.checksUserNameExists(username, childrenWhoExist)) {
            user_name_or_password_is_not_correct_TextView.setVisibility(View.VISIBLE);
        }
        else {
            //    public static void getUser(String userName, DatabaseReference rootDatabaseReference, OnUserReady onUserReady){
            rootDatabaseReference.child(username).child("password").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(password.equals(snapshot.getValue().toString()) )// if the password is correct
                    {
                         Gson gson = new Gson();
                          Tools.getUser(username, rootDatabaseReference, new OnUserReady() {
                             @Override
                             public void onUserReady(User user) {
                                 lock.lock();
                                 Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                                 intent.putExtra("userName",username);
                                 startActivity(intent);
                                 finish();

                                 lock.unlock();

                             }
                         });

                    }
                    else{//if the password is not correct
                        user_name_or_password_is_not_correct_TextView.setVisibility(View.VISIBLE);
                    }
                }



                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}