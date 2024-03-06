package com.inventics.e_commerce.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.inventics.e_commerce.R;

public class SplashActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    FirebaseDatabase database;
    DatabaseReference databaseRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initializeViews();
        hidingActionBar();
        startSplashActivity();

    }

    private void initializeViews() {
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference();

        Log.i("i_firebaseAuth", firebaseAuth.toString());
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
                finish();
            }
        }, 3000);
    }

    private void checkingIfUserExist() {
        if(currentUser != null) {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

        } else {
            Intent intent = new Intent(SplashActivity.this, SignUpActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

        }

    }
}
