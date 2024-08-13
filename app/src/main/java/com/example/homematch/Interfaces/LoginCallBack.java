package com.example.homematch.Interfaces;

public interface LoginCallBack {

    void onLoginSuccess(String uid);
    void onLoginFailure(Exception exception);

}
