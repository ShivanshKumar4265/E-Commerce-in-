package com.inventics.e_commerce.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.inventics.e_commerce.R;
import com.inventics.e_commerce.java_class.MySpannable;
import com.inventics.e_commerce.modal.Product;


public class ProductDescriptionActivity extends AppCompatActivity {
    ImageView productImage1, productImage2, productImage3;
    ViewFlipper viewFlipper;
    TextView productCategory, productTitle, productDescription, productPrice, productRateCount;
    RatingBar productRating;
    Button deleteData, updateData;
    String p_id;

    SharedPreferences myPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_description);


        findingAllTheViewsById();

        gettingProductKeyViaSharedPreference();

        getDescriptionOfTheProduct();

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


    }

    private void updateProductData() {

        startActivity(new Intent(ProductDescriptionActivity.this, UpdateProductActivity.class));

    }

    private void findingAllTheViewsById() {
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
                    startActivity(new Intent(ProductDescriptionActivity.this, ProductListActivity.class));
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
                        makeTextViewResizable(productDescription,2,"...Read more",true);
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

    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned,
                                                                            final TextView tv,
                                                                            final int maxLine,
                                                                            final String spanableText,
                                                                            final boolean viewMore) {
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


}