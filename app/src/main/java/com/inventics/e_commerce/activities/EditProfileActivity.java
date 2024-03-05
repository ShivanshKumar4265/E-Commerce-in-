package com.inventics.e_commerce.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.inventics.e_commerce.databinding.ActivityEditProfileBinding;

import java.util.HashMap;

public class EditProfileActivity extends AppCompatActivity {

    ActivityEditProfileBinding binding;
    FirebaseUser currentUser;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        handleOnClickListener();

    }

    private void handleOnClickListener() {
        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.enterName.getText().toString();
                String email = binding.enterEmail.getText().toString();
                String phoneNumber = binding.enterNumber.getText().toString();
                submitUserDataToFirebase(name,email,phoneNumber);

            }
        });
    }

    private void submitUserDataToFirebase(String name, String email, String phoneNumber) {
        if(name.isEmpty()){
           binding.enterName.setError("It cant be blank");
        }else if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.enterEmail.setError("Enter Valid Mail id");
        }else if(phoneNumber.length() < 10){
            binding.enterNumber.setError("Enter Valid Number");
        }else{
            pushDataToFirebase(name,email,phoneNumber);
        }
        showProgressDialog("Uploading. . .");
    }

    private void pushDataToFirebase(String name, String email, String phoneNumber) {

        currentUser = firebaseAuth.getInstance().getCurrentUser();
        Log.i("5currentUser", currentUser.getUid().toString());
        if (currentUser != null) {
            String userId = currentUser.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId);
            HashMap<String,Object> userData = new HashMap<>();
            userData.put("name",name);
            userData.put("email",email);
            userData.put("phoneNumber",phoneNumber);
            databaseReference.setValue(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(EditProfileActivity.this, "Uploaded Successfully. . ", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditProfileActivity.this, "Some thing went wrong. . .", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private void showProgressDialog(String message) {
        progressDialog = new ProgressDialog(EditProfileActivity.this);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
}