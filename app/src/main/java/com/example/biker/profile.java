package com.example.biker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.biker.garageuser.home;

import static com.example.biker.Urls.getAddress_fl;
import static com.example.biker.Urls.getAddress_sl;
import static com.example.biker.Urls.getCity;
import static com.example.biker.Urls.getEmail;
import static com.example.biker.Urls.getMobile;
import static com.example.biker.Urls.getUsername;
import static com.example.biker.Urls.getZip;
import static com.example.biker.Urls.storeIsLoggedIn;

public class profile extends AppCompatActivity {
    TextView profile_name,profile_email,profile_number,profile_address,profile_zip;
    ImageView logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        profile_name=findViewById(R.id.profile_name);
        profile_email=findViewById(R.id.profile_email);
        profile_number=findViewById(R.id.profile_number);
        profile_address=findViewById(R.id.profile_address);
        profile_zip=findViewById(R.id.profile_zip);
        logout=findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                storeIsLoggedIn(home.this, false);
//                        startActivity(new Intent(getApplicationContext(), select_login_signup.class));
//                        finish();
                //dialogbox

                final AlertDialog.Builder builder = new AlertDialog.Builder(profile.this);
                builder.setMessage("Are you sure?");
                builder.setCancelable(true);
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //logout
                        storeIsLoggedIn(profile.this, false);
                        startActivity(new Intent(getApplicationContext(), login.class));
                        finish();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        // get data to show in profile
        getDataOfLoggedInMethod();

    }

    private void getDataOfLoggedInMethod() {

        if (getUsername(profile.this)!=null || getUsername(profile.this).equals("error")) {
            profile_name.setText("Username: \t"+getUsername(profile.this));
            profile_name.setVisibility(View.VISIBLE);
        } else {
            profile_name.setVisibility(View.GONE);
        }

        if (getEmail(profile.this)!=null || getEmail(profile.this).equals("error")) {
            profile_email.setText("Email: \t"+getEmail(profile.this));
            profile_email.setVisibility(View.VISIBLE);
        } else {
            profile_email.setVisibility(View.GONE);
        }

        if (getMobile(profile.this)!=null || getMobile(profile.this).equals("error")) {
            profile_number.setText("Mobile Number: \t"+getMobile(profile.this));
            profile_number.setVisibility(View.VISIBLE);
        } else {
            profile_number.setVisibility(View.GONE);
        }

        String address = new String();
        if (getAddress_fl(profile.this)!=null || getAddress_fl(profile.this).equals("error")) {
            address = getAddress_fl(profile.this);
        }
        if (getAddress_sl(profile.this)!=null || getAddress_sl(profile.this).equals("error")) {
            address = address + ", "+ getAddress_sl(profile.this);
        }
        if (getCity(profile.this)!=null || getCity(profile.this).equals("error")) {
            address = address + ",\n "+ getCity(profile.this);
        }
        if (getAddress_fl(profile.this)!=null || getAddress_fl(profile.this).equals("error")) {
            profile_name.setText("Address: \t"+address);
            profile_name.setVisibility(View.VISIBLE);
        } else {
            profile_name.setVisibility(View.GONE);
        }

        if (getZip(profile.this)!=null || getZip(profile.this).equals("error")) {
            profile_zip.setText("Zip: \t"+getZip(profile.this));
            profile_zip.setVisibility(View.VISIBLE);
        } else {
            profile_zip.setVisibility(View.GONE);
        }

    }
}