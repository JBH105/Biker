package com.example.biker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

public class upload_image extends AppCompatActivity {
    ImageView upload_image;
    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_image);

        upload_image=findViewById(R.id.upload_image);
        next=findViewById(R.id.next);
    }
}