package com.inventics.e_commerce.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.inventics.e_commerce.R;
public class CategoryFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (getActivity() != null && getActivity().getActionBar() != null) {
            getActivity().getActionBar().setTitle("Account");
            Log.i("TitleName",getActivity().getActionBar().getTitle().toString());
        }
        return inflater.inflate(R.layout.fragment_category, container, false);
    }
}