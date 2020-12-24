package com.example.biker;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.example.biker.Urls.book_service_url;
import static com.example.biker.list_user_service.getServiceListMethod;
import static com.example.biker.list_user_service.setProgressBarVisibilityService;

public class MyListServiceMethods {

    public void CancelServiceMethod(final Context context, MyListServiceData myListServiceData, JSONObject jsonObjectFirstMethod, String s) {
        Log.e("kk","Cancel Service......."+jsonObjectFirstMethod);
        setProgressBarVisibilityService(View.VISIBLE);


        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("servicer", jsonObjectFirstMethod.getInt("servicer"));
            jsonBody.put("user", jsonObjectFirstMethod.getInt("user"));
            jsonBody.put("brand", jsonObjectFirstMethod.getInt("brand"));
            jsonBody.put("vehicle_fk", jsonObjectFirstMethod.getInt("vehicle_fk"));
            jsonBody.put("model_fk", jsonObjectFirstMethod.getInt("model_fk"));
            jsonBody.put("solved", jsonObjectFirstMethod.getBoolean("solved"));
            jsonBody.put("accept", jsonObjectFirstMethod.getBoolean("accept"));
            jsonBody.put("remarks", jsonObjectFirstMethod.getString("remarks"));
            jsonBody.put("review", jsonObjectFirstMethod.getString("review"));
            if (s.trim().equals("servicer")) {
                jsonBody.put("cancel_user", jsonObjectFirstMethod.getBoolean("cancel_user"));
                jsonBody.put("cancel_servicer", true);
            } else if (s.trim().equals("user")) {
                jsonBody.put("cancel_user", true);
                jsonBody.put("cancel_servicer", jsonObjectFirstMethod.getBoolean("cancel_servicer"));
            } else {
                jsonBody.put("cancel_user", jsonObjectFirstMethod.getBoolean("cancel_user"));
                jsonBody.put("cancel_servicer", jsonObjectFirstMethod.getBoolean("cancel_servicer"));
            }
            jsonBody.put("problem", jsonObjectFirstMethod.getString("problem"));
            jsonBody.put("created_at", jsonObjectFirstMethod.getString("created_at"));
        } catch (JSONException e) {
            setProgressBarVisibilityService(View.GONE);
            e.printStackTrace();
        }
        final String requestBody = jsonBody.toString();

        String service_update_url = "";
        try {
            service_update_url = book_service_url + jsonObjectFirstMethod.get("id") + "/";
        } catch (JSONException e) {
            setProgressBarVisibilityService(View.GONE);
            e.printStackTrace();
        }
        StringRequest request = new StringRequest(Request.Method.PUT, service_update_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(login.this, ""+response, Toast.LENGTH_SHORT).show();
//                progressBar.setVisibility(View.GONE);
                try {
                    setProgressBarVisibilityService(View.GONE);
                    Toast.makeText(context, "Service is Cancelled Successfully!!", Toast.LENGTH_SHORT).show();
                    ShowUpdatedServiceListMethod(context);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                progressBar.setVisibility(View.GONE);
                setProgressBarVisibilityService(View.GONE);
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

    public void AcceptServiceMethod(final Context context, MyListServiceData myListServiceData, JSONObject jsonObjectFirstMethod, String s) {
        Log.e("kk","Accept Service......."+jsonObjectFirstMethod);
        setProgressBarVisibilityService(View.VISIBLE);




        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("servicer", jsonObjectFirstMethod.getInt("servicer"));
            jsonBody.put("user", jsonObjectFirstMethod.getInt("user"));
            jsonBody.put("brand", jsonObjectFirstMethod.getInt("brand"));
            jsonBody.put("vehicle_fk", jsonObjectFirstMethod.getInt("vehicle_fk"));
            jsonBody.put("model_fk", jsonObjectFirstMethod.getInt("model_fk"));
            jsonBody.put("solved", jsonObjectFirstMethod.getBoolean("solved"));
            jsonBody.put("accept", true);
            jsonBody.put("remarks", "" + s);
            jsonBody.put("review", jsonObjectFirstMethod.getString("review"));
            jsonBody.put("cancel_user", jsonObjectFirstMethod.getBoolean("cancel_user"));
            jsonBody.put("cancel_servicer", jsonObjectFirstMethod.getBoolean("cancel_servicer"));
            jsonBody.put("problem", jsonObjectFirstMethod.getString("problem"));
            jsonBody.put("created_at", jsonObjectFirstMethod.getString("created_at"));
        } catch (Exception e) {
            setProgressBarVisibilityService(View.GONE);
            e.printStackTrace();
        }
        final String requestBody = jsonBody.toString();

        String service_update_url = "";
        try {
            service_update_url = book_service_url + jsonObjectFirstMethod.get("id") + "/";
        } catch (JSONException e) {
            setProgressBarVisibilityService(View.GONE);
            e.printStackTrace();
        }
        StringRequest request = new StringRequest(Request.Method.PUT, service_update_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(login.this, ""+response, Toast.LENGTH_SHORT).show();
//                progressBar.setVisibility(View.GONE);
                try {
                    setProgressBarVisibilityService(View.GONE);
                    Toast.makeText(context, "Service is Accepted by Servicer!!", Toast.LENGTH_SHORT).show();
                    ShowUpdatedServiceListMethod(context);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                progressBar.setVisibility(View.GONE);
                setProgressBarVisibilityService(View.GONE);
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

    public void RemarkServiceMethod(final Context context, MyListServiceData myListServiceData, JSONObject jsonObjectFirstMethod, String s) {
        Log.e("kk","Remark Service......."+jsonObjectFirstMethod);
        setProgressBarVisibilityService(View.VISIBLE);






        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("servicer", jsonObjectFirstMethod.getInt("servicer"));
            jsonBody.put("user", jsonObjectFirstMethod.getInt("user"));
            jsonBody.put("brand", jsonObjectFirstMethod.getInt("brand"));
            jsonBody.put("vehicle_fk", jsonObjectFirstMethod.getInt("vehicle_fk"));
            jsonBody.put("model_fk", jsonObjectFirstMethod.getInt("model_fk"));
            jsonBody.put("solved", jsonObjectFirstMethod.getBoolean("solved"));
            jsonBody.put("accept", jsonObjectFirstMethod.getBoolean("accept"));
            jsonBody.put("remarks", "" + s);
            jsonBody.put("review", jsonObjectFirstMethod.getString("review"));
            jsonBody.put("cancel_user", jsonObjectFirstMethod.getBoolean("cancel_user"));
            jsonBody.put("cancel_servicer", jsonObjectFirstMethod.getBoolean("cancel_servicer"));
            jsonBody.put("problem", jsonObjectFirstMethod.getString("problem"));
            jsonBody.put("created_at", jsonObjectFirstMethod.getString("created_at"));
        } catch (Exception e) {
            setProgressBarVisibilityService(View.GONE);
            e.printStackTrace();
        }
        final String requestBody = jsonBody.toString();

        String service_update_url = "";
        try {
            service_update_url = book_service_url + jsonObjectFirstMethod.get("id") + "/";
        } catch (JSONException e) {
            setProgressBarVisibilityService(View.GONE);
            e.printStackTrace();
        }
        StringRequest request = new StringRequest(Request.Method.PUT, service_update_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(login.this, ""+response, Toast.LENGTH_SHORT).show();
//                progressBar.setVisibility(View.GONE);
                try {
                    setProgressBarVisibilityService(View.GONE);
                    Toast.makeText(context, "Remark is Updated Successfully!!", Toast.LENGTH_SHORT).show();
                    ShowUpdatedServiceListMethod(context);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                progressBar.setVisibility(View.GONE);
                setProgressBarVisibilityService(View.GONE);
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

    public void ReviewServiceMethod(final Context context, MyListServiceData myListServiceData, JSONObject jsonObjectFirstMethod, String s) {
        Log.e("kk","Review Service......."+jsonObjectFirstMethod);
        setProgressBarVisibilityService(View.VISIBLE);






        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("servicer", jsonObjectFirstMethod.getInt("servicer"));
            jsonBody.put("user", jsonObjectFirstMethod.getInt("user"));
            jsonBody.put("brand", jsonObjectFirstMethod.getInt("brand"));
            jsonBody.put("vehicle_fk", jsonObjectFirstMethod.getInt("vehicle_fk"));
            jsonBody.put("model_fk", jsonObjectFirstMethod.getInt("model_fk"));
            jsonBody.put("solved", jsonObjectFirstMethod.getBoolean("solved"));
            jsonBody.put("accept", jsonObjectFirstMethod.getBoolean("accept"));
            jsonBody.put("remarks", jsonObjectFirstMethod.getString("remarks"));
            jsonBody.put("review", "" + s);
            jsonBody.put("cancel_user", jsonObjectFirstMethod.getBoolean("cancel_user"));
            jsonBody.put("cancel_servicer", jsonObjectFirstMethod.getBoolean("cancel_servicer"));
            jsonBody.put("problem", jsonObjectFirstMethod.getString("problem"));
            jsonBody.put("created_at", jsonObjectFirstMethod.getString("created_at"));
        } catch (Exception e) {
            setProgressBarVisibilityService(View.GONE);
            e.printStackTrace();
        }
        final String requestBody = jsonBody.toString();

        String service_update_url = "";
        try {
            service_update_url = book_service_url + jsonObjectFirstMethod.get("id") + "/";
        } catch (JSONException e) {
            setProgressBarVisibilityService(View.GONE);
            e.printStackTrace();
        }
        StringRequest request = new StringRequest(Request.Method.PUT, service_update_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(login.this, ""+response, Toast.LENGTH_SHORT).show();
//                progressBar.setVisibility(View.GONE);
                try {
                    setProgressBarVisibilityService(View.GONE);
                    Toast.makeText(context, "Review is Submitted Successfully!!", Toast.LENGTH_SHORT).show();
                    ShowUpdatedServiceListMethod(context);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                progressBar.setVisibility(View.GONE);
                setProgressBarVisibilityService(View.GONE);
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


    public void ShowUpdatedServiceListMethod(final Context context) {
/*
        new Thread() {
            @Override
            public void run() {
                getServiceListMethod(context);
            }
        }.start();
*/
        getServiceListMethod(context);
    }

}
