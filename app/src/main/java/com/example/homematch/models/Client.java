package com.example.homematch.models;

public class Client extends User {

    public Client() {
        super();
    }

    public Client(String firstName, String lastName, String email, String phoneNumber
            , String password, String uid, String imageUrl) {
        super(firstName, lastName, email, phoneNumber, password, uid, imageUrl);
    }
}

