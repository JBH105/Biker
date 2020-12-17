package com.example.biker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
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
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class signup extends AppCompatActivity {
    TextInputEditText username,email,number;
    TextInputEditText password,copassword;
    Button next;
    Switch aSwitch;
    String name, Email, Number, pas, copas;

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

                if (aSwitch.isChecked()) {
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

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String requestBody = jsonBody.toString();

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                progressBar.setVisibility(View.GONE);

//                    if (response.equalsIgnoreCase("Registered Successfully!")) {
//                        fullname.setText("");
//                        email.setText("");
//                        password.setText("");
//                        number.setText("");
//                        city.setText("");
//                        zipcode.setText("");
//                        Toast.makeText(user_register.this, response, Toast.LENGTH_SHORT).show();
//
//                    }
//                    /*else if (response.equalsIgnoreCase("A User Already Exist with this Email ID!")) {
//                        password.setText("");
//                        number.setText("");
//                        city.setText("");
//                        zipcode.setText("");
//                        Toast.makeText(user_register.this, response, Toast.LENGTH_SHORT).show();
//                    }*/
//                    else {
//                        password.setText("");
//                        number.setText("");
//                        city.setText("");
//                        zipcode.setText("");
//                        Toast.makeText(user_register.this, response.toString(), Toast.LENGTH_SHORT).show();
//                    }

//
                try {
                    JSONObject jsonObj = new JSONObject(response.toString());
                    Log.e("Responce", jsonObj.toString());

                    String user_id = jsonObj.getJSONObject("user").getString("_id");
                    Log.e("Responce22", user_id.toString());

                    Intent i = new Intent(getApplicationContext(), OTP.class);
                    i.putExtra("User_Id", user_id);
                    startActivity(i);

                    //Toast.makeText(getApplicationContext(), user_Array.toString(), Toast.LENGTH_LONG).show();

                    // looping through All Contacts
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
//                @Override
//                protected Map<String, String> getParams() throws AuthFailureError {
//                    Map<String, String> params = new HashMap<String, String>();
//                    params.put("username", str_fullname);
//                    params.put("email", str_email);
//                    params.put("password", str_password);
//                    params.put("mobile", str_number);
//                    params.put("city", str_city);
//                    params.put("zip", str_zipcode);
//                    return params;
//                }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                return params;
            }

//                @Override
//                public String getBodyContentType() {
//                    return "application/json; charset=utf-8";
//                }

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
}