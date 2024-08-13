package com.example.homematch.Models;

import com.google.android.material.button.MaterialButton;

public class HouseTypeButton {

    private String houseType;
    private MaterialButton button;

    public HouseTypeButton() {
    }

    public HouseTypeButton(MaterialButton button, String houseType) {
        this.houseType = houseType;
        this.button = button;
    }

    public String getHouseType() {
        return houseType;
    }

    public HouseTypeButton setHouseType(String houseType) {
        this.houseType = houseType;
        return this;
    }

    public MaterialButton getButton() {
        return button;
    }

    public HouseTypeButton setButton(MaterialButton button) {
        this.button = button;
        return this;
    }
}
