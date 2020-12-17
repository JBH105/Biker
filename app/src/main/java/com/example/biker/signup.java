package com.example.biker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class signup extends AppCompatActivity {
    TextInputEditText username,email,number;
    TextInputEditText password,copassword;
    Button next;
    Switch aSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        number = findViewById(R.id.number);
        next = findViewById(R.id.next);
        password=findViewById(R.id.password);
        copassword=findViewById(R.id.copassword);
        aSwitch = findViewById(R.id.signupSwitch);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = username.getText().toString();
                String Email = email.getText().toString();
                String Number = number.getText().toString();
                String pas = password.getText().toString().trim();
                String copas = copassword.getText().toString().trim();

                if (name.isEmpty()){
                    username.setError("Username is Requird");
                    return;
                }
                if (Email.isEmpty()){
                    email.setError("Email is Requird");
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches())
                {
                    Toast.makeText(getApplicationContext(),"Enter Valid Email address",Toast.LENGTH_LONG).show();
                    return;
                }
                if (Number.isEmpty()|| number.length()<10){
                    number.setError("Number is Requird");
                    return;
                }
                if (pas.isEmpty()){
                    password.setError("Password is Requird");
                    return;
                }
                if (pas.length()<8)
                {
                    password.setError("Password must be atleast 8 characters");
                    return;
                }

                if (copas.isEmpty()){
                    copassword.setError("Password is Requird");
                    return;
                }

                if (!pas.equals(copas)) {
                    copassword.setError("Password and Confirm Password does NOT match!!");
                    return;
                }

                if (aSwitch.isChecked())
                    startActivity(new Intent(getApplicationContext(),signup_address.class));
                else
                    startActivity(new Intent(getApplicationContext(),home.class));
            }
        });
    }
}