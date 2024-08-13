package com.example.homematch.Interfaces;

import com.example.homematch.Models.House;

import java.util.ArrayList;

public interface HousesListCallBack {
    void onSuccess(ArrayList<House> allHouses);
    void onFailure(Exception exception);
}
