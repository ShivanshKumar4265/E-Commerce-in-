package com.inventics.e_commerce.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.inventics.e_commerce.R;
import com.inventics.e_commerce.activities.AddProductActivity;
import com.inventics.e_commerce.adapters.ProductDataAdapter;
import com.inventics.e_commerce.modal.Product;

import java.util.ArrayList;

public class ProductListFragment extends Fragment {

    private RecyclerView productRecyclerView;
    private ArrayList<Product> data = new ArrayList<>();
    private ProductDataAdapter mAdapter;
    private FloatingActionButton fab_addProduct;
    private DatabaseReference databaseRef;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);

        fab_addProduct = view.findViewById(R.id.fab_addProduct);
        productRecyclerView = view.findViewById(R.id.rv_productList);
        progressBar = view.findViewById(R.id.progressBar_productList);

        settingUpRecyclerView(productRecyclerView);

        fab_addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentToProductDescriptionActivity();
            }
        });

        gettingProductData();

        return view;
    }

    private void intentToProductDescriptionActivity() {
        startActivity(new Intent(requireActivity(), AddProductActivity.class));
    }

    private void settingUpRecyclerView(RecyclerView productRecyclerView) {
        productRecyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 2);
        productRecyclerView.setLayoutManager(gridLayoutManager);
    }

    private void gettingProductData() {
        databaseRef = FirebaseDatabase.getInstance().getReference().child("Product");

        databaseRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (isAdded() && getActivity() != null) {
                    // Your UI-related operations here
                    progressBar.setVisibility(View.VISIBLE);
                    data.clear();
                    Log.i("Snapshot", snapshot.toString());
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Product productData = dataSnapshot.getValue(Product.class);
                        if (productData != null) {
                            productData.setKey(dataSnapshot.getKey());
                        }
                        Log.i("productDataTitle", productData.getTitle());
                        Log.i("productDataDescription", productData.getDescription());
                        Log.i("productDataImage", productData.getImage());
                        data.add(productData);
                    }
                    mAdapter = new ProductDataAdapter(data, requireContext());
                    progressBar.setVisibility(View.INVISIBLE);
                    productRecyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        });
    }
}


