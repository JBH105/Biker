package com.example.biker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;

public class login extends AppCompatActivity {
TextInputEditText username,password;
Button loginbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        loginbtn=findViewById(R.id.loginbtn);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=username.getText().toString().trim();
                String pass = password.getText().toString().trim();

                if (name.isEmpty())
                {
                    username.setError("Username is Requird");
                    return;
                }

                if (pass.isEmpty())
                {
                    password.setError("Password is Requird");
                    return;
                }
                if (pass.length()<8)
                {
                    password.setError("Password must be at least 8 characters");
                    return;
                }
              startActivity(new Intent(getApplicationContext(),home.class));

            }
        });
    }
}