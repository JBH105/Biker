package com.example.biker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.example.biker.Urls.brand_url;
import static com.example.biker.Urls.signup_url;

public class home extends AppCompatActivity {

    DrawerLayout drawerlayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navi;
    Toolbar toolbar;
    CardView vehiclesCardViewHome;
    String brandName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

//        drawerlayout=findViewById(R.id.drawerlayout);
//        navi=findViewById(R.id.navi);
//        toolbar=findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerlayout,toolbar,R.string.app_name,R.string.app_name);
//        drawerlayout.addDrawerListener(actionBarDrawerToggle);
//        actionBarDrawerToggle.syncState();
//        navi.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//                //switch (menuItem.getItemId()) {
//////                    case R.id.nav_sign_out:
//////                        FirebaseAuth.getInstance().signOut();
//////                        startActivity(new Intent(getApplicationContext(), login.class));
//////                        finish();
////
////                //}
//                return true;
//            }
//        });

//        setUpToolbar();
//
//        //navigation
//        navi = findViewById(R.id.navi);
//        navi.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
////                switch (menuItem.getItemId()) {
////                    case R.id.nav_sign_out:
////                        FirebaseAuth.getInstance().signOut();
////                        startActivity(new Intent(getApplicationContext(), login.class));
////                        finish();
//
//                //}
//                return false;
//            }
//        });
//
//    }
//    private  void  setUpToolbar(){
//        drawerlayout = findViewById(R.id.drawerlayout);
//        toolbar=findViewById(R.id.toolbar);
//        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerlayout,toolbar,R.string.app_name,R.string.app_name);
//        drawerlayout.addDrawerListener(actionBarDrawerToggle);
//        actionBarDrawerToggle.syncState();



        vehiclesCardViewHome = findViewById(R.id.vehiclesCardViewHome);
        vehiclesCardViewHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vehiclesHomeMethod();
            }
        });

    }

    private void vehiclesHomeMethod() {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("brand", brandName);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String requestBody = jsonBody.toString();

        StringRequest request = new StringRequest(Request.Method.POST, brand_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                progressBar.setVisibility(View.GONE);
                Toast.makeText(home.this, ".. "+response, Toast.LENGTH_SHORT).show();


                try {
                    JSONObject jsonObj = new JSONObject(response.toString());
                    Log.e("Responce", jsonObj.toString());

                    String user_id = jsonObj.getString("id");
                    Log.e("Responce22", user_id);
                    modelMethod(user_id);

//                    Intent i = new Intent(getApplicationContext(), OTP.class);
//                    i.putExtra("User_Id", user_id);
//                    startActivity(i);
//
//                    //Toast.makeText(getApplicationContext(), user_Array.toString(), Toast.LENGTH_LONG).show();
//
//                    // looping through All Contacts
//                        for (int i = 0; i < user_Array.length(); i++) {
//                            JSONObject c = user_Array.getJSONObject(i);
//                            String id = c.getString("_id");
//                            Toast.makeText(getApplicationContext(), id, Toast.LENGTH_LONG).show();
//                        }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                progressBar.setVisibility(View.GONE);
//                Toast.makeText(signup.this, "/ ERROR: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                if(error.networkResponse.data!=null) {
                    try {
                        String errorMessage = new String(error.networkResponse.data,"UTF-8");
                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
        ) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                return params;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        request.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 60000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 5;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        requestQueue.add(request);
    }

    private void modelMethod(String user_id) {
    }
}