package com.inventics.e_commerce.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.inventics.e_commerce.R;
import com.inventics.e_commerce.adapters.ProductDataAdapter;
import com.inventics.e_commerce.modal.Product;

import java.util.ArrayList;

public class ProductListActivity extends AppCompatActivity {
    RecyclerView productRecyclerView;
    ArrayList<Product> data = new ArrayList<>();
    ProductDataAdapter mAdapter;
    FloatingActionButton fab_addProduct;

    DatabaseReference databaseRef;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_productlist);

        fab_addProduct = findViewById(R.id.fab_addProduct);
        productRecyclerView = findViewById(R.id.rv_productList);
        progressBar = findViewById(R.id.progressBar_productList);

        settingUpRecyclerView(productRecyclerView);

        fab_addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentToProductDescriptionActivity();
            }
        });

        gettingProductData();

    }

    private void intentToProductDescriptionActivity() {
        startActivity(new Intent(ProductListActivity.this, AddProductActivity.class));
    }

    private void settingUpRecyclerView(RecyclerView productRecyclerView) {
        productRecyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        productRecyclerView.setLayoutManager(gridLayoutManager);
    }

    private void gettingProductData() {

        databaseRef = FirebaseDatabase.getInstance().getReference().child("Product");

        databaseRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
                /**
                 * Clearing all the data
                 */

                data.clear();
                Log.i("Snapshot", snapshot.toString());
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Product productData = dataSnapshot.getValue(Product.class);
                    //Getting the key of the product node
                    if (productData != null) {
                        productData.setKey(dataSnapshot.getKey());
                    }
                    Log.i("productDataTitle", productData.getTitle());
                    Log.i("productDataDescription", productData.getDescription());
                    Log.i("productDataImage", productData.getImage());
                    data.add(productData);

                }
                mAdapter = new ProductDataAdapter(data, ProductListActivity.this);
                progressBar.setVisibility(View.INVISIBLE);

                productRecyclerView.setAdapter(mAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }


}