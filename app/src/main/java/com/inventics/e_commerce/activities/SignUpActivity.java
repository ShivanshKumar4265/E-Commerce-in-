package com.inventics.e_commerce.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.inventics.e_commerce.R;

import java.util.concurrent.TimeUnit;

public class SignUpActivity extends AppCompatActivity {
    Button sendOtp;
    TextInputEditText enterYourNumber;
    TextView alreadyHaveAnAccount;

    String UserPhoneNumber;
    ProgressBar progressBar_sendOTP;
    FirebaseAuth firebaseAuth;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Sign Up</font>"));

        intializingView();

        handleOnClickListener();

        FirebaseApp.initializeApp(this);


    }

    private void intializingView() {

        sendOtp = findViewById(R.id.sendOtp);
        enterYourNumber = findViewById(R.id.enterYourPhoneNumber);
        firebaseAuth = FirebaseAuth.getInstance();
        Log.i("i_firebaseAuth",firebaseAuth.toString());
        progressBar_sendOTP = findViewById(R.id.progressBar_sendOTP);
        alreadyHaveAnAccount = findViewById(R.id.alreadyHaveAnAcccount);

    }

    private void handleOnClickListener() {
        sendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar_sendOTP.setVisibility(View.VISIBLE);
                sendOtpOnUserMobile();
            }
        });

        alreadyHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentToLogInACtivity();
            }
        });


    }

    private void intentToLogInACtivity() {
        startActivity(new Intent(SignUpActivity.this,LogInActivity.class));
    }

    private void sendOtpOnUserMobile() {
        UserPhoneNumber = enterYourNumber.getText().toString();// User's phone number
        Log.i("i_UserPhoneNumber",UserPhoneNumber);



        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(UserPhoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this) // (optional) Activity for callback binding
                        // If no activity is passed, reCAPTCHA verification can not be used.
                        // OnVerificationStateChangedCallbacks
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                Log.d("i_PhoneAuthCredentials", phoneAuthCredential.toString());

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Log.d("i_firebaseException", e.getMessage().toString());

                            }


                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                Log.d("i_otp", s);
                                Log.d("i_forceResendingToken",forceResendingToken.toString());
                                Intent intent = new Intent(SignUpActivity.this,VerifyOtpActivity.class);
                                intent.putExtra("OTP",s);
                                progressBar_sendOTP.setVisibility(View.GONE);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }


}