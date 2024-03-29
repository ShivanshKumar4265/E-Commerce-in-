package com.inventics.e_commerce.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.inventics.e_commerce.R;
import com.inventics.e_commerce.expanding_textview.MySpannable;
import com.inventics.e_commerce.modal.Product;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


public class ProductDescriptionActivity extends AppCompatActivity {
    ImageView productImage1, productImage2, productImage3, addToCart, removeFromCart, slide;
    int paddingL = 0;
    String uid;
    LinearLayout addAndRemoveProduct;
    RelativeLayout addAndRemoveProduct1;
    ViewFlipper viewFlipper;
    TextView productCategory, productTitle, productDescription, productPrice, productRateCount, numberOfProductAdded;
    RatingBar productRating;
    ImageView deleteData, updateData, btn_addToCart;
    int totalItem = 1;
    String p_id;
    SharedPreferences myPreferences;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_description_test);

        findingAllTheViewsById();

        gettingProductKeyViaSharedPreference();

        getDescriptionOfTheProduct();

        handleSetOnClickListener();

    }

    private void handleSetOnClickListener() {
        deleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProductData();
            }
        });
        updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProductData();
            }
        });
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalItem += 1;
                setCount(totalItem);

            }
        });
        removeFromCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalItem -= 1;
                setCount(totalItem);
            }
        });
        slide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideLeft(addAndRemoveProduct1);
            }
        });
        btn_addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAuth.getCurrentUser() != null) {
                    // User is signed in
                    uid = mAuth.getCurrentUser().getUid();
                    Log.d("MainActivity", "User ID: " + uid);
                } else {
                    Log.d("MainActivity", "User is signed out");
                }
                String timestamp = getCurrentTimestamp();
                databaseReference = firebaseDatabase.getReference().child("cart").child(uid);
                HashMap<String,Object> cartData = new HashMap<>();
                cartData.put("pro_key",p_id);
                cartData.put("qty",totalItem);
                cartData.put("timestamp",timestamp);
//                assert key != null;
                databaseReference.child(p_id).setValue(cartData).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(ProductDescriptionActivity.this, "Added successfully..", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProductDescriptionActivity.this, e + "", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }

    private void updateProductData() {

        startActivity(new Intent(ProductDescriptionActivity.this, UpdateProductActivity.class));

    }

    private void findingAllTheViewsById() {

        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        productImage1 = findViewById(R.id.productImage1);
        productImage2 = findViewById(R.id.productImage2);
        productImage3 = findViewById(R.id.productImage3);
        productCategory = findViewById(R.id.productCategory);
        viewFlipper = findViewById(R.id.viewFlipper);
        productTitle = findViewById(R.id.productTitle);
        productDescription = findViewById(R.id.productDescription);
        productPrice = findViewById(R.id.productPrice);
        productRateCount = findViewById(R.id.productRateCount);
        productRating = findViewById(R.id.productRating);

        deleteData = findViewById(R.id.deleteData);
        updateData = findViewById(R.id.updateData);

        addAndRemoveProduct1 = findViewById(R.id.addAndRemoveProduct1);

        addToCart = findViewById(R.id.addToCart);
        removeFromCart = findViewById(R.id.removeFromCart);
        numberOfProductAdded = findViewById(R.id.numberOfProductAdded);

        slide = findViewById(R.id.slide);
        addAndRemoveProduct = findViewById(R.id.addRemoveProduct);
        btn_addToCart = findViewById(R.id.btn_addToCart);
    }

    private static String getCurrentTimestamp() {
        // Get the current date and time
        Date currentDate = new Date();

        // Define the date format you want
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // Format the date and time using the specified format
        String formattedDate = dateFormat.format(currentDate);

        return formattedDate;
    }

    private void gettingProductKeyViaSharedPreference() {
        myPreferences = PreferenceManager.getDefaultSharedPreferences(ProductDescriptionActivity.this);
        p_id = myPreferences.getString("p_id", "");
        Toast.makeText(ProductDescriptionActivity.this, "Pid=" + p_id, Toast.LENGTH_SHORT).show();
    }

    private void deleteProductData() {

        // Get a reference to the root of your Firebase Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Product");

        // Remove the value at the specified node path
        databaseReference.child(p_id).removeValue()
                .addOnSuccessListener(aVoid -> {
                    // Deletion successful
                    Toast.makeText(this, "Delete Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ProductDescriptionActivity.this, MainActivity.class));
                })
                .addOnFailureListener(e -> {
                    // Deletion failed
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void getDescriptionOfTheProduct() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference().child("Product");
        databaseReference.child(p_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getValue() != null) {
                    Product product = snapshot.getValue(Product.class);

                    // Check if the product object is not null before accessing its properties
                    if (product != null) {
                        productTitle.setText(product.getTitle() != null ? product.getTitle() : "");
                        productDescription.setText(product.getDescription() != null ? product.getDescription() : "");
                        productPrice.setText("INR " + (product.getPrice() != null ? product.getPrice() : ""));
                        productCategory.setText(product.getCategory() != null ? product.getCategory() : "");
                        productRateCount.setText((product.getRating() != null ? String.valueOf(product.getRating().getCount()) : "") + " Reviews");
                        productRating.setRating(product.getRating() != null ? product.getRating().getRate() : 0);
                        Glide.with(ProductDescriptionActivity.this)
                                .load(product.getImage())
                                .into(productImage1);

                        Glide.with(ProductDescriptionActivity.this)
                                .load(product.getImage())
                                .into(productImage2);

                        Glide.with(ProductDescriptionActivity.this)
                                .load(product.getImage())
                                .into(productImage3);

                        viewFlipper.startFlipping();
                        makeTextViewResizable(productDescription, 2, "...Read more", true);
                    } else {
                        // Handle the case where the product object is null
                        Toast.makeText(ProductDescriptionActivity.this, "Product not found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Handle the case where the DataSnapshot is null or doesn't exist
                    Toast.makeText(ProductDescriptionActivity.this, "Product not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }


        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {

                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine == 0) {
                    int lineEndIndex = tv.getLayout().getLineEnd(0);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else {
                    int lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                }
            }
        });

    }

    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv, final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {


            ssb.setSpan(new MySpannable(false) {
                @Override
                public void onClick(View widget) {
                    if (viewMore) {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, -1, "Read Less", false);
                    } else {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, 3, "... Read More", true);
                    }
                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;

    }

    private void setCount(int totalItem) {


        if (totalItem >= 1) {
            numberOfProductAdded.setText("  " + totalItem + "  ");
        }

    }

    private void slideLeft(View view) {

        if (paddingL == 0) {
            paddingL = addAndRemoveProduct1.getPaddingLeft();
        }

        Log.d("TAG", "slideLeft: " + addAndRemoveProduct1.getPaddingLeft());
        if (addAndRemoveProduct1.getPaddingLeft() > 100) {
            addAndRemoveProduct1.setPadding(25, 0, 0, 30);
            slide.setImageResource(R.drawable.baseline_arrow_forward_ios_24);
            addAndRemoveProduct.setVisibility(View.VISIBLE);
            deleteData.setVisibility(View.VISIBLE);
            updateData.setVisibility(View.VISIBLE);
            btn_addToCart.setVisibility(View.VISIBLE);
        } else {
            addAndRemoveProduct1.setPadding(paddingL, 0, 0, 30);
            slide.setImageResource(R.drawable.baseline_arrow_back_ios_24);
            addAndRemoveProduct.setVisibility(View.GONE);
            deleteData.setVisibility(View.GONE);
            updateData.setVisibility(View.GONE);
            btn_addToCart.setVisibility(View.GONE);

        }

    }

}




