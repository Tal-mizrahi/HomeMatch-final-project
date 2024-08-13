package com.example.homematch.Interfaces;

import java.util.ArrayList;

public interface ImgListCallBack {
    void onSuccess(ArrayList<String> list);
    void onFailure(Exception exception);

}
