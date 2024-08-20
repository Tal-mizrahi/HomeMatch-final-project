package com.example.homematch.Models;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.google.android.material.textview.MaterialTextView;

public class Feature {

    MaterialTextView featureText;
    AppCompatImageView featureImage;
    LinearLayoutCompat featureLayout;
    boolean isRent;
    boolean isEnabled;

    public Feature(boolean isEnabled, MaterialTextView featureText, AppCompatImageView featureImage, boolean isRent) {
        this.featureText = featureText;
        this.featureImage = featureImage;
        this.isEnabled = isEnabled;
        this.isRent = isRent;
    }

    public Feature setFeatureLayout(LinearLayoutCompat featureLayout) {
        this.featureLayout = featureLayout;
        return this;
    }

    public MaterialTextView getFeatureText() {
        return featureText;
    }

    public AppCompatImageView getFeatureImage() {
        return featureImage;
    }

    public LinearLayoutCompat getFeatureLayout() {
        return featureLayout;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public boolean isRent() {
        return isRent;
    }
}
