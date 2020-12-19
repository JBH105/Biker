package com.example.biker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import static com.example.biker.Urls.getIsServicer;
import static com.example.biker.Urls.signin_url;
import static com.example.biker.Urls.storeIsLoggedIn;
import static com.example.biker.Urls.storeUserInfoInSharedPref;

public class bike_service_location extends AppCompatActivity {
    TextInputEditText locationEditText;
    TextView current_location;
    Button submit;
    LocationManager locationManager;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bike_service_location);

        locationEditText = findViewById(R.id.location);
        current_location = findViewById(R.id.current_location);
        submit = findViewById(R.id.submit);

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
//do here checking if number entered is zip code or not
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if (!locationEditText.getText().toString().isEmpty()) {
                        String zipExpression = "[0-9]{6}";
                        Pattern zipPattern = Pattern.compile(zipExpression);
                        Matcher zipMatcher = zipPattern.matcher(locationEditText.getText().toString());
                        if (zipMatcher.matches())
                            if (locationEditText.getText().toString().trim().length() == 6)
                                findServicersMethod();
                    }
                } catch (Exception e) {e.printStackTrace();}
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Location = locationEditText.getText().toString().trim();
                String current = current_location.toString().trim();

                if (Location.isEmpty()||current.isEmpty()){
                    Toast.makeText(bike_service_location.this, "Enter details", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(bike_service_location.this, "Service book", Toast.LENGTH_SHORT).show();

//                findServicersMethod();
            }
        });

    }

    private void findServicersMethod() {

        StringRequest request = new StringRequest(Request.Method.GET, find_servicer_url, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                Toast.makeText(bike_service_location.this, "" + response, Toast.LENGTH_SHORT).show();
//                progressBar.setVisibility(View.GONE);

                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {

                        try {
                            List<MyListFindServiceData> myListData = new ArrayList<>();

                            JSONArray jsonArray = new JSONArray(response);
                            Log.e("Responce", jsonArray.toString());

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                myListData.add(new MyListFindServiceData(jsonObject.getString("mobile"), jsonObject.getString("address_fl") + "  " + jsonObject.getString("address_sl") + "  " + jsonObject.getString("city") + "  " + jsonObject.getString("zip")));
                            }


                            RecyclerView recyclerView = findViewById(R.id.recyclerfindservice);
                            MyListFindServiceAdater adapter = new MyListFindServiceAdater(myListData);
                            recyclerView.setLayoutManager(new LinearLayoutManager(bike_service_location.this));
                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };
                runOnUiThread(runnable);


//                try {
//                    JSONObject jsonObj = new JSONObject(response.toString());
//                    Log.e("Responce", jsonObj.toString());
//
//                    String user_id = jsonObj.getJSONObject("user").getString("id");
//                    Log.e("Responce22", user_id);
//                    Toast.makeText(signup_address.this, ""+user_id, Toast.LENGTH_SHORT).show();

//                    Intent i = new Intent(getApplicationContext(), OTP.class);
//                    i.putExtra("User_Id", user_id);
//                    startActivity(i);

                //Toast.makeText(getApplicationContext(), user_Array.toString(), Toast.LENGTH_LONG).show();

                // looping through All Contacts
//                        for (int i = 0; i < user_Array.length(); i++) {
//                            JSONObject c = user_Array.getJSONObject(i);
//                            String id = c.getString("_id");
//                            Toast.makeText(getApplicationContext(), id, Toast.LENGTH_LONG).show();
//                        }

//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                progressBar.setVisibility(View.GONE);
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
        if (ContextCompat.checkSelfPermission(bike_service_location.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(bike_service_location.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);
        }
        getLocation();
    }

    private void getLocation() {

        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5,bike_service_location.this);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5,
                    new LocationListener() {
                        @Override
                        public void onLocationChanged(@NonNull Location location) {
                            Log.i("kkkk", location.getLatitude() + "," + location.getLongitude());
                            try {
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