package com.inventics.e_commerce.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.inventics.e_commerce.databinding.FragmentCategoryBinding;

public class CategoryFragment extends Fragment {

    FragmentCategoryBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCategoryBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        handleOnClickListener();

        return view;
    }

    private void handleOnClickListener() {

    }
}