package com.example.biker.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;

import com.example.biker.R;

public class user_home extends AppCompatActivity {

    private CardView bikeCardView, carCardView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_home);

        bikeCardView = findViewById(R.id.bikeCardView);
        carCardView = findViewById(R.id.carCardView);

    }
}