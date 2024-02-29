package com.inventics.e_commerce.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.inventics.e_commerce.R;

public class AccountFragment extends Fragment {
    View view;
//    CircularImageView circleImageView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_account, container, false);

//        circleImageView=view.findViewById(R.id.circleImageView);

//        circleImageView.setScaleType(CircleImageView.ScaleType.CENTER);


        return  view;
    }
}