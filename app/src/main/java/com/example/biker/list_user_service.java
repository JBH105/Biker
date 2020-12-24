package com.example.biker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.biker.garageuser.MyListAddedVehiclesAdapter;
import com.example.biker.garageuser.MyListAddedVehiclesData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.biker.Urls.brand_url;
import static com.example.biker.Urls.getIsServicer;
import static com.example.biker.Urls.getUsername;
import static com.example.biker.Urls.model_url;
import static com.example.biker.Urls.servicer_service_list_url;
import static com.example.biker.Urls.servicer_userdetails_url;
import static com.example.biker.Urls.signupid_url;
import static com.example.biker.Urls.user_service_list_url;
import static com.example.biker.Urls.vehicle_api_url;

public class list_user_service extends AppCompatActivity {

    private static RelativeLayout relativelayoutnoservice;
    private static RecyclerView recyclerView;
    private static MyListServiceAdapter adapter;
    private static List<MyListServiceData> myList = new ArrayList<>();
    private static ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_user_service);

        relativelayoutnoservice = findViewById(R.id.relativelayoutnoservice);
        recyclerView = findViewById(R.id.recyclerlistservice);
        progressBar = findViewById(R.id.progressBarService);
        progressBar.setVisibility(View.VISIBLE);

        // recyclerView
        myList.clear();
        adapter = new MyListServiceAdapter(myList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        new Thread() {
            @Override
            public void run() {
                getServiceListMethod(list_user_service.this);
            }
        }.start();


    }

    public static void getServiceListMethod(final Context context) {

        myList.clear();
        String service_list_url = "";
        if (getIsServicer(context))
            service_list_url = (servicer_service_list_url + getUsername(context));
        else
            service_list_url = (user_service_list_url + getUsername(context));
        StringRequest request = new StringRequest(Request.Method.GET, service_list_url, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
//                progressBar.setVisibility(View.GONE);
//                Toast.makeText(context, "No Details "+response, Toast.LENGTH_SHORT).show();
                try {
                    if (new JSONArray(response).toString().trim().isEmpty() || response.trim().equals("[]")) {
                        progressBar.setVisibility(View.GONE);
                        relativelayoutnoservice.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        progressBar.setVisibility(View.VISIBLE);
                        relativelayoutnoservice.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                new Thread() {
                    @Override
                    public void run() {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            Log.e("Responce", jsonArray.toString());

                            for (int i=0;i<jsonArray.length();i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                if (getIsServicer(context))
                                    getServicerUserNameMethod(context, jsonObject, "user");
                                else
                                    getServicerUserNameMethod(context, jsonObject, "servicer");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
//                Toast.makeText(add_vehicles.this, "/ ERROR: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                if(error.networkResponse.data!=null) {
                    try {
                        String errorMessage = new String(error.networkResponse.data,"UTF-8");
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
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

    private static void getServicerUserNameMethod(final Context context, final JSONObject jsonObjectFirstMethod, String WhoToFind) throws JSONException {

        String servicerorusertofind_url;
        if (WhoToFind.equals("user"))
            servicerorusertofind_url = signupid_url + jsonObjectFirstMethod.getString("user") + "/";
        else
            servicerorusertofind_url = signupid_url + jsonObjectFirstMethod.getString("servicer") + "/";

        StringRequest request = new StringRequest(Request.Method.GET, servicerorusertofind_url, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
//                progressBar.setVisibility(View.GONE);
//                Toast.makeText(context, ".. "+response, Toast.LENGTH_SHORT).show();

                new Thread() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.e("Responce", jsonObject.toString());
                            getUserFromResult(context, jsonObjectFirstMethod, jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
//                Toast.makeText(add_vehicles.this, "/ ERROR: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                if(error.networkResponse.data!=null) {
                    try {
                        String errorMessage = new String(error.networkResponse.data,"UTF-8");
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
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

    private static void getUserFromResult(final Context context, final JSONObject jsonObjectFirstMethod, final JSONObject jsonObjectSecondMethod) throws JSONException {

        String usernametofind_url = servicer_userdetails_url + jsonObjectSecondMethod.getString("user") + "/";
        StringRequest request = new StringRequest(Request.Method.GET, usernametofind_url, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
//                progressBar.setVisibility(View.GONE);
//                Toast.makeText(context, ".. "+response, Toast.LENGTH_SHORT).show();

                new Thread() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.e("Responce", jsonObject.toString());
                            if (getIsServicer(context)) {
                                jsonObjectFirstMethod.put("servicerName", getUsername(context));
                                jsonObjectFirstMethod.put("userName", jsonObject.getString("username"));
                                jsonObjectFirstMethod.put("address", jsonObjectSecondMethod.getString("address_fl")+", "+jsonObjectSecondMethod.getString("address_sl")+",\n "+jsonObjectSecondMethod.getString("city")+"-"+jsonObjectSecondMethod.getString("zip"));
                            } else {
                                jsonObjectFirstMethod.put("servicerName", jsonObject.getString("username"));
                                jsonObjectFirstMethod.put("userName", getUsername(context));
                            }
                            jsonObjectFirstMethod.put("mobileNumber", jsonObjectSecondMethod.getString("mobile"));
                            getBrandFromFirstResult(context, jsonObjectFirstMethod);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
//                Toast.makeText(add_vehicles.this, "/ ERROR: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                if(error.networkResponse.data!=null) {
                    try {
                        String errorMessage = new String(error.networkResponse.data,"UTF-8");
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
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

    private static void getBrandFromFirstResult(final Context context, final JSONObject jsonObjectFirstMethod) throws JSONException {

        String brandto_url = brand_url + jsonObjectFirstMethod.getString("brand") + "/";
        StringRequest request = new StringRequest(Request.Method.GET, brandto_url, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
//                progressBar.setVisibility(View.GONE);
//                Toast.makeText(context, ".. "+response, Toast.LENGTH_SHORT).show();

                new Thread() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.e("Responce", jsonObject.toString());
                            jsonObjectFirstMethod.put("brandName", jsonObject.getString("brand"));
                            getVehicleFromFirstResult(context, jsonObjectFirstMethod);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
//                Toast.makeText(add_vehicles.this, "/ ERROR: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                if(error.networkResponse.data!=null) {
                    try {
                        String errorMessage = new String(error.networkResponse.data,"UTF-8");
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
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



    private static void getVehicleFromFirstResult(final Context context, final JSONObject jsonObjectFirstMethod) throws JSONException {

        String vehicleto_url = vehicle_api_url + jsonObjectFirstMethod.getString("vehicle_fk") + "/";
        StringRequest request = new StringRequest(Request.Method.GET, vehicleto_url, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
//                progressBar.setVisibility(View.GONE);
//                Toast.makeText(context, ".. "+response, Toast.LENGTH_SHORT).show();

                new Thread() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.e("Responce", jsonObject.toString());
                            jsonObjectFirstMethod.put("vehicleNumber", jsonObject.getString("vehicle_number"));
                            getModelFromFirstResult(context, jsonObjectFirstMethod);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
//                Toast.makeText(add_vehicles.this, "/ ERROR: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                if(error.networkResponse.data!=null) {
                    try {
                        String errorMessage = new String(error.networkResponse.data,"UTF-8");
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
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

    private static void getModelFromFirstResult(final Context context, final JSONObject jsonObjectFirstMethod) throws JSONException {

        String modelto_url = model_url + jsonObjectFirstMethod.getString("model_fk") + "/";
        StringRequest request = new StringRequest(Request.Method.GET, modelto_url, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                progressBar.setVisibility(View.GONE);
//                Toast.makeText(context, ".. "+response, Toast.LENGTH_SHORT).show();

                new Thread() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.e("Responce", jsonObject.toString());
                            jsonObjectFirstMethod.put("modelName", jsonObject.getString("model_name"));


                            // List in RecyclerView
                            if (getIsServicer(context)) {
                                myList.add(new MyListServiceData(context,
                                        jsonObjectFirstMethod.getString("id"),
                                        jsonObjectFirstMethod.getBoolean("cancel_servicer"),
                                        jsonObjectFirstMethod.getBoolean("cancel_user"),
                                        jsonObjectFirstMethod.getString("created_at"),
                                        jsonObjectFirstMethod.getString("mobileNumber"),
                                        jsonObjectFirstMethod.getString("servicerName"),
                                        jsonObjectFirstMethod.getString("userName"),
                                        jsonObjectFirstMethod.getString("problem"),
                                        jsonObjectFirstMethod.getString("vehicleNumber"),
                                        jsonObjectFirstMethod.getString("modelName"),
                                        jsonObjectFirstMethod.getString("brandName"),
                                        jsonObjectFirstMethod.getBoolean("accept"),
                                        jsonObjectFirstMethod.getBoolean("solved"),
                                        jsonObjectFirstMethod.getString("remarks"),
                                        jsonObjectFirstMethod.getString("review"),
                                        jsonObjectFirstMethod.getString("address"),
                                        jsonObjectFirstMethod));
                            } else {
                                myList.add(new MyListServiceData(context,
                                        jsonObjectFirstMethod.getString("id"),
                                        jsonObjectFirstMethod.getBoolean("cancel_servicer"),
                                        jsonObjectFirstMethod.getBoolean("cancel_user"),
                                        jsonObjectFirstMethod.getString("created_at"),
                                        jsonObjectFirstMethod.getString("mobileNumber"),
                                        jsonObjectFirstMethod.getString("servicerName"),
                                        jsonObjectFirstMethod.getString("userName"),
                                        jsonObjectFirstMethod.getString("problem"),
                                        jsonObjectFirstMethod.getString("vehicleNumber"),
                                        jsonObjectFirstMethod.getString("modelName"),
                                        jsonObjectFirstMethod.getString("brandName"),
                                        jsonObjectFirstMethod.getBoolean("accept"),
                                        jsonObjectFirstMethod.getBoolean("solved"),
                                        jsonObjectFirstMethod.getString("remarks"),
                                        jsonObjectFirstMethod.getString("review"),
                                        jsonObjectFirstMethod));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();

                adapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
//                Toast.makeText(add_vehicles.this, "/ ERROR: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                if(error.networkResponse.data!=null) {
                    try {
                        String errorMessage = new String(error.networkResponse.data,"UTF-8");
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
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