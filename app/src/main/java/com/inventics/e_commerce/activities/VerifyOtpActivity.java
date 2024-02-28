package com.inventics.e_commerce.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.inventics.e_commerce.R;

public class VerifyOtpActivity extends AppCompatActivity {

    TextInputEditText enterOTP;
    Button verifyOTP;

    ProgressBar progressBar_VerifyOTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        initializingViews();

        handleOnClickListener();
    }

    private void initializingViews() {
        enterOTP = findViewById(R.id.enterOTP);
        verifyOTP = findViewById(R.id.verifyOTP);
        progressBar_VerifyOTP = findViewById(R.id.progressBar_VerifyOtp);
    }

    private void handleOnClickListener() {

        verifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar_VerifyOTP.setVisibility(View.VISIBLE);
                verifyingOTP();
            }
        });

    }

    private void verifyingOTP() {

        String codeSent = getIntent().getStringExtra("OTP");
        String otpEnteredByUser = enterOTP.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, otpEnteredByUser);
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // OTP verification successful, navigate to the next activity
                        startActivity(new Intent(VerifyOtpActivity.this, MainActivity.class));
                        progressBar_VerifyOTP.setVisibility(View.GONE);
                        finish(); // Close this activity
                    } else {
                        // OTP verification failed
                        // Handle the failure, for example, show an error message
                        Toast.makeText(VerifyOtpActivity.this, "Failed to verify OTP", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(VerifyOtpActivity.this, "Failed = " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }


}