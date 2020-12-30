package com.example.biker.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;


import com.example.biker.Nav_header;
import com.example.biker.R;
import com.example.biker.bike_service;
import com.example.biker.list_user_service;
import com.example.biker.login;
import com.example.biker.profile;
import com.example.biker.select_login_signup;
import com.google.android.material.navigation.NavigationView;

import static com.example.biker.Urls.getUsername;
import static com.example.biker.Urls.storeIsLoggedIn;

public class user_home extends AppCompatActivity {

    private CardView bikeCardView, serviceCardView;
   DrawerLayout drawerlayout;
   Toolbar toolbar;
   ActionBarDrawerToggle actionBarDrawerToggle;
   NavigationView navigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_home);

        bikeCardView = findViewById(R.id.bikeCardView);
        serviceCardView = findViewById(R.id.serviceCardView);

        drawerlayout=findViewById(R.id.drawerlayout);
        toolbar=findViewById(R.id.toolbar);
        navigation=findViewById(R.id.navigation);
        setSupportActionBar(toolbar);

        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerlayout,toolbar,R.string.app_name,R.string.app_name);
        drawerlayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        View headerView = navigation.getHeaderView(0);
        TextView profile_name = headerView.findViewById(R.id.profile_name_header);
        try {
            if (getUsername(user_home.this) != null && !getUsername(user_home.this).equals("null")) {
                profile_name.setText("Username: \t" + getUsername(user_home.this));
                Log.e("123" ,"Username");
                profile_name.setVisibility(View.VISIBLE);
            } else {
                profile_name.setVisibility(View.GONE);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_settings_logout:
//                        storeIsLoggedIn(user_home.this, false);
//                        startActivity(new Intent(getApplicationContext(), select_login_signup.class));
//                        finish();
                        final AlertDialog.Builder builder = new AlertDialog.Builder(user_home.this);
                        builder.setMessage("Are you sure?");
                        builder.setCancelable(true);
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //logout
                                storeIsLoggedIn(user_home.this, false);
                                startActivity(new Intent(getApplicationContext(), login.class));
                                finish();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        break;

                    case R.id.nav_settings_profile:
                        if (drawerlayout.isDrawerOpen(navigation))
                            drawerlayout.closeDrawer(navigation);
                        startActivity(new Intent(getApplicationContext(), profile.class));
                        break;
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
        serviceCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), list_user_service.class));
            }
        });

    }
}