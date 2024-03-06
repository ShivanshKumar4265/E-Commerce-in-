package com.inventics.e_commerce.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.inventics.e_commerce.activities.EditProfileActivity;
import com.inventics.e_commerce.activities.SignUpActivity;
import com.inventics.e_commerce.databinding.FragmentAccountBinding;

public class AccountFragment extends Fragment {
    View view;
    RelativeLayout logOut;

    TextView tv_logOut;

    CardView editProfile;

    private FragmentAccountBinding binding;
    private DatabaseReference databaseReference;
    String userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAccountBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        handleOnClickListener();

        getUserData();

        return view;
    }

    private void getUserData() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            userId = currentUser.getUid();
            // Now you have the current user's ID (userId)
        } else {
            Toast.makeText(getActivity(), "Something went wrong. .", Toast.LENGTH_SHORT).show();
        }
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.i("snapshot",snapshot.toString());
                binding.userName.setText(snapshot.child("name").getValue().toString());
                binding.userMailId.setText(snapshot.child("email").getValue().toString());
                binding.userContactNumber.setText(snapshot.child("phoneNumber").getValue().toString());

                String imageUrl = snapshot.child("image").getValue().toString();
                Glide.with(requireActivity())
                        .load(imageUrl)
                        .into(binding.circleImageView);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    private void handleOnClickListener() {

        binding.logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                letingUserToLogout();
            }
        });


        binding.editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentToEditProfileActivity();
            }
        });
    }

    private void intentToEditProfileActivity() {

        startActivity(new Intent(getActivity(), EditProfileActivity.class));



    }

    private void letingUserToLogout() {

        FirebaseAuth.getInstance().signOut();
        Log.i("i_user",FirebaseAuth.getInstance().toString());
        startActivity(new Intent(getContext(), SignUpActivity.class));


    }

//    private void initializingViews() {
//
//        logOut = view.findViewById(R.id.logOut);
//        tv_logOut = view.findViewById(R.id.tv_logOut);
//        editProfile = view.findViewById(R.id.editProfile);
//
//    }
}