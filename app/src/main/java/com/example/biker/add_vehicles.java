package com.example.biker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.biker.Urls.brand_url;
import static com.example.biker.Urls.getAccountId;
import static com.example.biker.Urls.getIsServicer;
import static com.example.biker.Urls.getUsername;
import static com.example.biker.Urls.model_url;
import static com.example.biker.Urls.servicer_add_vehicle_url;
import static com.example.biker.Urls.signin_url;
import static com.example.biker.Urls.storeIsLoggedIn;
import static com.example.biker.Urls.storeUserInfoInSharedPref;
import static com.example.biker.Urls.vehicle_url;
import static com.example.biker.user.user_service_data.storeModelBrandData;
import static com.example.biker.user.user_service_data.storeVehicleData;

public class add_vehicles extends AppCompatActivity {
    Spinner spinner;
    Button addbtn;
    ArrayAdapter<String> modelAdapter;
    Map<String, List<String>> modelmap = new HashMap<>();
    ScrollView addVehicleListScrollView;
    TextView noVehiclesTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_vehicles);

        spinner = findViewById(R.id.spinner);
        addbtn = findViewById(R.id.addbtn);
        addVehicleListScrollView = findViewById(R.id.addVehicleListScrollView);
        noVehiclesTextView = findViewById(R.id.noVehiclesTextView);

        getModelList();
        new Thread() {
            @Override
            public void run() {
                getVehicleListMethod();
            }
        }.start();

        // Model SpinnerArrayAdapter
        ArrayList<String> molist = new ArrayList<String>();
        molist.add("Select Model");
        modelAdapter = new ArrayAdapter<String>(add_vehicles.this, android.R.layout.select_dialog_item, molist);
        modelAdapter.setDropDownViewResource(android.R.layout.select_dialog_item);
        spinner.setAdapter(modelAdapter);

        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Spinner = spinner.getSelectedItem().toString().trim();

                if (Spinner.equals("Select Model")){
                    Toast.makeText(add_vehicles.this, "Select Model", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!modelmap.isEmpty()) {
                    new Thread() {
                        @Override
                        public void run() {
                            addVehicleServicerMethod(modelmap.get(spinner.getSelectedItem()).get(0));
                        }
                    }.start();
                } else {
                    Log.e("kk", "Model Data NOT Stored!");
                }

            }
        });



    }

    private void getModelList() {
        StringRequest request = new StringRequest(Request.Method.GET, model_url, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
//                progressBar.setVisibility(View.GONE);
                Toast.makeText(add_vehicles.this, ".. "+response, Toast.LENGTH_SHORT).show();

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
                            for (int i=0;i<jsonArray.length();i++) {
                                modellist.add(jsonArray.getJSONObject(i).getString("model_name"));
                            }

                            modelAdapter.addAll(modellist);
                            modelAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };
                runOnUiThread(runnable);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                progressBar.setVisibility(View.GONE);
//                Toast.makeText(add_vehicles.this, "/ ERROR: "+error.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void addVehicleServicerMethod(String modelid) {

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("user", getAccountId(add_vehicles.this));
            jsonBody.put("model_fk", modelid);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String requestBody = jsonBody.toString();

        StringRequest request = new StringRequest(Request.Method.POST, servicer_add_vehicle_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(login.this, ""+response, Toast.LENGTH_SHORT).show();
//                progressBar.setVisibility(View.GONE);
                try {
                    Toast.makeText(add_vehicles.this, "Vehicle Successfully Added!!", Toast.LENGTH_SHORT).show();
                    new Thread() {
                        @Override
                        public void run() {
                            getVehicleListMethod();
                        }
                    }.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                startActivity(new Intent(getApplicationContext(), user_home.class));

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

    private void getVehicleListMethod() {

        String servicer_vehicle_url = servicer_add_vehicle_url + getUsername(add_vehicles.this);
        StringRequest request = new StringRequest(Request.Method.GET, servicer_vehicle_url, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                Toast.makeText(add_vehicles.this, ""+response, Toast.LENGTH_SHORT).show();
//                progressBar.setVisibility(View.GONE);
                try {
                    if (new JSONObject(response).getString("").equals("")) {
                        addVehicleListScrollView.setVisibility(View.VISIBLE);
                        noVehiclesTextView.setVisibility(View.VISIBLE);
                    } else {
                        addVehicleListScrollView.setVisibility(View.GONE);
                        noVehiclesTextView.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {

                        try {
/*
                            List<> myList = new ArrayList<>();

                            final JSONArray jsonArray = new JSONArray(response);
                            Log.e("Responce", jsonArray.toString());

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                            }



                            RecyclerView recyclerView = findViewById(R.id.recycleraddedvehicles);
                            MyListFindServiceAdater adapter = new MyListFindServiceAdater(myList);
                            recyclerView.setLayoutManager(new LinearLayoutManager(add_vehicles.this));
                            recyclerView.setAdapter(adapter);
*/

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                };
                runOnUiThread(runnable);

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