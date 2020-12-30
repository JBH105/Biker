package com.example.biker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.biker.Urls.find_servicer_url;
import static com.example.biker.Urls.getAccountId;
import static com.example.biker.Urls.getIsServicer;
import static com.example.biker.Urls.servicer_userdetails_url;
import static com.example.biker.Urls.signin_url;
import static com.example.biker.Urls.storeIsLoggedIn;
import static com.example.biker.Urls.storeUserInfoInSharedPref;
import static com.example.biker.Urls.vehicle_api_url;
import static com.example.biker.user.user_service_data.getModel_id;
import static com.example.biker.user.user_service_data.getVehicle_number;
import static com.example.biker.user.user_service_data.getVehicleapi_id;
import static com.example.biker.user.user_service_data.storeVehicleApiId;

public class bike_service_location extends AppCompatActivity {
    public static Activity bike_service_location_activity;
    TextInputEditText locationEditText;
    TextView current_location, noServicerTextView;
    Button submit;
    LocationManager locationManager;
    //
    Boolean nearestLocationFlag = true;
    List<MyListFindServiceData> myListData = new ArrayList<>();
    RecyclerView recyclerView;
    MyListFindServiceAdater adapter;
    static ProgressBar progressBar;
    ProgressBar progressBarInsideRecyclerView;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bike_service_location);

        bike_service_location_activity = bike_service_location.this;
        locationEditText = findViewById(R.id.location);
        current_location = findViewById(R.id.current_location);
        noServicerTextView = findViewById(R.id.noServicerTextView);
        recyclerView = findViewById(R.id.recyclerfindservice);
        submit = findViewById(R.id.submit);
        progressBar = findViewById(R.id.progressBar);
        progressBarInsideRecyclerView = findViewById(R.id.progressBarInsideRecyclerView);

        // POST vehicle_api to get vehicle_api id
        getVehicleApiIdMethod();

        current_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocationZipCodeMethod();
            }
        });
        locationEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if (!locationEditText.getText().toString().isEmpty()) {

                        //do here checking if number entered is zip code or not
                        String zipExpression = "[0-9]{6}";
                        Pattern zipPattern = Pattern.compile(zipExpression);
                        Matcher zipMatcher = zipPattern.matcher(locationEditText.getText().toString());
                        if (zipMatcher.matches())
                            if (locationEditText.getText().toString().trim().length() == 6)
                                findServicersMethod();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


/*
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Service will be booked from Book Button in RecyclerView NOT FROM THIS BUTTON
                String Location = locationEditText.getText().toString().trim();
                if (Location.isEmpty()){
                    Toast.makeText(bike_service_location.this, "Specify Your Location!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(bike_service_location.this, "Service book", Toast.LENGTH_SHORT).show();

            }
        });
*/

    }

    @Override
    public void onBackPressed() {
        try {
            new Thread() {
                @Override
                public void run() {
                    deleteVehicleApiIdMethod();
                }
            }.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        try {
            new Thread() {
                @Override
                public void run() {
                    deleteVehicleApiIdMethod();
                }
            }.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    public static void setProgressBarVisibility(int i) {
        progressBar.setVisibility(i);
    }

    private void getVehicleApiIdMethod() {

        JSONObject jsonBody = new JSONObject();
        try {
            if (getVehicle_number() == null || getModel_id() == null)
                wait(100);
            jsonBody.put("vehicle_number", getVehicle_number());
            jsonBody.put("model_fk", getModel_id());
            jsonBody.put("user",getAccountId(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
        final String requestBody = jsonBody.toString();

        StringRequest request = new StringRequest(Request.Method.POST, vehicle_api_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(login.this, ""+response, Toast.LENGTH_SHORT).show();
//                progressBar.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    final String vehicle_api_id = jsonObject.getString("id");
                    new Thread() {
                        @Override
                        public void run() {
                            storeVehicleApiId(vehicle_api_id);
                        }
                    }.start();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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

    private void deleteVehicleApiIdMethod() {

        JSONObject jsonBody = new JSONObject();
        try {
            if (getVehicle_number() == null || getModel_id() == null || getVehicleapi_id() == null)
                wait(100);
            if (getVehicleapi_id() == null)
                return;
            jsonBody.put("vehicle_number", getVehicle_number());
            jsonBody.put("model_fk", getModel_id());
            jsonBody.put("user",getAccountId(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
        final String requestBody = jsonBody.toString();

        String delete_vehicle_api_url = vehicle_api_url + getVehicleapi_id();
        StringRequest request = new StringRequest(Request.Method.DELETE, delete_vehicle_api_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(login.this, ""+response, Toast.LENGTH_SHORT).show();
                Log.e("kk",getVehicleapi_id()+" ID Vehicle_Id is Deleted..");
//                progressBar.setVisibility(View.GONE);
/*
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    final String vehicle_api_id = jsonObject.getString("id");
                    new Thread() {
                        @Override
                        public void run() {
                            storeVehicleApiId(vehicle_api_id);
                        }
                    }.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
*/

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

    private void findServicersMethod() throws InterruptedException {
        progressBarInsideRecyclerView.setVisibility(View.VISIBLE);
        if (getVehicleapi_id() == null)
            wait(100);
        String zipCode = locationEditText.getText().toString().trim();
        String findservicerurl = find_servicer_url+"vehicle/"+getVehicleapi_id()+"/zip/"+zipCode;
        StringRequest request = new StringRequest(Request.Method.GET, findservicerurl, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
//                Toast.makeText(bike_service_location.this, "" + response, Toast.LENGTH_SHORT).show();
//                progressBar.setVisibility(View.GONE);
                try {
                    if (response.trim().equals("[]")) {
                        if (nearestLocationFlag) {
                            getLocationNearest();
                            nearestLocationFlag = false;
                        }
                        progressBarInsideRecyclerView.setVisibility(View.GONE);
                        noServicerTextView.setVisibility(View.VISIBLE);
                    } else {
                        noServicerTextView.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {

                        try {
//                            List<MyListFindServiceData> myListData = new ArrayList<>();

                            final JSONArray jsonArray = new JSONArray(response);
                            Log.e("Responce", jsonArray.toString());

/*
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                myListData.add(new MyListFindServiceData(jsonObject.getString("id"),jsonObject.getString("user"), jsonObject.getString("address_fl"), jsonObject.getString("address_sl"), jsonObject.getString("city"), jsonObject.getString("zip"), jsonObject.getString("mobile"), jsonObject.getString("is_servicer")));
                            }


                            RecyclerView recyclerView = findViewById(R.id.recyclerfindservice);
                            MyListFindServiceAdater adapter = new MyListFindServiceAdater(myListData);
                            recyclerView.setLayoutManager(new LinearLayoutManager(bike_service_location.this));
                            recyclerView.setAdapter(adapter);
*/
                            myListData.clear();
                            adapter = new MyListFindServiceAdater(myListData);
                            recyclerView.setLayoutManager(new LinearLayoutManager(bike_service_location.this));
                            recyclerView.setAdapter(adapter);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                final int finalI = i;
                                new Thread() {
                                    @Override
                                    public void run() {
                                        try {
                                            getServicerUserDetails(jsonArray.getJSONObject(finalI));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }.start();
                            }

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
                progressBarInsideRecyclerView.setVisibility(View.GONE);
                if (error.networkResponse.data != null) {
                    try {
                        String errorMessage = new String(error.networkResponse.data, "UTF-8");
                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "ERROR: " + error.toString(), Toast.LENGTH_SHORT).show();
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

    private void getServicerUserDetails(final JSONObject jsonObject) throws JSONException {
        String serviceuserdetailsrurl = servicer_userdetails_url+jsonObject.getString("user");
        StringRequest request = new StringRequest(Request.Method.GET, serviceuserdetailsrurl, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
//                Toast.makeText(bike_service_location.this, "" + response, Toast.LENGTH_SHORT).show();
//                progressBar.setVisibility(View.GONE);

                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {

                        try {

                            progressBarInsideRecyclerView.setVisibility(View.GONE);
                            JSONObject jsonObjectUserDetails = new JSONObject(response);
                            if (jsonObjectUserDetails.toString().isEmpty())
                                return;
                            MyListFindServiceData md = new MyListFindServiceData(jsonObject.getString("id"), jsonObject.getString("user"), jsonObjectUserDetails.getString("email"), jsonObjectUserDetails.getString("username"), jsonObject.getString("address_fl"), jsonObject.getString("address_sl"), jsonObject.getString("city"), jsonObject.getString("zip"), jsonObject.getString("mobile"), jsonObject.getString("is_servicer"));
                            myListData.add(md);

                            //adapter.add(md);
                            adapter.notifyDataSetChanged();


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
                progressBarInsideRecyclerView.setVisibility(View.GONE);
                if (error.networkResponse.data != null) {
                    try {
                        String errorMessage = new String(error.networkResponse.data, "UTF-8");
                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "ERROR: " + error.toString(), Toast.LENGTH_SHORT).show();
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

    private void getLocationZipCodeMethod() {
        //Runtime permissions
        try {

            if (ContextCompat.checkSelfPermission(bike_service_location.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(bike_service_location.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    ActivityCompat.requestPermissions(bike_service_location.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
                } else {
                    ActivityCompat.requestPermissions(bike_service_location.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
                }
            } else {
                getLocation();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 100: {
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(bike_service_location.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        getLocation();
                    }
                } else {
                    Toast.makeText(bike_service_location.this, "Permission Denied!!", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private void getLocation() {

        try {
            progressBar.setVisibility(View.VISIBLE);
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5,bike_service_location.this);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(bike_service_location.this, "Permission Denied!!", Toast.LENGTH_SHORT).show();
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5,
                    new LocationListener() {
                        @Override
                        public void onLocationChanged(@NonNull Location location) {
                            Log.i("kkkk", location.getLatitude() + "," + location.getLongitude());
                            try {
                                progressBar.setVisibility(View.GONE);
                                Geocoder geocoder = new Geocoder(bike_service_location.this, Locale.getDefault());
                                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);


                                if (addresses.size() > 0) {
                                    Address address1 = addresses.get(0);
                                    String fullAdd = address1.getAddressLine(0); // full address
                                    String locality = address1.getLocality();
                                    String zip = address1.getPostalCode();
                                    String country = address1.getCountryName();
                                    Log.i("kkkk", "Zip: " + zip);
                                    locationEditText.setText(zip);
                                }
                                // To get Location only once and then removeUpdates() use following line
                                locationManager.removeUpdates(this);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void getLocationNearest() {

        try {
            progressBar.setVisibility(View.VISIBLE);
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5,bike_service_location.this);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(bike_service_location.this, "Permission Denied!!", Toast.LENGTH_SHORT).show();
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 10000,
                    new LocationListener() {
                        @Override
                        public void onLocationChanged(@NonNull Location location) {
                            Log.i("kkkk", location.getLatitude() + "," + location.getLongitude());
                            try {
                                progressBar.setVisibility(View.GONE);
                                Geocoder geocoder = new Geocoder(bike_service_location.this, Locale.getDefault());
                                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);


                                if (addresses.size() > 0) {
                                    Address address1 = addresses.get(0);
                                    String fullAdd = address1.getAddressLine(0); // full address
                                    String locality = address1.getLocality();
                                    String zip = address1.getPostalCode();
                                    String country = address1.getCountryName();
                                    Log.i("kkkk", "Zip: " + zip);
                                    locationEditText.setText(zip);
                                }
                                // To get Location only once and then removeUpdates() use following line
                                locationManager.removeUpdates(this);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}