package com.example.biker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.biker.Urls.brand_url;
import static com.example.biker.Urls.model_url;

public class bike_service extends AppCompatActivity {
    Spinner spinner;
    TextInputEditText plate_number,remark;
    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bike_service);

        spinner=findViewById(R.id.spinner);
        plate_number=findViewById(R.id.plate_number);
        remark=findViewById(R.id.remark);
        next=findViewById(R.id.next);

        getModelList();
        //spinner
//        ArrayAdapter<CharSequence> Adapter=ArrayAdapter.createFromResource(getApplicationContext(),R.array.bike,R.layout.support_simple_spinner_dropdown_item);
//        spinner.setAdapter(Adapter);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = plate_number.getText().toString().trim();
                String Remark = remark.getText().toString().trim();
                String Spinner = spinner.getSelectedItem().toString().trim();

                if (Spinner.equals("Selecte Model")){
                    Toast.makeText(bike_service.this, "select model", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (number.isEmpty()){
                    plate_number.setError("Enter plate number");
                    return;
                }
                if (Remark.isEmpty()){
                    remark.setError("Enter Remark");
                    return;
                }
                // get Brand from brand_id in model_url response
                getBrandItem();
                Toast.makeText(bike_service.this, "Selected Item"+spinner.getSelectedItem(), Toast.LENGTH_SHORT).show();

//                startActivity(new Intent(getApplicationContext(), bike_service_location.class));
            }
        });

    }

    private void getModelList() {
        StringRequest request = new StringRequest(Request.Method.GET, model_url, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
//                progressBar.setVisibility(View.GONE);
                Toast.makeText(bike_service.this, ".. "+response, Toast.LENGTH_SHORT).show();


                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            try {

                            JSONObject jsonObj = new JSONObject(response);
                            Log.e("Responce", jsonObj.toString());

                            List<String> modellist = new ArrayList<String>();
                            for(int i=0;i<jsonObj.length();i++) {
                                modellist.add(jsonObj.getString("model_name"));
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(bike_service.this, android.R.layout.select_dialog_item, modellist);
                            adapter.setDropDownViewResource(android.R.layout.select_dialog_item);
                            spinner.setAdapter(adapter);
/*                            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });*/

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };


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
//                progressBar.setVisibility(View.GONE);
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


    private void getBrandItem() {
    }
}