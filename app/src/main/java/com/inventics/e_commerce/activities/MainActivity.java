package com.inventics.e_commerce.activities;

import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.inventics.e_commerce.R;
import com.inventics.e_commerce.fragments.AccountFragment;
import com.inventics.e_commerce.fragments.CartFragment;
import com.inventics.e_commerce.fragments.CategoryFragment;
import com.inventics.e_commerce.fragments.ProductListFragment;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(Html.fromHtml("<font color='#FFFFFF'>Product List</font>"));
        }

        findingAllTheViews();

        setFragmentOnFirstLaunch();

        settingOnClickListener();
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
//                    getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Product list</font>")); // Set the text color

                } else if (item.getItemId() == R.id.cartFragment) {

                    replaceFragment(new CartFragment());
//                    getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Your Cart</font>"));

                } else if (item.getItemId() == R.id.accountFragment) {

                    replaceFragment(new AccountFragment());
//                    getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Your Account</font>"));

                } else if (item.getItemId() == R.id.categoryFragment) {

                    replaceFragment(new CategoryFragment());
//                    getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>Product Category</font>"));


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