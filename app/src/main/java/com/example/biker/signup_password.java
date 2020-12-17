package com.example.biker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.biker.R;
import com.google.android.material.textfield.TextInputEditText;

public class signup_password extends AppCompatActivity {
    TextInputEditText password,copassword;
    Button pasignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_password);

        password = findViewById(R.id.password);
        copassword = findViewById(R.id.copassword);
        pasignup = findViewById(R.id.pasignup);

        pasignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pas = password.getText().toString();
                String copas = copassword.getText().toString();

                if (pas.isEmpty()){
                    password.setError("Password is Requird");
                    return;
                }
                if (pas.length()<8)
                {
                    password.setError("Password must be at least 8 characters");
                    return;
                }

                if (copas.isEmpty()){
                    copassword.setError("Password is Requird");
                    return;
                }
                startActivity(new Intent(getApplicationContext(),home.class));
            }
        });
    }
}