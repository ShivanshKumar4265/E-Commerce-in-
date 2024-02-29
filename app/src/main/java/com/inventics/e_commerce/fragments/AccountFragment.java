package com.inventics.e_commerce.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.inventics.e_commerce.R;
import com.inventics.e_commerce.activities.SignUpActivity;

public class AccountFragment extends Fragment {
    View view;
    RelativeLayout logOut;

    TextView tv_logOut;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_account, container, false);

        initializingViews();

        handleOnClickListener();

        return  view;
    }

    private void handleOnClickListener() {

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                letingUserToLogout();
            }
        });


        tv_logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                letingUserToLogout();
            }
        });



    }

    private void letingUserToLogout() {

        FirebaseAuth.getInstance().signOut();
        Log.i("i_user",FirebaseAuth.getInstance().toString());
        startActivity(new Intent(getContext(), SignUpActivity.class));


    }

    private void initializingViews() {

        logOut = view.findViewById(R.id.logOut);
        tv_logOut = view.findViewById(R.id.tv_logOut);

    }
}