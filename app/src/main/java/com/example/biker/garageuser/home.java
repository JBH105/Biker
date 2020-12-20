package com.example.biker.garageuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import android.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.biker.R;
import com.example.biker.add_vehicles;
import com.example.biker.select_login_signup;
import com.example.biker.user.user_home;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.example.biker.Urls.brand_url;
import static com.example.biker.Urls.storeIsLoggedIn;

public class home extends AppCompatActivity {

    DrawerLayout drawerlayout;
    //ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navi;
    Toolbar toolbar;
    CardView vehiclesCardViewHome,service,profile;
    String brandName;
    ImageView logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        drawerlayout=findViewById(R.id.drawerlayout);
        navi=findViewById(R.id.navigation);
        toolbar=findViewById(R.id.toolbar);

        //logout
        logout=findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                storeIsLoggedIn(home.this, false);
//                        startActivity(new Intent(getApplicationContext(), select_login_signup.class));
//                        finish();
                //dialogbox

                    final AlertDialog.Builder builder = new AlertDialog.Builder(home.this);
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
                            storeIsLoggedIn(home.this, false);
                            startActivity(new Intent(getApplicationContext(), select_login_signup.class));
                            finish();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
        });

        //navigationview
        //setSupportActionBar(toolbar);
//
//        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerlayout,toolbar,R.string.app_name,R.string.app_name);
//        drawerlayout.addDrawerListener(actionBarDrawerToggle);
//        actionBarDrawerToggle.syncState();
//        navi.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//                switch (menuItem.getItemId()) {
//                    case R.id.nav_settings_logout:
//                        storeIsLoggedIn(home.this, false);
//                        startActivity(new Intent(getApplicationContext(), select_login_signup.class));
//                        finish();
//                        break;
//
//                }
//                return true;
//            }
//        });

        vehiclesCardViewHome = findViewById(R.id.vehiclesCardViewHome);
        vehiclesCardViewHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), add_vehicles.class));
            }
        });

    }

}