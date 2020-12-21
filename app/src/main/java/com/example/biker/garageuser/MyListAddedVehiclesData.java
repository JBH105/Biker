package com.example.biker.garageuser;

import android.content.Context;
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
import com.example.biker.add_vehicles;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.example.biker.Urls.getAccountId;
import static com.example.biker.Urls.servicer_add_vehicle_url;

public class MyListAddedVehiclesData {
    private String id, srno, model_id, model_name, brand_name;

    public MyListAddedVehiclesData(String id, String model_id, String model_name, String brand_name) {
        this.id = id;
        this.model_id = model_id;
        this.model_name = model_name;
        this.brand_name = brand_name;
    }

    public String getId() {
        return id;
    }

    public String getModelId() {
        return model_id;
    }

    public String getModel_name() {
        return model_name;
    }

    public String getBrand_name() {
        return brand_name;
    }

    // delete added vehicles of servicer, method is written in add_vehicles.java file.
/*
    public void deletevehicleAddedMethod(final Context context, MyListAddedVehiclesData addedvehicletodelete) {

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
//                progressBar.setVisibility(View.GONE);
                try {
                    Toast.makeText(context, "Vehicle Deleted Successfully!!", Toast.LENGTH_SHORT).show();
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
*/

}
