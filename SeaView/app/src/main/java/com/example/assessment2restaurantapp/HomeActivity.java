package com.example.assessment2restaurantapp;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.assessment2restaurantapp.Fragments.BookFragment;
import com.example.assessment2restaurantapp.Fragments.HomeFragment;
import com.example.assessment2restaurantapp.Fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;

    HomeFragment homeFragment = new HomeFragment();
    BookFragment bookFragment = new BookFragment();
    ProfileFragment profileFragment = new ProfileFragment();
    SharedViewModel sharedViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);

        String enteredEmail = getIntent().getStringExtra("enteredEmail");
        sharedViewModel.setUserEmail(enteredEmail);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, homeFragment).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.bottomnav_home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, homeFragment).commit();
        } else if (item.getItemId() == R.id.bottomnav_book) {
            getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, bookFragment).commit();
        } else if (item.getItemId() == R.id.bottomnav_profile) {
            getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, profileFragment).commit();
        }

        return true;
    }
    public void selectBookMenuItem() {
        bottomNavigationView.setSelectedItemId(R.id.bottomnav_book);
    }


}