package com.example.biker;

public class MyListFindServiceData {
    private String user_email, user_username;
    private String id, user_id, account_id;             /*  ->  Here "id" is nothing but vehicle_api_id */
    private String address_fl, address_sl, city, zip, mobile, is_servicer;

    public MyListFindServiceData(String id, String user_id, String user_email, String user_username, String address_fl, String address_sl, String city, String zip, String mobile, String is_servicer) {
        this.id = id;
        this.user_id = user_id;
        this.user_email = user_email;
        this.user_username = user_username;
        this.address_fl = address_fl;
        this.address_sl = address_sl;
        this.city = city;
        this.zip = zip;
        this.mobile = mobile;
        this.is_servicer = is_servicer;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public String getAccount_id() {
        return account_id;
    }

    public String getId() {
        return id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getUser_email() {
        return user_email;
    }

    public String getUser_username() {
        return user_username;
    }

    public String getAddress_fl() {
        return address_fl;
    }

    public String getAddress_sl() {
        return address_sl;
    }

    public String getCity() {
        return city;
    }

    public String getZip() {
        return zip;
    }

    public String getMobile() {
        return mobile;
    }

    public String getIs_servicer() {
        return is_servicer;
    }
}
