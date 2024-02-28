package com.inventics.e_commerce.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.inventics.e_commerce.R;

public class SplashActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        hidingActionBar();
        startSplashActivity();
        initializeViews();





    }

    private void initializeViews() {
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        Log.i("i_firebaseAuth",firebaseAuth.toString());
//        Log.i("i_currentUser",currentUser.toString());
    }

    private void hidingActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    private void startSplashActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkingIfUserExist();
//                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
                finish();
            }
        }, 3000);

    }

    private void checkingIfUserExist() {

        if (currentUser != null) {
            // User is already registered, navigate to main activity or home screen
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish(); // close splash activity
        } else {
            // User is not registered, navigate to registration or login activity
            Intent intent = new Intent(SplashActivity.this, SignUpActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish(); // close splash activity
        }

    }
}