package com.example.homematch.interaces;

public interface LoginCallBack {

    void onLoginSuccess(String uid);
    void onLoginFailure(Exception exception);

}
