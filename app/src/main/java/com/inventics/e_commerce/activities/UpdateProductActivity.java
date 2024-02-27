package com.inventics.e_commerce.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.inventics.e_commerce.R;
import com.inventics.e_commerce.modal.Product;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class UpdateProductActivity extends AppCompatActivity {
    ImageView productImage;
    TextView clickHereToEditImage;

    TextInputEditText editProductCategory, editProductTitle, editProductDescription, editProductPrice;

    Button updateData;

    String p_id;

    SharedPreferences myPreferences;

    private boolean cameraPermissionGranted = false;

    private static final int REQUEST_IMAGE_CAPTURE = 1001;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1002;

    private String currentPhotoPath;

    File imageFile = null;

    ProgressDialog progress;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);

        gettingAllTheDataInTheViews();

        createProgress();

        findingALlTheViews();

        handlingOnClickListener();


    }

    private void createProgress() {
        progress = new ProgressDialog(this);
        progress.setMax(100);
        progress.setMessage("Its Uploading....");
        progress.setTitle("Uploading Image");
    }

    private void handlingOnClickListener() {
        clickHereToEditImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cameraPermissionGranted) {
                    captureImageWithCamera();
                } else {
                    requestCameraPermission();
                }
            }
        });

        updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadDataToFirebase();
            }
        });
    }

    private void uploadDataToFirebase() {
        if (imageFile != null) {
            // Compress and resize the image before uploading
            Bitmap compressedBitmap = compressImage(Uri.fromFile(imageFile));

            // Upload the compressed image to Firebase Storage
            uploadCompressedImageToFirebase(compressedBitmap);
        } else {
            Toast.makeText(this, "Image not selected", Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap compressImage(Uri imageUri) {
        try {
            // Open an InputStream from the Uri
            InputStream imageStream = getContentResolver().openInputStream(imageUri);

            // Decode the InputStream into a Bitmap
            Bitmap originalBitmap = BitmapFactory.decodeStream(imageStream);

            // Calculate the new dimensions for the compressed image (e.g., 80% of the original)
            int newWidth = (int) (originalBitmap.getWidth() * 0.8);
            int newHeight = (int) (originalBitmap.getHeight() * 0.8);

            // Create a new Bitmap with the calculated dimensions
            Bitmap compressedBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true);

            // Close the InputStream
            if (imageStream != null) {
                imageStream.close();
            }

            return compressedBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    private void uploadCompressedImageToFirebase(Bitmap compressedBitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        compressedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageData = baos.toByteArray();

        // Create a reference to store the file with a unique name
        StorageReference storageRef = FirebaseStorage.getInstance().getReference("images");
        StorageReference fileReference = storageRef.child(System.currentTimeMillis() + ".jpg");

        // Upload the byte array to Firebase Storage
        UploadTask uploadTask = fileReference.putBytes(imageData);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Image upload success
                Log.i("FirebaseStorage", "Upload successful");

                // You can get the download URL of the uploaded image
                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri downloadUrl) {
                        // Handle the download URL (e.g., store it in the database)
                        uploadProductData(downloadUrl);
                        Toast.makeText(UpdateProductActivity.this, "Uploaded SuccessFully", Toast.LENGTH_SHORT).show();
                        Log.i("DownloadUrl", downloadUrl.toString());
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Image upload failed
                Log.e("FirebaseStorage", "Upload failed: " + e.getMessage());
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                float progress_ = (float) (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                progress.setMessage(progress_ + "% uploaded");
                progress.show();

            }
        });
    }


    private void uploadProductData(Uri downloadUrl) {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mref = firebaseDatabase.getReference("Product");

        String S_enterProductCategory = editProductCategory.getText().toString();
        String S_enterProductTitle = editProductTitle.getText().toString();
        String S_enterProductDescription = editProductDescription.getText().toString();
        float S_enterProductPrice = Float.parseFloat(editProductPrice.getText().toString());
        String productImageUrl = downloadUrl.toString();


        int id = (int) System.currentTimeMillis();
        Map<String, Object> productData = new HashMap<>();
        productData.put("title", S_enterProductTitle);
        productData.put("description", S_enterProductDescription);
        productData.put("price", S_enterProductPrice);
        productData.put("category", S_enterProductCategory);
        productData.put("id", id);
        productData.put("image", productImageUrl);
        Map<String, Object> rating = new HashMap<>();
        rating.put("rate", 4.5);
        rating.put("count", 500);
        productData.put("rating", rating);


        if (p_id != null && !p_id.isEmpty()) { // Check if p_id is valid
            databaseReference = mref.child(p_id); // Use the existing push key
        } else {
            Toast.makeText(this, "Something Went Wrong. . .", Toast.LENGTH_SHORT).show(); // Store the push key in p_id
        }

        databaseReference.setValue(productData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(UpdateProductActivity.this, "Data Uploaded", Toast.LENGTH_SHORT).show();
                progress.dismiss();
                startActivity(new Intent(UpdateProductActivity.this, ProductListActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateProductActivity.this, "Something went wrong. . ", Toast.LENGTH_SHORT).show();
                progress.dismiss();
            }
        });


    }

    private void findingALlTheViews() {
        editProductCategory = findViewById(R.id.Et_editProductCategory);
        editProductTitle = findViewById(R.id.Et_enterProductTitle);
        editProductDescription = findViewById(R.id.Et_editProductDescription);
        editProductPrice = findViewById(R.id.Et_editProductPrice);
        productImage = findViewById(R.id.productImage);
        clickHereToEditImage = findViewById(R.id.clickHereToEditImage);
        updateData = findViewById(R.id.updateProduct);
    }

    private void gettingAllTheDataInTheViews() {
        myPreferences = PreferenceManager.getDefaultSharedPreferences(UpdateProductActivity.this);
        p_id = myPreferences.getString("p_id", "");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference().child("Product");
        databaseReference.child(p_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getValue() != null) {
                    Product product = snapshot.getValue(Product.class);

                    // Check if the product object is not null before accessing its properties
                    if (product != null) {
                        editProductTitle.setText(product.getTitle() != null ? product.getTitle() : "");
                        editProductDescription.setText(product.getDescription() != null ? product.getDescription() : "");
                        editProductPrice.setText(String.valueOf(product.getPrice() != null ? product.getPrice() : ""));
                        editProductCategory.setText(product.getCategory() != null ? product.getCategory() : "");
//                        productRateCount.setText((product.getRating() != null ? String.valueOf(product.getRating().getCount()) : "") + " Reviews");
//                        productRating.setRating(product.getRating() != null ? product.getRating().getRate() : 0);
                        Glide.with(UpdateProductActivity.this)
                                .load(product.getImage())
                                .into(productImage);
                    } else {
                        // Handle the case where the product object is null
                        Toast.makeText(UpdateProductActivity.this, "Product not found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Handle the case where the DataSnapshot is null or doesn't exist
                    Toast.makeText(UpdateProductActivity.this, "Product not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


    }


    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                CAMERA_PERMISSION_REQUEST_CODE);
    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            // Check if the permission has been granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission has been granted
                cameraPermissionGranted = true;
                captureImageWithCamera();
            } else {
                // Permission denied, show a message or handle accordingly
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void captureImageWithCamera() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.inventics.e_commerce.provider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }

        }

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        // Get the directory for the app's external pictures directory
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        // Create the image file
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Image captured and saved to fileUri specified in the Intent
            // Now you can use the 'currentPhotoPath' to do whatever you want with the image
            // For example, you can display it in an ImageView or save it to storage
            // Save the image to the gallery
            galleryAddPic();
        }
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Log.i("ImagePath", currentPhotoPath);
        Log.i("File F", f.toString());
        Uri contentUri = Uri.fromFile(f);
        Log.i("ImageUri", contentUri.toString());
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);

        getTheOriginalImageTempFileAsInputStream(contentUri);
    }


    private void getTheOriginalImageTempFileAsInputStream(Uri contentUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(contentUri);
            if (inputStream != null) {
                // Get the size of the image
                int imageSize = inputStream.available();
                Log.i("Image Size", String.valueOf(imageSize));

                // Close the input stream when done
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        convertTheImageInputStreamToOutputStream(contentUri);
    }

    private void convertTheImageInputStreamToOutputStream(Uri contentUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(contentUri);
            if (inputStream != null) {
                // Create an output stream
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                // Transfer data from input stream to output stream
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, length);
                }

                // Get the size of the data in the output stream
                byte[] imageData = outputStream.toByteArray();
                int imageSize = imageData.length;
                Log.i("8Image Size", String.valueOf(imageSize));

                // Close the streams when done
                inputStream.close();
                outputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        saveImageToPublicDirectoryPictures(contentUri);
    }

    private void saveImageToPublicDirectoryPictures(Uri contentUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(contentUri);
            if (inputStream != null) {
                // Create an output stream
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                // Transfer data from input stream to output stream
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, length);
                }

                // Get the data as byte array
                byte[] imageData = outputStream.toByteArray();

                // Close the input stream and output stream
                inputStream.close();
                outputStream.close();

                // Get the public "Pictures" directory
                File picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                if (picturesDir != null) {
                    // Create a subfolder named "MyAppImages" within the "Pictures" directory
                    File myAppImagesDir = new File(picturesDir, "com.inventics.E-Commerce");
                    if (!myAppImagesDir.exists()) {
                        // Create the subfolder if it doesn't exist
                        if (myAppImagesDir.mkdirs()) {
                            Log.i("Subfolder Created", myAppImagesDir.getAbsolutePath());
                        } else {
                            Log.e("Error", "Failed to create subfolder");
                            return;
                        }
                    }

                    // Create a new file in the subfolder
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
                    String currentDateTime = dateFormat.format(new Date());

                    // Append the current date and time to the image name
                    String imageName = "image_" + currentDateTime + ".jpg";

                    Log.i("8_ImageName", imageName);

                    // Create a new file in the subfolder with the modified image name
                    imageFile = new File(myAppImagesDir, imageName);

                    // Write the data to the file
                    FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
                    fileOutputStream.write(imageData);
                    fileOutputStream.close();

                    // Log the path of the saved image file
                    Log.i("Saved Image Path", imageFile.getAbsolutePath());
                    savingImageToTheImageView(imageFile.getAbsolutePath());

                } else {
                    Log.e("Error", "Failed to get public Pictures directory");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void savingImageToTheImageView(String absolutePath) {
        Bitmap bitmap = BitmapFactory.decodeFile(absolutePath);
        productImage.setImageBitmap(bitmap);
        Log.i("8BitMapSize", String.valueOf(bitmap.getByteCount()));
    }


}