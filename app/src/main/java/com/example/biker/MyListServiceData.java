package com.example.biker;

import android.content.Context;

public class MyListServiceData {
    private Context context;
    private String serviceId;
    Boolean cancelServicer, cancelUser;
    private String date, mobile, servicerName, userName;
    private String problemExplanation;
    private String vehicleNumber, model, brand;
    private Boolean accept, solved;
    private String remarks;
    private String review;

    public MyListServiceData(Context context, String serviceId, Boolean cancelServicer, Boolean cancelUser, String date, String mobile, String servicerName, String userName, String problemExplanation, String vehicleNumber, String model, String brand, Boolean accept, Boolean solved, String remarks, String review) {
        this.context = context;
        this.serviceId = serviceId;
        this.cancelServicer = cancelServicer;
        this.cancelUser = cancelUser;
        this.date = date;
        this.mobile = mobile;
        this.servicerName = servicerName;
        this.userName = userName;
        this.problemExplanation = problemExplanation;
        this.vehicleNumber = vehicleNumber;
        this.model = model;
        this.brand = brand;
        this.accept = accept;
        this.solved = solved;
        this.remarks = remarks;
        this.review = review;
    }

    public Context getContext() {
        return context;
    }

    public String getServiceId() {
        return serviceId;
    }

    public Boolean getCancelServicer() {
        return cancelServicer;
    }

    public Boolean getCancelUser() {
        return cancelUser;
    }

    public String getDate() {
        return date;
    }

    public String getMobile() {
        return mobile;
    }

    public String getServicerName() {
        return servicerName;
    }

    public String getUserName() {
        return userName;
    }

    public String getProblemExplanation() {
        return problemExplanation;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public String getModel() {
        return model;
    }

    public String getBrand() {
        return brand;
    }

    public Boolean getAccept() {
        return accept;
    }

    public Boolean getSolved() {
        return solved;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getReview() {
        return review;
    }
}
