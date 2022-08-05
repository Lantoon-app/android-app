package com.bazinga.lantoon.registration.model;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("email")
    public String email;
    @SerializedName("uname")
    public String uname;
    @SerializedName("pass")
    public String pass;
    @SerializedName("countrycode")
    public String countrycode;
    @SerializedName("phone")
    public String phone;
    @SerializedName("region")
    public String region;
    @SerializedName("institute")
    public String institute;
    @SerializedName("groupcode")
    public String groupcode;
    @SerializedName("learnlang")
    public int learnlang;
    @SerializedName("knownlang")
    public int knownlang;
    @SerializedName("deviceid")
    public String deviceid;
    @SerializedName("userrole")
    public int userrole;
    @SerializedName("currentlocation")
    public String currentlocation;
    @SerializedName("minsperday")
    public int minsperday;
    @SerializedName("registrationtype")
    public int registrationtype;

    @SerializedName("notify_token")
    public String notify_token;
    @SerializedName("os_type")
    public String os_type;
    @SerializedName("device_model")
    public String device_model;

    public User(String email,
                String uname,
                String pass,
                String countrycode,
                String phone,
                String region,
                String institute,
                String groupcode,
                int learnlang,
                int knownlang,
                String deviceid,
                int userrole,
                String currentlocation,
                int minsperday,
                int registrationtype,
                String notify_token,
                String os_type,
                String device_model) {
        this.email = email;
        this.uname = uname;
        this.pass = pass;
        this.countrycode = countrycode;
        this.phone = phone;
        this.region = region;
        this.institute = institute;
        this.groupcode = groupcode;
        this.learnlang = learnlang;
        this.knownlang = knownlang;
        this.deviceid = deviceid;
        this.userrole = userrole;
        this.currentlocation = currentlocation;
        this.minsperday = minsperday;
        this.registrationtype = registrationtype;
        this.notify_token = notify_token;
        this.os_type = os_type;
        this.device_model = device_model;
    }
}
//email,uname,pass,countryCode,phone,region,learnLanguage,knownLanguage,deviceId,currentLocation,userRole,timeDurationPerDay,[register]