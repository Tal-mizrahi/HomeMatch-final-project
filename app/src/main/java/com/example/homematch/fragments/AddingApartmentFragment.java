package com.example.homematch.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;

import com.example.homematch.R;
import com.example.homematch.interaces.ImgCallBack;
import com.example.homematch.utilities.MyDbDataManager;
import com.example.homematch.utilities.MyDbStorageManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class AddingApartmentFragment extends Fragment {

    private MaterialButtonToggleGroup toggleGroup;
    private TextInputLayout homeMatch_LAY_city;
    private TextInputLayout homeMatch_LAY_street;
    private TextInputLayout homeMatch_LAY_street_number;
    private TextInputLayout homeMatch_LAY_postal_code;
    private TextInputLayout homeMatch_LAY_floor_number;
    private TextInputLayout homeMatch_LAY_apt_number;
    private TextInputLayout homeMatch_LAY_balcony_size;
    private TextInputLayout homeMatch_LAY_price;
    private TextInputLayout homeMatch_LAY_area_size;
    private TextInputLayout homeMatch_LAY_number_of_rooms;

    private TextInputEditText homeMatch_INP_price;
    private TextInputEditText homeMatch_INP_balcony_size;
    private TextInputEditText homeMatch_INP_city;
    private TextInputEditText homeMatch_INP_street;
    private TextInputEditText homeMatch_INP_street_number;
    private TextInputEditText homeMatch_INP_postal_code;
    private TextInputEditText homeMatch_INP_floor_number;
    private TextInputEditText homeMatch_INP_number_of_rooms;
    private TextInputEditText homeMatch_INP_apt_number;
    private TextInputEditText homeMatch_INP_area_size;

    private Spinner homeMatch_SPN_apartment_type;

    private LinearLayoutCompat homeMatch_LL_floor_apt;

    private boolean isForSale;

    private CheckBox homeMatch_CHKBOX_garage;
    private CheckBox homeMatch_CHKBOX_balcony;
    private CheckBox homeMatch_CHKBOX_can_smoke;
    private CheckBox homeMatch_CHKBOX_pets_allowed;
    private CheckBox homeMatch_CHKBOX_parking;
    private CheckBox homeMatch_CHKBOX_bills;

    private MaterialButton homeMatch_BTN_finish;

    private AppCompatImageView[] allUploadedImages;
    int numOfImages = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_adding_apartment, container, false);
        findViews(view);
        isForSale = true;
        toggleGroup.check(R.id.homeMatch_BTN_for_sale);
        setCheckBoxUI();
        SetDropDownListener();
        setFormUI();
        return view;
    }

    public void SetDropDownListener() {
        homeMatch_SPN_apartment_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedType = homeMatch_SPN_apartment_type.getSelectedItem().toString();
                // Here you can handle the selected item
                switch (selectedType) {
                    case "Private House":
                        homeMatch_LAY_balcony_size.setHint("Balcony Size");
                        homeMatch_LL_floor_apt.setVisibility(View.GONE);
                        break;
                    case "Apartment":
                    case "Penthouse":
                    case "Duplex":
                        homeMatch_LAY_balcony_size.setHint("Balcony Size");
                        homeMatch_LL_floor_apt.setVisibility(View.VISIBLE);
                        break;
                    case "Garden Apartment":
                        homeMatch_LAY_balcony_size.setHint("Garden Size");
                        homeMatch_LL_floor_apt.setVisibility(View.VISIBLE);
                        homeMatch_CHKBOX_balcony.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle case where no item is selected if necessary
            }
        });

    }

    public void setFormUI(){
        toggleGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (!isChecked)
                return;
            if (checkedId == R.id.homeMatch_BTN_for_sale) {
                isForSale = true;
                showSaleFields();
            } else if (checkedId == R.id.homeMatch_BTN_for_rent) {
                isForSale = false;
                showRentFields();
            }
        });
    }


    public void setCheckBoxUI(){

        homeMatch_CHKBOX_balcony.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                homeMatch_LAY_balcony_size.setVisibility(View.VISIBLE);
            } else {
                homeMatch_LAY_balcony_size.setVisibility(View.GONE);
            }
        });
    }



    public void showSaleFields() {
        resetLayoutUI();
        homeMatch_CHKBOX_pets_allowed.setVisibility(View.GONE);
        homeMatch_CHKBOX_bills.setVisibility(View.GONE);
        homeMatch_CHKBOX_can_smoke.setVisibility(View.GONE);
    }

    public void showRentFields() {
        resetLayoutUI();
        homeMatch_CHKBOX_pets_allowed.setVisibility(View.VISIBLE);
        homeMatch_CHKBOX_bills.setVisibility(View.VISIBLE);
        homeMatch_CHKBOX_can_smoke.setVisibility(View.VISIBLE);
    }

    public void resetLayoutUI() {
        homeMatch_LAY_city.setError(null);
        homeMatch_LAY_street.setError(null);
        homeMatch_LAY_street_number.setError(null);
        homeMatch_LAY_postal_code.setError(null);
        homeMatch_LAY_floor_number.setError(null);
        homeMatch_LAY_apt_number.setError(null);
        homeMatch_LAY_balcony_size.setError(null);
        homeMatch_LAY_price.setError(null);
        homeMatch_LAY_area_size.setError(null);
        homeMatch_INP_number_of_rooms.setError(null);
    }




    private void updateImagesUI() {

        int SZ = allUploadedImages.length;
        for (AppCompatImageView ppgImgHeart : allUploadedImages) {
            ppgImgHeart.setVisibility(View.VISIBLE);
        }

        for (int i = 0; i < SZ - numOfImages; i++) {
            allUploadedImages[i].setVisibility(View.INVISIBLE);
        }

        for (int i = SZ - numOfImages - 1 ; i >= numOfImages ; i--){
            allUploadedImages[i].setImageResource(R.drawable.ic_add_image);
            if( i > numOfImages)
                allUploadedImages[i].setVisibility(View.GONE);

        }

    }


    public boolean validateInput() {
        boolean isValid = true;

        if (TextUtils.isEmpty(homeMatch_INP_city.getText())) {
            homeMatch_LAY_city.setError("City is required");
            isValid = false;
        } else {
            homeMatch_LAY_city.setError(null);
        }

        if (TextUtils.isEmpty(homeMatch_INP_street.getText())) {
            homeMatch_LAY_street.setError("Street is required");
            isValid = false;
        } else {
            homeMatch_LAY_street.setError(null);
        }

        if (TextUtils.isEmpty(homeMatch_INP_street_number.getText())) {
            homeMatch_LAY_street_number.setError("Street number is required");
            isValid = false;
        } else {
            homeMatch_LAY_street_number.setError(null);
        }

        if (TextUtils.isEmpty(homeMatch_INP_postal_code.getText())) {
            homeMatch_LAY_postal_code.setError("Postal code is required");
            isValid = false;
        } else {
            homeMatch_LAY_postal_code.setError(null);
        }

        if (TextUtils.isEmpty(homeMatch_INP_floor_number.getText())) {
            homeMatch_LAY_floor_number.setError("Floor number is required");
            isValid = false;
        } else {
            homeMatch_LAY_floor_number.setError(null);
        }

        if (TextUtils.isEmpty(homeMatch_INP_apt_number.getText())) {
            homeMatch_LAY_apt_number.setError("House number is required");
            isValid = false;
        } else {
            homeMatch_LAY_apt_number.setError(null);
        }

        if (TextUtils.isEmpty(homeMatch_INP_balcony_size.getText())) {
            homeMatch_LAY_balcony_size.setError("Balcony size is required");
            isValid = false;
        } else {
            homeMatch_LAY_balcony_size.setError(null);
        }

        if (TextUtils.isEmpty(homeMatch_INP_price.getText())) {
            homeMatch_LAY_price.setError("Price is required");
            isValid = false;
        } else {
            homeMatch_LAY_price.setError(null);
        }

        if (TextUtils.isEmpty(homeMatch_INP_number_of_rooms.getText())) {
            homeMatch_LAY_number_of_rooms.setError("Price is required");
            isValid = false;
        } else {
            homeMatch_LAY_number_of_rooms.setError(null);
        }

        if (TextUtils.isEmpty(homeMatch_INP_area_size.getText())) {
            homeMatch_LAY_area_size.setError("area size is required");
            isValid = false;
        } else {
            homeMatch_LAY_area_size.setError(null);
        }

        return isValid;
    }



    private void findViews(View view) {
        toggleGroup = view.findViewById(R.id.homeMatch_GRP_for_sale_or_rent);
        homeMatch_LAY_city = view.findViewById(R.id.homeMatch_LAY_city);
        homeMatch_INP_city = view.findViewById(R.id.homeMatch_INP_city);
        homeMatch_LAY_street = view.findViewById(R.id.homeMatch_LAY_street);
        homeMatch_INP_street = view.findViewById(R.id.homeMatch_INP_street);
        homeMatch_LAY_street_number = view.findViewById(R.id.homeMatch_LAY_street_number);
        homeMatch_INP_street_number = view.findViewById(R.id.homeMatch_INP_street_number);
        homeMatch_LAY_postal_code = view.findViewById(R.id.homeMatch_LAY_postal_code);
        homeMatch_INP_postal_code = view.findViewById(R.id.homeMatch_INP_postal_code);
        homeMatch_SPN_apartment_type = view.findViewById(R.id.homeMatch_SPN_apartment_type);
        homeMatch_LAY_floor_number = view.findViewById(R.id.homeMatch_LAY_floor_number);
        homeMatch_INP_floor_number = view.findViewById(R.id.homeMatch_INP_floor_number);
        homeMatch_LAY_number_of_rooms = view.findViewById(R.id.homeMatch_LAY_number_of_rooms);
        homeMatch_INP_number_of_rooms = view.findViewById(R.id.homeMatch_INP_number_of_rooms);
        homeMatch_LAY_apt_number = view.findViewById(R.id.homeMatch_LAY_apt_number);
        homeMatch_INP_apt_number = view.findViewById(R.id.homeMatch_INP_apt_number);
        homeMatch_LAY_balcony_size = view.findViewById(R.id.homeMatch_LAY_balcony_size);
        homeMatch_INP_balcony_size = view.findViewById(R.id.homeMatch_INP_balcony_size);
        homeMatch_LAY_price = view.findViewById(R.id.homeMatch_LAY_price);
        homeMatch_INP_area_size = view.findViewById(R.id.homeMatch_INP_area_size);
        homeMatch_LAY_area_size = view.findViewById(R.id.homeMatch_LAY_area_size);
        homeMatch_INP_price = view.findViewById(R.id.homeMatch_INP_price);
        homeMatch_CHKBOX_garage = view.findViewById(R.id.homeMatch_CHKBOX_garage);
        homeMatch_CHKBOX_balcony = view.findViewById(R.id.homeMatch_CHKBOX_balcony);
        homeMatch_CHKBOX_can_smoke = view.findViewById(R.id.homeMatch_CHKBOX_can_smoke);
        homeMatch_CHKBOX_pets_allowed = view.findViewById(R.id.homeMatch_CHKBOX_pets_allowed);
        homeMatch_CHKBOX_parking = view.findViewById(R.id.homeMatch_CHKBOX_parking);
        homeMatch_CHKBOX_bills = view.findViewById(R.id.homeMatch_CHKBOX_bills);

        allUploadedImages = new AppCompatImageView[] {
            view.findViewById(R.id.homeMatch_BTN_add_image1),
            view.findViewById(R.id.homeMatch_BTN_add_image2),
            view.findViewById(R.id.homeMatch_BTN_add_image3),
            view.findViewById(R.id.homeMatch_BTN_add_image4),
            view.findViewById(R.id.homeMatch_BTN_add_image5)
        };

        homeMatch_BTN_finish = view.findViewById(R.id.homeMatch_BTN_finish);
        homeMatch_LL_floor_apt = view.findViewById(R.id.homeMatch_LL_floor_apt);

    }
}