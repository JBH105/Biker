package com.example.biker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;

public class home extends AppCompatActivity {

    DrawerLayout drawerlayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navi;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

//        drawerlayout=findViewById(R.id.drawerlayout);
//        navi=findViewById(R.id.navi);
//        toolbar=findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerlayout,toolbar,R.string.app_name,R.string.app_name);
//        drawerlayout.addDrawerListener(actionBarDrawerToggle);
//        actionBarDrawerToggle.syncState();
//        navi.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//                //switch (menuItem.getItemId()) {
//////                    case R.id.nav_sign_out:
//////                        FirebaseAuth.getInstance().signOut();
//////                        startActivity(new Intent(getApplicationContext(), login.class));
//////                        finish();
////
////                //}
//                return true;
//            }
//        });

//        setUpToolbar();
//
//        //navigation
//        navi = findViewById(R.id.navi);
//        navi.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
////                switch (menuItem.getItemId()) {
////                    case R.id.nav_sign_out:
////                        FirebaseAuth.getInstance().signOut();
////                        startActivity(new Intent(getApplicationContext(), login.class));
////                        finish();
//
//                //}
//                return false;
//            }
//        });
//
//    }
//    private  void  setUpToolbar(){
//        drawerlayout = findViewById(R.id.drawerlayout);
//        toolbar=findViewById(R.id.toolbar);
//        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerlayout,toolbar,R.string.app_name,R.string.app_name);
//        drawerlayout.addDrawerListener(actionBarDrawerToggle);
//        actionBarDrawerToggle.syncState();

    }
}