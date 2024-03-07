package com.inventics.e_commerce.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.inventics.e_commerce.R;
import com.inventics.e_commerce.adapters.CartItemAdapter;
import com.inventics.e_commerce.modal.Product;
import com.inventics.e_commerce.modal.cart;

import java.util.ArrayList;

public class CartFragment extends Fragment {

    View view;

    private RecyclerView cartRecyclerView;
    private ArrayList<Product> data = new ArrayList<>();
    private CartItemAdapter mAdapter;
//    private ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    String userID;

    private DatabaseReference databaseRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_card, container, false);
         initializeViews();

        settingUpRecyclerView(cartRecyclerView);
        getCartData();

         return view;
    }

    private void getCartData() {
        databaseRef = FirebaseDatabase.getInstance().getReference().child("cart").child(userID);
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot Snapshot:snapshot.getChildren()){

                    cart cart = Snapshot.getValue(cart.class);
                    String key = cart.getPro_key();
                    int qty = cart.getQty();
                    gettingProductData(key,qty);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void gettingProductData(String key, int qty) {


        databaseRef = FirebaseDatabase.getInstance().getReference().child("Product");


        databaseRef.child(key).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (isAdded() && getActivity() != null) {
                    // Your UI-related operations here
//                    progressBar.setVisibility(View.VISIBLE);
                    Log.i("Snapshot", snapshot.toString());

                        Product productData = snapshot.getValue(Product.class);
                        if (productData != null) {
                            productData.setQty(qty);
                        }
                        Log.i("productDataTitle", productData.getTitle());
                        Log.i("productDataDescription", productData.getDescription());
                        Log.i("productDataImage", productData.getImage());
                        data.add(productData);

                    mAdapter = new CartItemAdapter(requireContext(),data);
//                    progressBar.setVisibility(View.INVISIBLE);
                    cartRecyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        });
    }



    private void settingUpRecyclerView(RecyclerView cartRecyclerView) {

        cartRecyclerView.setHasFixedSize(true);
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(requireContext());
        cartRecyclerView.setLayoutManager(gridLayoutManager);

    }

    private void initializeViews() {
        cartRecyclerView = view.findViewById(R.id.cartItem);
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}