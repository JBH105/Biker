package com.example.biker;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Nav_header extends AppCompatActivity {

    TextView Name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_header);

        Name = findViewById(R.id.txt_drawer_name);
        String name = getIntent().getExtras().getString("Name");

        Name.setText(name);
        Name.setBackgroundColor(Color.WHITE);

    }
}
