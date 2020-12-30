package com.example.biker;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import static com.example.biker.Urls.find_servicer_url;
import static com.example.biker.Urls.getUsername;

public class Nav_header extends AppCompatActivity {

    TextView Name,profile_name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_header);

        profile_name=findViewById(R.id.profile_name);
        Name = findViewById(R.id.txt_drawer_name);
        String name = getIntent().getExtras().getString("Name");

        Name.setText(name);
        Name.setBackgroundColor(Color.WHITE);
        getDataOfLoggedInMethod();
    }
    private void getDataOfLoggedInMethod() {

        try {
            if (getUsername(Nav_header.this) != null && !getUsername(Nav_header.this).equals("null")) {
                profile_name.setText("Username: \t" + getUsername(Nav_header.this));
                Log.e("123" ,"Username");
                profile_name.setVisibility(View.VISIBLE);
            } else {
                profile_name.setVisibility(View.GONE);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
