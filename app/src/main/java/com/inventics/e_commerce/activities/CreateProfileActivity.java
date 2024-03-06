package com.inventics.e_commerce.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.inventics.e_commerce.databinding.ActivityCreateProfileActivtyBinding;

import java.util.HashMap;

public class CreateProfileActivity extends AppCompatActivity {
    ActivityCreateProfileActivtyBinding binding;
    FirebaseUser currentUser;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    ProgressDialog progressDialog;
    Uri uri = null;
    String imageUrl;
    private static final int REQUEST_IMAGE_PICK = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateProfileActivtyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        handleOnClickListener();
    }

    private void handleOnClickListener() {
        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("9996", uri.toString());
                if(uri == null){
                    String name = binding.enterName.getText().toString();

                    Log.i("9998", uri.toString());

                    String email = binding.enterEmail.getText().toString();
                    String enterShippingAddress = binding.enterShippingAddress.getText().toString();
                    getUserData(name, email, enterShippingAddress);
                }else {
                    uploadImageToFirebaseStorage(uri);
                }

                Log.i("9997", uri.toString());


            }
        });

        binding.clickHereToEditImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFiles();
            }
        });

    }

    private void getUserData(String name, String email, String enterShippingAddress) {
        if (!name.isEmpty() && !email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches() && !enterShippingAddress.isEmpty()) {
            pushDataToFirebase(name, email, enterShippingAddress);
            showProgressDialog("Uploading ...");
        } else {
            Toast.makeText(this, "Enter valid details . . .", Toast.LENGTH_SHORT).show();
        }
    }

    private void pushDataToFirebase(String name, String email, String enterShippingAddress) {

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        Log.i("5currentUser", currentUser.getUid());
        if (currentUser != null) {
            String userId = currentUser.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId);
            HashMap<String, Object> userData = new HashMap<>();
            userData.put("name", name);
            userData.put("email", email);
            userData.put("address", enterShippingAddress);
//            Log.i("7894",imageUrl);
            userData.put("image", imageUrl);
            userData.put("phoneNUmber", currentUser.getPhoneNumber());
            databaseReference.setValue(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(CreateProfileActivity.this, "Uploaded Successfully. . ", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
//                    getOnBackPressedDispatcher().onBackPressed();

                    startActivity(new Intent(CreateProfileActivity.this,MainActivity.class));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(CreateProfileActivity.this, "Some thing went wrong. . .", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void openFiles() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE_PICK);

    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            uri = data.getData();
            Log.i("uri", uri.toString());
            binding.circleImageView.setImageURI(uri);
            // Image selected, you can handle it here
            // The selected image URI can be obtained from data.getData()
        }
    }
    private void uploadImageToFirebaseStorage(Uri imageUri) {
        showProgressDialog("Uploading . . . ");
        // File name in Firebase Storage (you can change it as needed)
        String imageName = "image_" + "Ecommerce_" + System.currentTimeMillis() + ".jpg";

        storageReference = FirebaseStorage.getInstance().getReference().child("images");

        storageReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Image upload successful, get download URL
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                imageUrl = uri.toString();
                                Log.i("0_imageUrl", imageUrl);
                                String name = binding.enterName.getText().toString();

                                Log.i("9998", uri.toString());

                                String email = binding.enterEmail.getText().toString();
                                String enterShippingAddress = binding.enterShippingAddress.getText().toString();
                                getUserData(name, email, enterShippingAddress);
                                // URL of the uploaded image
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Image upload failed
                        Toast.makeText(CreateProfileActivity.this, "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showProgressDialog(String message) {
        progressDialog = new ProgressDialog(CreateProfileActivity.this);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }


}