package com.example.goodrun;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.goodrun.appInterfaces.OnUserSave;
import com.example.goodrun.utilities.Tools;
import com.example.goodrun.Models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SignInActivity extends AppCompatActivity {
    private enum SignInProblemMessage {none, alreadyExists, passwords_do_not_match, invalid}

    ;

    private SignInProblemMessage signInProblemMessage = SignInProblemMessage.none;//in the beginning no need for red note

    private User user;
    private DatabaseReference rootDatabaseReference;
    private ArrayList<String> childrenWhoExist;


    private EditText enter_your_name_sign_in_activity_EditText, enter_password_1_EditText, enter_password_2_EditText;
    private TextView username_already_exists_TextView, the_passwords_do_not_match_TextView, password_or_user_name_invalid_TextView;
    private Button sign_in_Button, log_in_Button_sign_in_activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        findViews();
        initviews();


    }

    private void findViews() {

        enter_your_name_sign_in_activity_EditText = findViewById(R.id.enter_your_name_sign_in_activity_EditText);
        enter_password_1_EditText = findViewById(R.id.enter_password_1_EditText);
        enter_password_2_EditText = findViewById(R.id.enter_password_2_EditText);
        username_already_exists_TextView = findViewById(R.id.username_already_exists_TextView);
        the_passwords_do_not_match_TextView = findViewById(R.id.the_passwords_do_not_match_TextView);
        password_or_user_name_invalid_TextView = findViewById(R.id.password_or_user_name_invalid_TextView);
        sign_in_Button = findViewById(R.id.sign_in_Button);
        log_in_Button_sign_in_activity = findViewById(R.id.log_in_Button_sign_in_activity);


    }

    private void initviews() {
        sign_in_Button.setOnClickListener(v -> signIn());
        log_in_Button_sign_in_activity.setOnClickListener(v -> logIn());
        rootDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        rootDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                childrenWhoExist = new ArrayList<String>();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    childrenWhoExist.add(snap.getKey().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void logIn() {
        Intent intent = new Intent(SignInActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void signIn() {


        String username = enter_your_name_sign_in_activity_EditText.getText().toString();
        String password1 = enter_password_1_EditText.getText().toString();
        String password2 = enter_password_2_EditText.getText().toString();

        if (!(checksUserInput(username) && checksUserInput(password1)))//if one of the Inputs is invalid
        {
            signInProblemMessage = SignInProblemMessage.invalid;
            updateUI();

        } else if (Tools.checksUserNameExists(username, childrenWhoExist))//if username Exists
        {
            signInProblemMessage = SignInProblemMessage.alreadyExists;
            updateUI();
        } else if (!password1.equals(password2))// if password1 not equals to password2
        {
            signInProblemMessage = SignInProblemMessage.passwords_do_not_match;
            updateUI();
        } else {
            user = new User(username, password1);//Adds the user
            Tools.saveUser(user, true);
            Intent intent = new Intent(SignInActivity.this, ProfileActivity.class);
            intent.putExtra("userName", user.getUserName());
            startActivity(intent);
            finish();



        }

    }


    private boolean checksUserInput(String userInput)  //Checks that the input is greater than 4 characters and does not contain white space
    {
        int len = userInput.length();
        return len >= 4 && len == userInput.trim().length();

    }


    private void updateUI() {//  Put the correct red note
        switch (signInProblemMessage) {
            case none:
                the_passwords_do_not_match_TextView.setVisibility(View.INVISIBLE);
                username_already_exists_TextView.setVisibility(View.INVISIBLE);
                password_or_user_name_invalid_TextView.setVisibility(View.INVISIBLE);
                break;
            case alreadyExists:
                the_passwords_do_not_match_TextView.setVisibility(View.INVISIBLE);
                username_already_exists_TextView.setVisibility(View.VISIBLE);
                password_or_user_name_invalid_TextView.setVisibility(View.INVISIBLE);
                break;
            case passwords_do_not_match:
                the_passwords_do_not_match_TextView.setVisibility(View.VISIBLE);
                username_already_exists_TextView.setVisibility(View.INVISIBLE);
                password_or_user_name_invalid_TextView.setVisibility(View.INVISIBLE);
                break;
            case invalid:
                the_passwords_do_not_match_TextView.setVisibility(View.INVISIBLE);
                username_already_exists_TextView.setVisibility(View.INVISIBLE);
                password_or_user_name_invalid_TextView.setVisibility(View.VISIBLE);
                break;
        }
    }

}