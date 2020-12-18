package com.example.biker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.biker.garageuser.home;
import com.example.biker.user.user_home;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.example.biker.Urls.signup_url;
import static com.example.biker.Urls.signupid_url;
import static com.example.biker.Urls.storeIsLoggedIn;
import static com.example.biker.Urls.storeUserInfoInSharedPref;

public class signup_address extends AppCompatActivity {

    TextInputEditText address_first,address_second,city,zip;
    Button adsignup;
    String name, email, pas, Number, first, second, City, Zip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_address);

        address_first=findViewById(R.id.address_first);
        address_second=findViewById(R.id.address_second);
        city=findViewById(R.id.city);
        zip=findViewById(R.id.zip);
        adsignup=findViewById(R.id.adsignup);

        Intent intent = getIntent();
        name = intent.getStringExtra("username");
        email = intent.getStringExtra("email");
        pas = intent.getStringExtra("password");
        Number = intent.getStringExtra("number");



        adsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                first = address_first.getText().toString().trim();
                second = address_second.getText().toString().trim();
                City = city.getText().toString().trim();
                Zip = zip.getText().toString().trim();

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
                usermethod();
                //startActivity(new Intent(getApplicationContext(),signup_password.class));
            }
        });
    }

    private void usermethod() {

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("username", name);
            jsonBody.put("email", email);
            jsonBody.put("password", pas);
            jsonBody.put("mobile", Number);
            jsonBody.put("address_fl", first);
            jsonBody.put("address_sl", second);
            jsonBody.put("city", City);
            jsonBody.put("zip", Zip);
            jsonBody.put("is_servicer", true);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String requestBody = jsonBody.toString();

        StringRequest request = new StringRequest(Request.Method.POST, signup_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(signup_address.this, ""+response, Toast.LENGTH_SHORT).show();
//                progressBar.setVisibility(View.GONE);

                try {
                    JSONObject jsonObj = new JSONObject(response.toString());
                    Log.e("Responce", jsonObj.toString());

                    String user_id = jsonObj.getJSONObject("user").getString("id");
                    Log.e("Responce22", user_id);
                    Toast.makeText(signup_address.this, ""+user_id, Toast.LENGTH_SHORT).show();
                    signupidmethod(user_id);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                progressBar.setVisibility(View.GONE);
                if(error.networkResponse.data!=null) {
                    try {
                        String errorMessage = new String(error.networkResponse.data,"UTF-8");
                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "ERROR: "+error.toString(), Toast.LENGTH_SHORT).show();
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

    private void signupidmethod(String user_id) {

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("user", user_id);
            jsonBody.put("address_fl", first);
            jsonBody.put("address_sl", second);
            jsonBody.put("city", City);
            jsonBody.put("zip", Zip);
            jsonBody.put("mobile", Number);
            jsonBody.put("is_servicer", true);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String requestBody = jsonBody.toString();

        StringRequest request = new StringRequest(Request.Method.POST, signupid_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(signup_address.this, ""+response, Toast.LENGTH_SHORT).show();
//                progressBar.setVisibility(View.GONE);
                try {
                    storeUserInfoInSharedPref(signup_address.this, new JSONObject(response));
                    storeIsLoggedIn(signup_address.this, true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(new Intent(getApplicationContext(), home.class));

//                try {
//                    JSONObject jsonObj = new JSONObject(response.toString());
//                    Log.e("Responce", jsonObj.toString());
//
//                    String user_id = jsonObj.getJSONObject("user").getString("id");
//                    Log.e("Responce22", user_id);
//                    Toast.makeText(signup_address.this, ""+user_id, Toast.LENGTH_SHORT).show();

//                    Intent i = new Intent(getApplicationContext(), OTP.class);
//                    i.putExtra("User_Id", user_id);
//                    startActivity(i);

                    //Toast.makeText(getApplicationContext(), user_Array.toString(), Toast.LENGTH_LONG).show();

                    // looping through All Contacts
//                        for (int i = 0; i < user_Array.length(); i++) {
//                            JSONObject c = user_Array.getJSONObject(i);
//                            String id = c.getString("_id");
//                            Toast.makeText(getApplicationContext(), id, Toast.LENGTH_LONG).show();
//                        }

//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                progressBar.setVisibility(View.GONE);
                if(error.networkResponse.data!=null) {
                    try {
                        String errorMessage = new String(error.networkResponse.data,"UTF-8");
                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "ERROR: "+error.toString(), Toast.LENGTH_SHORT).show();
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
}