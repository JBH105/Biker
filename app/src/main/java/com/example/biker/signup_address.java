package com.example.biker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;

public class signup_address extends AppCompatActivity {

    TextInputEditText address_first,address_second,city,zip;
    Button adsignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_address);

        address_first=findViewById(R.id.address_first);
        address_second=findViewById(R.id.address_second);
        city=findViewById(R.id.city);
        zip=findViewById(R.id.zip);
        adsignup=findViewById(R.id.adsignup);

        adsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String first = address_first.getText().toString().trim();
                String second = address_second.getText().toString().trim();
                String City = city.getText().toString().trim();
                String Zip = zip.getText().toString().trim();

                if (first.isEmpty()){
                    address_first.setError("Address is Requird");
                    return;
                }
                if (second.isEmpty()){
                    address_second.setError("Address is Requird");
                    return;
                }
                if (City.isEmpty()){
                    city.setError("City is Requird");
                    return;
                }
                if (Zip.isEmpty()){
                    zip.setError("Zip code is Requird");
                    return;
                }
                if (Zip.length()<6){
                    zip.setError("Zip code must be at least 6 characters");
                    return;
                }
                startActivity(new Intent(getApplicationContext(),signup_password.class));
            }
        });
    }
}