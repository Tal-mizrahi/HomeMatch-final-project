package com.example.homematch.interaces;

import com.example.homematch.models.User;
import com.google.firebase.database.DatabaseReference;

public interface UserCallBack {
    void onSuccess(User currentUser);
    void onFailure();
}
