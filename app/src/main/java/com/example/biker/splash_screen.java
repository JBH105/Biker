package com.example.biker;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.biker.garageuser.home;
import com.example.biker.user.user_home;

import static com.example.biker.Urls.getIsLoggedIn;
import static com.example.biker.Urls.getIsServicer;


public class splash_screen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getIsLoggedIn(splash_screen.this)) {
                    if (getIsServicer(splash_screen.this))
                        startActivity(new Intent(getApplicationContext(), home.class));
                    else
                        startActivity(new Intent(getApplicationContext(), user_home.class));
                } else
                    startActivity(new Intent(getApplicationContext(),login.class));
                finish();
            }
        },4000);
    }
}