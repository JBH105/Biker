package com.example.biker.user;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
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
import com.example.biker.MyListFindServiceData;
import com.example.biker.garageuser.home;
import com.example.biker.list_user_service;
import com.example.biker.login;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.example.biker.Urls.book_service_url;
import static com.example.biker.Urls.getAccountId;
import static com.example.biker.Urls.getIsServicer;
import static com.example.biker.Urls.servicer_accountid_url;
import static com.example.biker.Urls.signin_url;
import static com.example.biker.Urls.storeIsLoggedIn;
import static com.example.biker.Urls.storeUserInfoInSharedPref;
import static com.example.biker.bike_service.bike_service_activity;
import static com.example.biker.bike_service_location.bike_service_location_activity;
import static com.example.biker.bike_service_location.setProgressBarVisibility;
import static com.example.biker.user.user_service_data.getBrand_id;
import static com.example.biker.user.user_service_data.getModel_id;
import static com.example.biker.user.user_service_data.getProblem;
import static com.example.biker.user.user_service_data.getVehicleapi_id;

public class user_book_service {
    private Context context;
    private static MyListFindServiceData selelectedServicerData;
    public void Book_Service(Context context1, MyListFindServiceData selelectedServicerData1) {
        context = context1;
        selelectedServicerData = selelectedServicerData1;
        new Thread() {
            @Override
            public void run() {
                getAccountIdOfServicer(selelectedServicerData.getUser_id());
            }
        }.start();
    }
    private void getAccountIdOfServicer(String user_id_ofservicer) {

        String findaccountid_url = servicer_accountid_url + user_id_ofservicer;
        StringRequest request = new StringRequest(Request.Method.GET, findaccountid_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(login.this, ""+response, Toast.LENGTH_SHORT).show();
//                progressBar.setVisibility(View.GONE);
                try {
                    final String accid = new JSONArray(response).getJSONObject(0).getString("id");
                    new Thread() {
                        @Override
                        public void run() {
                            selelectedServicerData.setAccount_id(accid);
                            bookServiceMethod();
                        }
                    }.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                progressBar.setVisibility(View.GONE);
                setProgressBarVisibility(View.GONE);
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

    private void bookServiceMethod() {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("servicer", selelectedServicerData.getAccount_id());
            jsonBody.put("user", getAccountId(context));
            jsonBody.put("brand", getBrand_id());
            jsonBody.put("vehicle_fk", getVehicleapi_id());
            jsonBody.put("model_fk", getModel_id());
            jsonBody.put("solved", false);
            jsonBody.put("accept", false);
            jsonBody.put("remarks", "");
            jsonBody.put("review", "");
            jsonBody.put("cancel_user", false);
            jsonBody.put("cancel_servicer", false);
//            jsonBody.put("problem_image", "");
            jsonBody.put("problem", getProblem());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String requestBody = jsonBody.toString();

        StringRequest request = new StringRequest(Request.Method.POST, book_service_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Toast.makeText(context, ""+response, Toast.LENGTH_SHORT).show();
                Toast.makeText(context, "Service Booked Successfully!!", Toast.LENGTH_SHORT).show();
                Log.i("kk","Book Service:  "+response);
//                progressBar.setVisibility(View.GONE);
                try {
                    setProgressBarVisibility(View.GONE);
                    bike_service_activity.finish();
                    bike_service_location_activity.finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                context.startActivity(new Intent(context, list_user_service.class));

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                progressBar.setVisibility(View.GONE);
                setProgressBarVisibility(View.GONE);
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
