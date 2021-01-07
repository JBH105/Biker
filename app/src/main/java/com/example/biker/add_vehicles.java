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
import android.widget.ProgressBar;
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
import com.example.biker.garageuser.MyListAddedVehiclesAdapter;
import com.example.biker.garageuser.MyListAddedVehiclesData;
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
import static com.example.biker.Urls.servicer_vehicles_list_url;
import static com.example.biker.Urls.signin_url;
import static com.example.biker.Urls.storeIsLoggedIn;
import static com.example.biker.Urls.storeUserInfoInSharedPref;
import static com.example.biker.Urls.vehicle_url;
import static com.example.biker.user.user_service_data.storeModelBrandData;
import static com.example.biker.user.user_service_data.storeVehicleData;

public class add_vehicles extends AppCompatActivity {
    private Spinner spinner;
    private Button addbtn;
    private ArrayAdapter<String> modelAdapter;
    private Map<String, List<String>> modelmap = new HashMap<>();
    private static ScrollView addVehicleListScrollView;
    private static TextView noVehiclesTextView;
    //
    private static RecyclerView recyclerView;
    private static MyListAddedVehiclesAdapter adapter;
    private static List<MyListAddedVehiclesData> myList = new ArrayList<>();
    private static ProgressBar progressBar, progressBarInsideRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_vehicles);

        spinner = findViewById(R.id.spinner);
        addbtn = findViewById(R.id.addbtn);
        addVehicleListScrollView = findViewById(R.id.addVehicleListScrollView);
        noVehiclesTextView = findViewById(R.id.noVehiclesTextView);
        recyclerView = findViewById(R.id.recycleraddedvehicles);
        progressBar = findViewById(R.id.progressBar);
        progressBarInsideRecyclerView = findViewById(R.id.progressBarInsideRecyclerViewAdd);

        getModelList();

        // recyclerView
        adapter = new MyListAddedVehiclesAdapter(myList);
        recyclerView.setLayoutManager(new LinearLayoutManager(add_vehicles.this));
        recyclerView.setAdapter(adapter);

/*
        new Thread() {
            @Override
            public void run() {
                getVehicleListMethod(add_vehicles.this);
            }
        }.start();
*/
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                getVehicleListMethod(add_vehicles.this);
            }
        };
        runOnUiThread(runnable);

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
                    progressBar.setVisibility(View.VISIBLE);
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
        progressBar.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.GET, model_url, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
//                progressBar.setVisibility(View.GONE);
//                Toast.makeText(add_vehicles.this, ".. "+response, Toast.LENGTH_SHORT).show();

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
                            progressBar.setVisibility(View.GONE);
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
                progressBar.setVisibility(View.GONE);
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
//                Toast.makeText(add_vehicles.this, ""+response, Toast.LENGTH_SHORT).show();
                try {
/*                    new Thread() {
                        @Override
                        public void run() {
                            getVehicleListMethod(add_vehicles.this);
                        }
                    }.start();*/
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                            spinner.setSelection(0);
                            Toast.makeText(add_vehicles.this, "Vehicle Successfully Added!!", Toast.LENGTH_SHORT).show();
                            getVehicleListMethod(add_vehicles.this);
                        }
                    };
                    runOnUiThread(runnable);
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                startActivity(new Intent(getApplicationContext(), user_home.class));

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                progressBar.setVisibility(View.GONE);
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                    }
                };
                runOnUiThread(runnable);
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

    private static void getVehicleListMethod(final Context context) {

        addVehicleListScrollView.setVisibility(View.VISIBLE);
        if (progressBar.getVisibility() != View.VISIBLE)
            progressBarInsideRecyclerView.setVisibility(View.VISIBLE);
        // Clear List before getting Added Vehicle List
        myList.clear();

        String servicer_vehiclelist_url = servicer_vehicles_list_url + getAccountId(context);
        StringRequest request = new StringRequest(Request.Method.GET, servicer_vehiclelist_url, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
//                Toast.makeText(context, ""+response, Toast.LENGTH_SHORT).show();
//                progressBar.setVisibility(View.GONE);
                try {
                    if (new JSONArray(response).toString().trim().isEmpty() || response.trim().equals("[]")) {
                        addVehicleListScrollView.setVisibility(View.VISIBLE);
                        progressBarInsideRecyclerView.setVisibility(View.GONE);
                        noVehiclesTextView.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        addVehicleListScrollView.setVisibility(View.VISIBLE);
                        noVehiclesTextView.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
/*
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {

                        new Thread() {
                            @Override
                            public void run() {
                                try {

                                    final JSONArray jsonArray = new JSONArray(response);
                                    Log.e("Responce", jsonArray.toString());

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        getModeldataMethod(context, jsonObject);
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        }.start();

                    }
                };
                new Thread(runnable).start();
*/
//                runOnUiThread(runnable);
                try {

                    final JSONArray jsonArray = new JSONArray(response);
                    Log.e("Responce", jsonArray.toString());

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        getModeldataMethod(context, jsonObject);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressBarInsideRecyclerView.setVisibility(View.GONE);
                if(error.networkResponse.data!=null) {
                    try {
                        String errorMessage = new String(error.networkResponse.data,"UTF-8");
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(context, "ERROR: "+error.toString(), Toast.LENGTH_SHORT).show();
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

    private static void getModeldataMethod(final Context context, final JSONObject jsonObject) throws JSONException {

        String modeldataurl = model_url + jsonObject.getString("model_fk") +"/";
        StringRequest request = new StringRequest(Request.Method.GET, modeldataurl, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
//                Toast.makeText(add_vehicles.this, ""+response, Toast.LENGTH_SHORT).show();
//                progressBar.setVisibility(View.GONE);

                try {

                    final JSONObject jsonObjectmodel = new JSONObject(response);
                    Log.e("Responce", jsonObjectmodel.toString());

                    getBranddataMethod(context, jsonObject, jsonObjectmodel);

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressBarInsideRecyclerView.setVisibility(View.GONE);
                if(error.networkResponse.data!=null) {
                    try {
                        String errorMessage = new String(error.networkResponse.data,"UTF-8");
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(context, "ERROR: "+error.toString(), Toast.LENGTH_SHORT).show();
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

    private static void getBranddataMethod(final Context context, final JSONObject jsonObjectav, final JSONObject jsonObjectmodel) {

        try {
            String branddataurl = brand_url + jsonObjectmodel.getString("brand") + "/";
            StringRequest request = new StringRequest(Request.Method.GET, branddataurl, new Response.Listener<String>() {
                @Override
                public void onResponse(final String response) {
//                    Toast.makeText(add_vehicles.this, ""+response, Toast.LENGTH_SHORT).show();
//                    progressBar.setVisibility(View.GONE);

                    try {
                        progressBarInsideRecyclerView.setVisibility(View.GONE);
                        JSONObject jsonObject = new JSONObject(response);
                        Log.e("Responce", jsonObject.toString());

                        myList.add(new MyListAddedVehiclesData(jsonObjectav.getString("id"), jsonObjectmodel.getString("id"),jsonObjectmodel.getString("model_name"),jsonObject.getString("brand")));
                        adapter.notifyDataSetChanged();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }




                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    progressBarInsideRecyclerView.setVisibility(View.GONE);
                    if(error.networkResponse.data!=null) {
                        try {
                            String errorMessage = new String(error.networkResponse.data,"UTF-8");
                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        Toast.makeText(context, "ERROR: "+error.toString(), Toast.LENGTH_SHORT).show();
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

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




    public static void deletevehicleAddedMethod(final Context context, MyListAddedVehiclesData addedvehicletodelete) {
        progressBar.setVisibility(View.VISIBLE);
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("user", getAccountId(context));
            jsonBody.put("model_fk", addedvehicletodelete.getModelId());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String requestBody = jsonBody.toString();

        String deleteaddedvehicleurl = servicer_add_vehicle_url + addedvehicletodelete.getId() + "/";
        StringRequest request = new StringRequest(Request.Method.DELETE, deleteaddedvehicleurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(login.this, ""+response, Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                try {
                    Toast.makeText(context, "Vehicle Deleted Successfully!!", Toast.LENGTH_SHORT).show();
/*                    new Thread() {
                        @Override
                        public void run() {
                            getVehicleListMethod(context);
                        }
                    }.start();*/
                    getVehicleListMethod(context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                startActivity(new Intent(getApplicationContext(), user_home.class));

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                if(error.networkResponse.data!=null) {
                    try {
                        String errorMessage = new String(error.networkResponse.data,"UTF-8");
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(context, "ERROR: "+error.toString(), Toast.LENGTH_SHORT).show();
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