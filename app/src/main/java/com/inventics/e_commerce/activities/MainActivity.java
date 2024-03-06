package com.inventics.e_commerce.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.inventics.e_commerce.R;
import com.inventics.e_commerce.fragments.AccountFragment;
import com.inventics.e_commerce.fragments.CartFragment;
import com.inventics.e_commerce.fragments.CategoryFragment;
import com.inventics.e_commerce.fragments.ProductListFragment;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;


    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findingAllTheViews();

        checkIfUserCreatedProfile();

        setFragmentOnFirstLaunch();

        settingOnClickListener();
    }

    private void checkIfUserCreatedProfile() {

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
            usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // User profile exists in the database
                        // You can perform further actions here
                        // For example, you can retrieve the profile data using dataSnapshot.getValue()
                    } else {
                        startActivity(new Intent(MainActivity.this,CreateProfileActivity.class));
                        finish();
                        // User profile does not exist in the database
                        // You can prompt the user to create their profile or take appropriate action
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle any errors
                }
            });
        }
    }

    private void setFragmentOnFirstLaunch() {
        replaceFragment(new ProductListFragment());
    }

    private void settingOnClickListener() {

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.homeFragment) {

                    replaceFragment(new ProductListFragment());

                } else if (item.getItemId() == R.id.cartFragment) {

                    replaceFragment(new CartFragment());

                } else if (item.getItemId() == R.id.accountFragment) {

                    replaceFragment(new AccountFragment());
                } else if (item.getItemId() == R.id.categoryFragment) {

                    replaceFragment(new CategoryFragment());

                }
                return true;
            }
        });
    }

    private void findingAllTheViews() {
        bottomNavigationView = findViewById(R.id.bottomNavigation);
    }

    private void replaceFragment(Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainActivityFrameLayout,fragment);
        fragmentTransaction.commit();

    }
}