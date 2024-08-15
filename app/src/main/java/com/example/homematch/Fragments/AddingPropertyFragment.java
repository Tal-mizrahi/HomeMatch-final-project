package com.example.homematch.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homematch.Adapters.ImageAdapter;
import com.example.homematch.R;
import com.example.homematch.Activities.LoginActivity;
import com.example.homematch.Interfaces.HouseAddedCallBack;
import com.example.homematch.Interfaces.ImgCallBack;
import com.example.homematch.Interfaces.ImgListCallBack;
import com.example.homematch.Models.Apartment;
import com.example.homematch.Models.House;
import com.example.homematch.Models.PrivateHouse;
import com.example.homematch.Utilities.FullScreenManager;
import com.example.homematch.Utilities.MyDbDataManager;
import com.example.homematch.Utilities.MyDbStorageManager;
import com.example.homematch.Utilities.MyDbUserManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.UUID;

public class AddingPropertyFragment extends Fragment {

    private MaterialButtonToggleGroup toggleGroup;
    private TextInputLayout homeMatch_LAY_city;
    private TextInputLayout homeMatch_LAY_street;
    private TextInputLayout homeMatch_LAY_street_number;
    private TextInputLayout homeMatch_LAY_postal_code;
    private TextInputLayout homeMatch_LAY_floor_number;
    private TextInputLayout homeMatch_LAY_apt_number;
    private TextInputLayout homeMatch_LAY_price;
    private TextInputLayout homeMatch_LAY_area_size;
    private TextInputLayout homeMatch_LAY_number_of_rooms;
    private TextInputLayout homeMatch_LAY_balcony_size;

    private TextInputEditText homeMatch_INP_price;
    private TextInputEditText homeMatch_INP_city;
    private TextInputEditText homeMatch_INP_street;
    private TextInputEditText homeMatch_INP_street_number;
    private TextInputEditText homeMatch_INP_postal_code;
    private TextInputEditText homeMatch_INP_floor_number;
    private TextInputEditText homeMatch_INP_number_of_rooms;
    private TextInputEditText homeMatch_INP_apt_number;
    private TextInputEditText homeMatch_INP_area_size;
    private TextInputEditText homeMatch_INP_description;
    private TextInputEditText homeMatch_INP_balcony_size;

    private Spinner homeMatch_SPN_apartment_type;

    private LinearLayoutCompat homeMatch_LL_floor_apt;
    private RelativeLayout homeMatch_REL_main;
    private CardView homeMatch_CARD_loading;


    private CheckBox homeMatch_CHKBOX_garage;
    private CheckBox homeMatch_CHKBOX_balcony;
    private CheckBox homeMatch_CHKBOX_can_smoke;
    private CheckBox homeMatch_CHKBOX_pets_allowed;
    private CheckBox homeMatch_CHKBOX_parking;
    private CheckBox homeMatch_CHKBOX_bills;
    private CheckBox homeMatch_CHKBOX_elevator;
    private CheckBox homeMatch_CHKBOX_protected_room;

    private MaterialButton homeMatch_BTN_finish;


    private  RecyclerView homeMatch_LST_images;

    private AppCompatImageView homeMatch_BTN_add_image;
    private MaterialTextView homeMatch_LBL_images_count;

    private boolean isForSale;
    private ImageAdapter imageAdapter;
    private ArrayList<Uri> imagesUri;
    private HouseAddedCallBack houseAddedCallBack;
    private static final int MAX_SELECTION = 5;
    private int imgSelected = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_adding_property, container, false);
        findViews(view);
        isForSale = true;
        imagesUri = new ArrayList<>();
        toggleGroup.check(R.id.homeMatch_BTN_for_sale);
        showSaleFields();
        Log.d("AddingApartmentFragment", "onCreateView: " + toggleGroup.getCheckedButtonId());
        SetDropDownListener();
        setFormUI();
        initAdapter();
        setCheckBoxUI();
        homeMatch_BTN_add_image.setOnClickListener(v -> uploadImages());

        homeMatch_BTN_finish.setOnClickListener(v -> addingApartment());
        return view;
    }

    public void setHouseAddedCallBack(HouseAddedCallBack houseAddedCallBack) {
        this.houseAddedCallBack = houseAddedCallBack;
    }

    public void addingApartment() {
        if(validateInput()){
            FullScreenManager.getInstance().hideKeyboard(this.requireActivity());
            homeMatch_REL_main.setAlpha(0.5f);
            homeMatch_CARD_loading.setVisibility(View.VISIBLE);

            this.requireActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            String houseType = homeMatch_SPN_apartment_type.getSelectedItem().toString();
            String uuid = UUID.randomUUID().toString();

            House house;
//            house = new Apartment(uuid, houseType)
//                    .setFloorNumber(Integer.parseInt(homeMatch_INP_floor_number.getText().toString()))
//                    .setFloorNumber(Integer.parseInt(homeMatch_INP_apt_number.getText().toString()));


            if(houseType.equals("Private House"))
                house = new PrivateHouse(uuid, houseType);
            else {
                house = new Apartment(uuid, houseType)
                        .setFloorNumber(Integer.parseInt(homeMatch_INP_floor_number.getText().toString()))
                        .setApartmentNumber(Integer.parseInt(homeMatch_INP_apt_number.getText().toString()));

                //house.setFloorNumber(Integer.parseInt(homeMatch_INP_floor_number.getText().toString()));

            }

            MyDbStorageManager.getInstance().uploadHouseImages(imagesUri, uuid, new ImgListCallBack() {
                @Override
                public void onSuccess(ArrayList<String> list) {
                    if (list.size() == imagesUri.size()) {
                        setHouse(house, list);
                        Log.d("AddingApartmentFragment", "onSuccess: " + house.toString());
                        Log.d("AddingApartmentFragment", "ImagesUrl: " + list);
                        MyDbDataManager.getInstance().setHouse(house);
                        AddingPropertyFragment.this.requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        if(houseAddedCallBack != null)
                            houseAddedCallBack.onHouseAdded();
                    } else {
                        startActivity(new Intent(AddingPropertyFragment.this.getContext(), LoginActivity.class)); // Redirect to login if not logged in
                        Log.d("AddingApartmentFragment", "onSuccess: " + house.toString());
                        AddingPropertyFragment.this.requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                    }
                }

                @Override
                public void onFailure(Exception exception) {
                    Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void setHouse(House house, ArrayList<String> imagesUrl) {
        String purchaseType = isForSale ? "Sale" : "Rent";

        house.setPurchaseType(purchaseType);
        house.setAreaSize(Integer.parseInt(homeMatch_INP_area_size.getText().toString()));
        house.setPrice(Integer.parseInt(homeMatch_INP_price.getText().toString()));

        house.setNumberOfRooms(Integer.parseInt(homeMatch_INP_number_of_rooms.getText().toString()));
        house.setStreetNumber(Integer.parseInt(homeMatch_INP_street_number.getText().toString()));
        house.setPostalCode(Integer.parseInt(homeMatch_INP_postal_code.getText().toString()));

        house.setStreet(homeMatch_INP_street.getText().toString());
        house.setCity(homeMatch_INP_city.getText().toString());


        String agentId = MyDbUserManager.getInstance().getUidOfCurrentUser();
        if(agentId == null){
            Toast.makeText(this.getContext(), "There is no user connected", Toast.LENGTH_SHORT).show();
            Log.d("AddingApartmentFragment", "setHouse: There is no user connected");
            startActivity(new Intent(this.getContext(), LoginActivity.class)); // Redirect to login if not logged in
            this.requireActivity().finish();
        } else {
            house.setAgentId(agentId);
        }

        house.setHasProtectedRoom(homeMatch_CHKBOX_protected_room.isChecked());
        house.setHasElevator(homeMatch_CHKBOX_elevator.isChecked());
        house.setHasGarage(homeMatch_CHKBOX_garage.isChecked());
        house.setHasParking(homeMatch_CHKBOX_parking.isChecked());
//        if(homeMatch_CHKBOX_balcony.isChecked() || houseType.equals("Garden Apartment"))
//        {
//            house.setHasBalcony(true);
//            house.setBalconyOrGardenSize(Double.parseDouble(homeMatch_INP_balcony_size.getText().toString()));
//
//        } else {
//            house.setHasBalcony(false);
//            house.setBalconyOrGardenSize(null);
//        }
        house.setHasBalcony(homeMatch_CHKBOX_balcony.isChecked());
        house.setBalconyOrGardenSize(Integer.parseInt(homeMatch_INP_balcony_size.getText().toString()));
        if (purchaseType.equals("Rent")){
            house.setBillsIncluded(homeMatch_CHKBOX_bills.isChecked());
            house.setPetsAllowed(homeMatch_CHKBOX_pets_allowed.isChecked());
            house.setCanSmoke(homeMatch_CHKBOX_can_smoke.isChecked());
        }

        house.setImagesUrl(imagesUrl);
        house.setDescription(homeMatch_INP_description.getText().toString());

    }



    public void initAdapter(){
        setImgToSelect();
        imageAdapter = new ImageAdapter(imagesUri, this.getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        homeMatch_LST_images.setLayoutManager(linearLayoutManager);
        homeMatch_LST_images.setAdapter(imageAdapter);

        imageAdapter.setImgRemovedCallBack((remove) -> {
            updateUI();

        });
    }


public void setImgToSelect(){
    imgSelected = imagesUri.size();
    Log.d("PhotoPicker", "imgSelected: " + imgSelected);
    Log.d("PhotoPicker", "imagesUri.size(): " + imagesUri.size());
}



    ActivityResultLauncher<PickVisualMediaRequest> pickMultipleMedia =
            registerForActivityResult(new ActivityResultContracts.PickMultipleVisualMedia(MAX_SELECTION - imgSelected), uris -> {
                Log.d("PhotoPicker", "Number of items to select: " + (MAX_SELECTION - imgSelected));
                if (!uris.isEmpty()) {
                    //imagesUri.clear();
                    if(uris.size() > MAX_SELECTION - imgSelected){
                        Toast.makeText(getContext(), "You can select only " + (MAX_SELECTION - imgSelected) + " more images", Toast.LENGTH_SHORT).show();
                    } else {
                        imagesUri.addAll(uris);
                        setImgToSelect();
                        Log.d("PhotoPicker", "Number of items selected: " + uris.size());
                        imageAdapter.notifyDataSetChanged();
                        MyDbStorageManager.getInstance().uploadHouseImage(imagesUri.get(0), "test", "test", new ImgCallBack() {
                            @Override
                            public void onSuccess(String imageUrl) {
                                Toast.makeText(AddingPropertyFragment.this.getContext(), "Uploaded success " + imageUrl, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Exception exception) {
                                Toast.makeText(AddingPropertyFragment.this.getContext(), "Uploaded failed " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    updateUI();
                } else {
                    Log.d("PhotoPicker", "No media selected");
                    Toast.makeText(getContext(), "No media selected", Toast.LENGTH_SHORT).show();
                }
            });


    public void uploadImages() {
        pickMultipleMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }





@SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
private void updateUI() {
    imgSelected = imagesUri.size();
    homeMatch_LBL_images_count.setText("(" + imgSelected + "/" + MAX_SELECTION + ")");
    if(imgSelected >= MAX_SELECTION){
        homeMatch_BTN_add_image.setVisibility(View.GONE);
    } else
        homeMatch_BTN_add_image.setVisibility(View.VISIBLE);
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
        homeMatch_LAY_price.setError(null);
        homeMatch_LAY_area_size.setError(null);
        homeMatch_INP_number_of_rooms.setError(null);
    }



    public boolean validateInput() {
        boolean isValid = true;

        if(homeMatch_CHKBOX_balcony.isChecked()) {
            if (TextUtils.isEmpty(homeMatch_INP_balcony_size.getText())) {
                homeMatch_LAY_balcony_size.setError("Balcony size is required");
                isValid = false;
            } else {
                homeMatch_LAY_balcony_size.setError(null);
            }
        }

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
        homeMatch_LAY_price = view.findViewById(R.id.homeMatch_LAY_price);
        homeMatch_INP_area_size = view.findViewById(R.id.homeMatch_INP_area_size);
        homeMatch_LAY_area_size = view.findViewById(R.id.homeMatch_LAY_area_size);
        homeMatch_INP_price = view.findViewById(R.id.homeMatch_INP_price);
        homeMatch_INP_description = view.findViewById(R.id.homeMatch_INP_description);
        homeMatch_LAY_balcony_size = view.findViewById(R.id.homeMatch_LAY_balcony_size);
        homeMatch_INP_balcony_size = view.findViewById(R.id.homeMatch_INP_balcony_size);

        homeMatch_CHKBOX_garage = view.findViewById(R.id.homeMatch_CHKBOX_garage);
        homeMatch_CHKBOX_balcony = view.findViewById(R.id.homeMatch_CHKBOX_balcony);
        homeMatch_CHKBOX_can_smoke = view.findViewById(R.id.homeMatch_CHKBOX_can_smoke);
        homeMatch_CHKBOX_pets_allowed = view.findViewById(R.id.homeMatch_CHKBOX_pets_allowed);
        homeMatch_CHKBOX_parking = view.findViewById(R.id.homeMatch_CHKBOX_parking);
        homeMatch_CHKBOX_bills = view.findViewById(R.id.homeMatch_CHKBOX_bills);
        homeMatch_CHKBOX_elevator = view.findViewById(R.id.homeMatch_CHKBOX_elevator);
        homeMatch_CHKBOX_protected_room = view.findViewById(R.id.homeMatch_CHKBOX_protected_room);

        homeMatch_LST_images = view.findViewById(R.id.homeMatch_LST_images);

        homeMatch_BTN_add_image = view.findViewById(R.id.homeMatch_BTN_add_image);
        homeMatch_LBL_images_count = view.findViewById(R.id.homeMatch_LBL_images_count);

        homeMatch_BTN_finish = view.findViewById(R.id.homeMatch_BTN_finish);
        homeMatch_LL_floor_apt = view.findViewById(R.id.homeMatch_LL_floor_apt);

        homeMatch_REL_main = view.findViewById(R.id.homeMatch_REL_main);
        homeMatch_CARD_loading = view.findViewById(R.id.homeMatch_CARD_loading);

    }
}