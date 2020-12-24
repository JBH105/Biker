package com.example.biker;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.biker.Urls.brand_url;
import static com.example.biker.Urls.model_url;
import static com.example.biker.user.user_service_data.storeModelBrandData;
import static com.example.biker.user.user_service_data.storeVehicleData;

public class bike_service extends AppCompatActivity {
    Spinner spinner, spinner1;
    TextInputLayout remarkInputLayout;
    TextInputEditText plate_number,remark;
    Button next;
    ArrayAdapter<String> modelAdapter;
    Map<String, List<String>> modelmap = new HashMap<>();
    Map<String, String> brandmap = new HashMap<>();
    ProgressBar progressBar;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bike_service);

        spinner=findViewById(R.id.spinner);
        plate_number=findViewById(R.id.plate_number);
        spinner1=findViewById(R.id.spinner1);
        remarkInputLayout=findViewById(R.id.remarkInputLayout);
        remark=findViewById(R.id.remark);
        next=findViewById(R.id.next);
        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.VISIBLE);
        getModelList();
        new Thread() {
            @Override
            public void run() {
                getBrandItem(bike_service.this);
            }
        }.start();

        // Problem Spinner ArrayAdapter
        final ArrayAdapter<CharSequence> Adapter=ArrayAdapter.createFromResource(getApplicationContext(),R.array.bike_problems,android.R.layout.select_dialog_item);
        spinner1.setAdapter(Adapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(bike_service.this, ""+position, Toast.LENGTH_SHORT).show();
                if (Adapter.getItem(position).equals("Others")) {
                    remarkInputLayout.setVisibility(View.VISIBLE);
//                    Toast.makeText(bike_service.this, "Others...", Toast.LENGTH_SHORT).show();
                } else {
                    remarkInputLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Model SpinnerArrayAdapter
        ArrayList<String> molist = new ArrayList<String>();
        molist.add("Select Model");
        modelAdapter = new ArrayAdapter<String>(bike_service.this, android.R.layout.select_dialog_item, molist);
        modelAdapter.setDropDownViewResource(android.R.layout.select_dialog_item);
        spinner.setAdapter(modelAdapter);

        //spinner
//        ArrayAdapter<CharSequence> Adapter=ArrayAdapter.createFromResource(getApplicationContext(),R.array.bike,R.layout.support_simple_spinner_dropdown_item);
//        spinner.setAdapter(Adapter);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String number = plate_number.getText().toString().trim();
                final String Remark = remark.getText().toString().trim();
                final String Spinner = spinner.getSelectedItem().toString().trim();
                String Spinner1 = spinner1.getSelectedItem().toString().trim();

                if (Spinner.equals("Select Model")){
                    Toast.makeText(bike_service.this, "Select Model", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (number.isEmpty()){
                    plate_number.setError("Enter plate number");
                    return;
                }
                if (Spinner1.equals("Select Problem")){
                    Toast.makeText(bike_service.this, "Select Problem", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Spinner1.equals("Others"))
                    if (Remark.isEmpty()){
                        remark.setError("Enter Remark");
                        return;
                    }
                // get Brand from brand_id in model_url response
//                getBrandItem(bike_service.this);
//                Toast.makeText(bike_service.this, "Selected Item: "+spinner1.getSelectedItem(), Toast.LENGTH_SHORT).show();

                progressBar.setVisibility(View.VISIBLE);
                if (!modelmap.isEmpty() && !brandmap.isEmpty()) {
                    new Thread() {
                        @Override
                        public void run() {
                            storeModelBrandData(modelmap,Spinner,brandmap);
                            if (spinner1.getSelectedItem().equals("Others"))
                                storeVehicleData(number,Remark);
                            else
                                storeVehicleData(number,spinner1.getSelectedItem().toString());
                        }
                    }.start();
                } else {
                    Toast.makeText(bike_service.this, "Error Storing Vehicle Service Data", Toast.LENGTH_SHORT).show();
                }

                progressBar.setVisibility(View.GONE);
                startActivity(new Intent(getApplicationContext(), bike_service_location.class));
        }
        });

    }

    private void getModelList() {
        StringRequest request = new StringRequest(Request.Method.GET, model_url, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                progressBar.setVisibility(View.GONE);
//                Toast.makeText(bike_service.this, ".. "+response, Toast.LENGTH_SHORT).show();

                new Thread() {
                    @Override
                    public void run() {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            Log.e("Responce", jsonArray.toString());

                            for (int i=0;i<jsonArray.length();i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                List<String> list = new ArrayList<String>();
                                list.add(jsonObject.getString("id"));
                                list.add(jsonObject.getString("brand"));
                                modelmap.put(jsonObject.getString("model_name"),list);
                            }
                            Log.e("kk", modelmap.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();

                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            Log.e("Responce", jsonArray.toString());

                            List<String> modellist = new ArrayList<String>();
//                            modellist.add("Select Model");
                            for (int i=0;i<jsonArray.length();i++) {
                                modellist.add(jsonArray.getJSONObject(i).getString("model_name"));
                            }

//                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(bike_service.this, android.R.layout.select_dialog_item, modellist);
//                            adapter.setDropDownViewResource(android.R.layout.select_dialog_item);
//                            spinner.setAdapter(adapter);
                            modelAdapter.addAll(modellist);
                            modelAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        }
                };
                runOnUiThread(runnable);
//                new Thread(runnable).start();

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


    public void getBrandItem(Context context) {
        StringRequest request = new StringRequest(Request.Method.GET, brand_url, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
//                progressBar.setVisibility(View.GONE);
//                Toast.makeText(bike_service.this, ".. "+response, Toast.LENGTH_SHORT).show();

                new Thread() {
                    @Override
                    public void run() {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            Log.e("Brand Responce", jsonArray.toString());

                            for (int i=0;i<jsonArray.length();i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                brandmap.put(jsonObject.getString("id"),jsonObject.getString("brand"));
                            }
                            Log.e("kk", brandmap.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                progressBar.setVisibility(View.GONE);
//                Toast.makeText(signup.this, "/ ERROR: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                if(error.networkResponse.data!=null) {
                    try {
                        String errorMessage = new String(error.networkResponse.data,"UTF-8");
//                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        Log.i("Brand Error",errorMessage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else {
                    try {
//                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.i("Brand Error",error.getMessage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);

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