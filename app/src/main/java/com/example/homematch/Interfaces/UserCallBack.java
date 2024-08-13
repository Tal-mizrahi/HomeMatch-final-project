package com.example.homematch.Interfaces;

import com.example.homematch.Models.User;

public interface UserCallBack {
    void onSuccess(User currentUser);
    void onFailure();
}
