package com.example.biker.user;

import java.util.List;
import java.util.Map;

public class user_service_data {
    public static String model_id, model_name;
    public static String brand_id, brand_name;
    public static String vehicle_number, problem;
    public static String vehicleapi_id;
    public static void storeModelBrandData(Map<String, List<String>> modelmp, String selectedmodelname, Map<String, String> brandmap) {
        model_name = selectedmodelname;
        List<String> selectedlist = modelmp.get(selectedmodelname);
        model_id = selectedlist.get(0);
        brand_id = selectedlist.get(1);
        brand_name = brandmap.get(brand_id);
    }
    public static void storeVehicleData(String number, String remark) {
        vehicle_number = number;
        problem = remark;
    }
    public static void storeVehicleApiId(String vehicle_api_id) {
        vehicleapi_id = vehicle_api_id;
    }

    public static String getBrand_id() {
        return brand_id;
    }
    public static String getProblem() {
        return problem;
    }
    public static String getVehicleapi_id() {
        return vehicleapi_id;
    }
    public static String getModel_id() {
        return model_id;
    }
    public static String getVehicle_number() {
        return vehicle_number;
    }
}
