package com.example.biker.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.biker.R;
import com.example.biker.bike_service;

public class user_home extends AppCompatActivity {

    private CardView bikeCardView, carCardView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_home);

        bikeCardView = findViewById(R.id.bikeCardView);
        carCardView = findViewById(R.id.carCardView);

        bikeCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), bike_service.class));
            }
        });

    }
}