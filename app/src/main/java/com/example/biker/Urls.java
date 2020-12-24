package com.example.biker;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

public class Urls {
    public static final String Root_Url = "http://azdhebar.pythonanywhere.com/account/api/";
    public static final String signin_url = Root_Url + "login/";
    public static final String signup_url = Root_Url + "register/";
    public static final String signupid_url = Root_Url + "account/";
    public static final String brand_url = Root_Url + "brand/";
    public static final String model_url = Root_Url + "model/";
    public static final String vehicle_url = Root_Url + "vehicle/";
    public static final String vehicle_api_url = Root_Url + "vehicle_api/";
//    public static final String find_servicer_url = Root_Url + "service/findservice/vehicle/20/zip/336656";
    public static final String find_servicer_url = Root_Url + "service/findservice/";
    public static final String servicer_accountid_url = Root_Url + "findaccount/";
    public static final String servicer_userdetails_url = Root_Url + "user/";
    public static final String servicer_add_vehicle_url = Root_Url + "servicer/";
    public static final String book_service_url = Root_Url + "service/";
    public static final String servicer_vehicles_list_url = Root_Url + "findservicer/";

    public static final String user_service_list_url = Root_Url + "service/user/";
    public static final String servicer_service_list_url = Root_Url + "service/servicer/";



    public static void storeUserInfoInSharedPref(Context context, JSONObject jsonObject) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            JSONObject jsonObjectUser = jsonObject.getJSONObject("user");
            editor.putInt("user_id", jsonObjectUser.getInt("id"));
            editor.putString("email", jsonObjectUser.getString("email"));
            editor.putString("username", jsonObjectUser.getString("username"));
            JSONObject jsonObjectAccount = jsonObject.getJSONObject("account");
            editor.putInt("account_id", jsonObjectAccount.getInt("id"));
            editor.putString("address_fl", jsonObjectAccount.getString("address_fl"));
            editor.putString("address_sl", jsonObjectAccount.getString("address_sl"));
            editor.putString("city", jsonObjectAccount.getString("city"));
            editor.putString("zip", jsonObjectAccount.getString("zip"));
            editor.putString("mobile", jsonObjectAccount.getString("mobile"));
            editor.putBoolean("is_servicer", jsonObjectAccount.getBoolean("is_servicer"));
            editor.commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public static void storeIsLoggedIn(Context context, Boolean value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        if (value.equals(false)) {
            editor.clear();
        }
        editor.putBoolean("ISLOGGEDIN", value);
        editor.commit();
    }
    public static boolean getIsLoggedIn(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean("ISLOGGEDIN", false);
    }
    public static boolean getIsServicer(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean("is_servicer", false);
    }
    public static int getUserId(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt("user_id", 0);
    }
    public static int getAccountId(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt("account_id", 0);
    }
    public static String getUsername(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("username", "null");
    }
    public static String getEmail(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("email", "null");
    }
    public static String getMobile(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("mobile", "null");
    }
    public static String getAddress_fl(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("address_fl", "null");
    }
    public static String getAddress_sl(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("address_sl", "null");
    }
    public static String getCity(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("city", "null");
    }
    public static String getZip(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("zip", "null");
    }
}
