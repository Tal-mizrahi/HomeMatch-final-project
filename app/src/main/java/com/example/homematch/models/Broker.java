package com.example.homematch.models;

public class Broker extends User {
    private String agencyName;

    public Broker(String firstName, String lastName, String email, String phoneNumber
            , String password, String uid,String image, String agencyName) {
        super(firstName, lastName, email, phoneNumber, password,uid,image);
        this.agencyName = agencyName;
    }

    public Broker() {
        super();
    }

    // Getter and setter for agencyName
    public String getAgencyName() {
        return agencyName;
    }

    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }
}