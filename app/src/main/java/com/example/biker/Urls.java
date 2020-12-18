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
    public static final String vehicle_api_url = Root_Url + "vehicle_api/";




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
            editor.apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public static void storeIsLoggedIn(Context context, Boolean value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("ISLOGGEDIN", value);
        editor.apply();
    }
    public static boolean getIsLoggedIn(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean("ISLOGGEDIN", false);
    }
    public static boolean getIsServicer(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean("is_servicer", false);
    }
}
