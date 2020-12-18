package com.example.biker.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.drawable.DrawableWrapper;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.widget.Toolbar;


import com.example.biker.R;
import com.example.biker.bike_service;
import com.google.android.material.navigation.NavigationView;

public class user_home extends AppCompatActivity {

    private CardView bikeCardView, carCardView;
   DrawerLayout drawerlayout;
   Toolbar toolbar;
   ActionBarDrawerToggle actionBarDrawerToggle;
   NavigationView navigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_home);

        bikeCardView = findViewById(R.id.bikeCardView);
        carCardView = findViewById(R.id.carCardView);

        drawerlayout=findViewById(R.id.drawerlayout);
        toolbar=findViewById(R.id.toolbar);
        navigation=findViewById(R.id.navigation);
        setSupportActionBar(toolbar);

        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerlayout,toolbar,R.string.app_name,R.string.app_name);
        drawerlayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
//                    case R.id.nav_sign_out:
//                        FirebaseAuth.getInstance().signOut();
//                        startActivity(new Intent(getApplicationContext(), login.class));
//                        finish();

                }
                return true;
            }
        });

        bikeCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), bike_service.class));
            }
        });
        carCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),bike_service.class));
            }
        });

    }
}