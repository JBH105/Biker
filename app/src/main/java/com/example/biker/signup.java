package com.example.biker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Switch;
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
import static com.example.biker.Urls.storeUserInfoInSharedPrefWhileRegister1;
import static com.example.biker.Urls.storeUserInfoInSharedPrefWhileRegister2;

public class signup extends AppCompatActivity {
    TextInputEditText username,email,number;
    TextInputEditText password,copassword;
    Button next;
    Switch aSwitch;
    String name, Email, Number, pas, copas;
    ProgressBar progressBar;

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
        progressBar = findViewById(R.id.progressBar);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = username.getText().toString();
                Email = email.getText().toString();
                Number = number.getText().toString();
                pas = password.getText().toString().trim();
                copas = copassword.getText().toString().trim();

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

                progressBar.setVisibility(View.VISIBLE);
                if (aSwitch.isChecked()) {
                    progressBar.setVisibility(View.GONE);
                    Intent intent = new Intent(getApplicationContext(), signup_address.class);
                    intent.putExtra("username", name);
                    intent.putExtra("email", Email);
                    intent.putExtra("password", pas);
                    intent.putExtra("number", Number);
                    startActivity(intent);
                }
                else
                    usermethod();
            }
        });
    }

    private void usermethod() {

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("username", name);
            jsonBody.put("email", Email);
            jsonBody.put("password", pas);
            jsonBody.put("mobile", Number);
            jsonBody.put("is_servicer", false);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String requestBody = jsonBody.toString();

        StringRequest request = new StringRequest(Request.Method.POST, signup_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                progressBar.setVisibility(View.GONE);
//                Toast.makeText(signup.this, ".. "+response, Toast.LENGTH_SHORT).show();


                try {
                    JSONObject jsonObj = new JSONObject(response.toString());
                    Log.e("Responce", jsonObj.toString());

                    String user_id = jsonObj.getJSONObject("user").getString("id");
                    Log.e("Responce22", user_id);
                    try {
                        storeUserInfoInSharedPrefWhileRegister1(signup.this, new JSONObject(response));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    signupidmethod(user_id);

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
                progressBar.setVisibility(View.GONE);
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

    private void signupidmethod(String user_id) {

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("user", user_id);
//            jsonBody.put("address_fl", first);
//            jsonBody.put("address_sl", second);
//            jsonBody.put("city", City);
//            jsonBody.put("zip", Zip);
            jsonBody.put("mobile", Number);
            jsonBody.put("is_servicer", false);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String requestBody = jsonBody.toString();

        StringRequest request = new StringRequest(Request.Method.POST, signupid_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(signup.this, "..... "+response, Toast.LENGTH_SHORT).show();
//                progressBar.setVisibility(View.GONE);
                try {
                    storeUserInfoInSharedPrefWhileRegister2(signup.this, new JSONObject(response));
                    storeIsLoggedIn(signup.this, true);
                    progressBar.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(signup.this, "Signup Successful!!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), user_home.class));
                finishAffinity();

/*
                try {
                    JSONObject jsonObj = new JSONObject(response.toString());
                    Log.e("Responce", jsonObj.toString());

                    String user_id = jsonObj.getJSONObject("user").getString("id");
                    Log.e("Responce22", user_id);
                    Toast.makeText(signup_address.this, ""+user_id, Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(getApplicationContext(), OTP.class);
                    i.putExtra("User_Id", user_id);
                    startActivity(i);

                Toast.makeText(getApplicationContext(), user_Array.toString(), Toast.LENGTH_LONG).show();

                 looping through All Contacts
                        for (int i = 0; i < user_Array.length(); i++) {
                            JSONObject c = user_Array.getJSONObject(i);
                            String id = c.getString("_id");
                            Toast.makeText(getApplicationContext(), id, Toast.LENGTH_LONG).show();
                        }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
*/


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                if(error.networkResponse.data!=null) {
                    try {
                        String errorMessage = new String(error.networkResponse.data,"UTF-8");
                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "..ERROR: "+error.toString(), Toast.LENGTH_SHORT).show();
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