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
                        startActivity(new Intent(getApplicationContext(), select_login_signup.class));
                        finish();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }
}