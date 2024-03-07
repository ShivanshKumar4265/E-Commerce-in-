package com.inventics.e_commerce.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.inventics.e_commerce.adapters.CategoryAdapter;
import com.inventics.e_commerce.databinding.FragmentCategoryBinding;
import com.inventics.e_commerce.modal.categories;

import java.util.ArrayList;

public class CategoryFragment extends Fragment {

    FragmentCategoryBinding binding;
    private DatabaseReference databaseRef;
    private ArrayList<categories> data = new ArrayList<>();

    RecyclerView recyclerView;

    private CategoryAdapter mAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCategoryBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        getProductCategory();
        handleOnClickListener();

        return view;
    }

    private void getProductCategory() {
        databaseRef = FirebaseDatabase.getInstance().getReference().child("categories");
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot Snapshot: snapshot.getChildren()) {
                    Log.i("snapshot",Snapshot.toString());
                    //here data get into the setter of the class, that's y we are passing "categories.class" as an argument
                    categories cat = Snapshot.getValue(categories.class);
                    Log.i("cat",cat.toString());
                    data.add(cat);
                    Log.i("data",data.toString());
                    mAdapter = new CategoryAdapter(getContext(),data);
                    binding.rvCategories.setAdapter(mAdapter);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void handleOnClickListener() {

    }
}