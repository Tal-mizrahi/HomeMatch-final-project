package com.example.homematch.interaces;

import com.google.firebase.auth.FirebaseUser;

public interface UserCreationCallBack {
    void onUserCreated(String uid);
    void onUserCreationFailed(Exception exception);

}
