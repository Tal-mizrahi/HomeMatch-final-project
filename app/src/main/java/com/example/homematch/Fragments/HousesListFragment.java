package com.example.homematch.Fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.homematch.Adapters.HouseAdapter;
import com.example.homematch.R;
import com.example.homematch.Interfaces.HousesListCallBack;
import com.example.homematch.Interfaces.UserCallBack;
import com.example.homematch.Models.Agent;
import com.example.homematch.Models.House;
import com.example.homematch.Models.User;
import com.example.homematch.Utilities.MyDbDataManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class HousesListFragment extends Fragment {


    private HouseAdapter houseAdapter;
    private ArrayList<House> allHousesList;
    RecyclerView allHouses_LST_houses;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_houses_list, container, false);

        allHousesList = new ArrayList<>();
        houseAdapter = new HouseAdapter(getContext(), allHousesList);
        allHouses_LST_houses = view.findViewById(R.id.allHouses_LST_houses);
        initAdapter();

        return view;
    }


    public void initAdapter(){
        houseAdapter = new HouseAdapter(getContext(), allHousesList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        allHouses_LST_houses.setLayoutManager(linearLayoutManager);
        allHouses_LST_houses.setAdapter(houseAdapter);

        houseAdapter.setHouseDetailsCallBack(this::showHouseDetailsDialog);

    }

    public void setAllHousesUI(String currentPurchaseType, String currentHouseType) {

        Log.d("HousesListFragment", "setAllHousesUI: " + currentPurchaseType + " " + currentHouseType);
        MyDbDataManager.getInstance().getHouseList(currentPurchaseType, currentHouseType, new HousesListCallBack() {

            @Override
            public void onSuccess(ArrayList<House> allHouses) {
                allHousesList.clear();
                allHousesList.addAll(allHouses);
                houseAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Exception exception) {
                Toast.makeText(HousesListFragment.this.getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    @SuppressLint("SetTextI18n")
    public void showHouseDetailsDialog(House house) {
        Dialog dialog = new Dialog(this.getContext());
        dialog.setContentView(R.layout.house_details_dialog);

        ImageSlider houseDetails_image_slider = dialog.findViewById(R.id.houseDetails_image_slider);
        ArrayList<SlideModel> imageList = new ArrayList<>();
        for(String imageUrl : house.getImagesUrl()){
            imageList.add(new SlideModel(imageUrl, ScaleTypes.CENTER_CROP));
        }
        houseDetails_image_slider.setImageList(imageList);

        ShapeableImageView houseDetails_IMG_favorite = dialog.findViewById(R.id.houseDetails_IMG_favorite);

        MaterialButton houseDetails_LBL_status = dialog.findViewById(R.id.houseDetails_LBL_status);
        houseDetails_LBL_status.setText("For " + house.getPurchaseType());

        MaterialButton houseDetails_LBL_property_type = dialog.findViewById(R.id.houseDetails_LBL_property_type);
        houseDetails_LBL_property_type.setText(house.getHouseType());

        MaterialTextView houseDetails_LBL_price = dialog.findViewById(R.id.houseDetails_LBL_price);
        houseDetails_LBL_price.setText("₪" + formatWithCommas(house.getPrice()));


        MaterialTextView houseDetails_LBL_address = dialog.findViewById(R.id.houseDetails_LBL_address);
        houseDetails_LBL_address.setText(house.getStreet() + " " + house.getStreetNumber() + ", " + house.getCity());

        MaterialTextView houseDetails_LBL_description = dialog.findViewById(R.id.houseDetails_LBL_description);
        if(house.getDescription() != null)
            houseDetails_LBL_description.setText(house.getDescription());
        else
            houseDetails_LBL_description.setVisibility(View.GONE);


        MaterialTextView houseDetails_LBL_size = dialog.findViewById(R.id.houseDetails_LBL_area_size);
        houseDetails_LBL_size.setText(formatWithCommas(house.getAreaSize()) + " m²");

        MaterialTextView houseDetails_LBL_rooms = dialog.findViewById(R.id.houseDetails_LBL_rooms);
        houseDetails_LBL_rooms.setText(formatWithCommas(house.getNumberOfRooms()) + " Rooms");


        // Additional features of house
        MaterialTextView houseDetails_TXT_protected = dialog.findViewById(R.id.houseDetails_TXT_protected);
        AppCompatImageView houseDetails_IMG_protected = dialog.findViewById(R.id.houseDetails_IMG_protected);


        if(!house.isHasProtectedRoom()){
            houseDetails_TXT_protected.setTextColor(Color.parseColor("#999898"));
            houseDetails_IMG_protected.setColorFilter(ContextCompat.getColor(this.getContext(), R.color.grey_light));
        }



        MaterialTextView houseDetails_TXT_garage = dialog.findViewById(R.id.houseDetails_TXT_garage);
        AppCompatImageView houseDetails_IMG_garage = dialog.findViewById(R.id.houseDetails_IMG_garage);
        if(!house.isHasGarage()){
            houseDetails_TXT_garage.setTextColor(Color.parseColor("#999898"));
            houseDetails_IMG_garage.setColorFilter(ContextCompat.getColor(this.getContext(), R.color.grey_light));
        }

        MaterialTextView houseDetails_TXT_balcony = dialog.findViewById(R.id.houseDetails_TXT_balcony);
        AppCompatImageView houseDetails_IMG_balcony = dialog.findViewById(R.id.houseDetails_IMG_balcony);
        MaterialCardView houseDetails_CARD_balcony_size = dialog.findViewById(R.id.houseDetails_CARD_balcony_size);
        MaterialTextView houseDetails_LBL_balcony_size = dialog.findViewById(R.id.houseDetails_LBL_balcony_size);

        if(!house.isHasBalcony()) {
            houseDetails_TXT_balcony.setTextColor(Color.parseColor("#999898"));
            houseDetails_IMG_balcony.setColorFilter(ContextCompat.getColor(this.getContext(), R.color.grey_light));
            houseDetails_CARD_balcony_size.setVisibility(View.GONE);
        } else {
            houseDetails_LBL_balcony_size.setText(formatWithCommas(house.getBalconyOrGardenSize()) + " m²");
        }

        MaterialTextView houseDetails_TXT_elevator = dialog.findViewById(R.id.houseDetails_TXT_elevator);
        AppCompatImageView houseDetails_IMG_elevator = dialog.findViewById(R.id.houseDetails_IMG_elevator);
        if(!house.isHasElevator()){
            houseDetails_TXT_elevator.setTextColor(Color.parseColor("#999898"));
            houseDetails_IMG_elevator.setColorFilter(ContextCompat.getColor(this.getContext(), R.color.grey_light));
        }

        MaterialTextView houseDetails_TXT_parking = dialog.findViewById(R.id.houseDetails_TXT_parking);
        AppCompatImageView houseDetails_IMG_parking = dialog.findViewById(R.id.houseDetails_IMG_parking);
        if(!house.isHasParking()){
            houseDetails_TXT_parking.setTextColor(Color.parseColor("#999898"));
            houseDetails_IMG_parking.setColorFilter(ContextCompat.getColor(this.getContext(), R.color.grey_light));
        }

        MaterialTextView houseDetails_TXT_smoke = dialog.findViewById(R.id.houseDetails_TXT_smoke);
        MaterialTextView houseDetails_TXT_pets = dialog.findViewById(R.id.houseDetails_TXT_pets);
        MaterialTextView houseDetails_TXT_bills = dialog.findViewById(R.id.houseDetails_TXT_bills);

        AppCompatImageView houseDetails_IMG_smoke = dialog.findViewById(R.id.houseDetails_IMG_smoke);
        AppCompatImageView houseDetails_IMG_pets = dialog.findViewById(R.id.houseDetails_IMG_pets);
        AppCompatImageView houseDetails_IMG_bills = dialog.findViewById(R.id.houseDetails_IMG_bills);

        LinearLayoutCompat houseDetails_LAY_smoke = dialog.findViewById(R.id.houseDetails_LAY_smoke);
        LinearLayoutCompat houseDetails_LAY_pets = dialog.findViewById(R.id.houseDetails_LAY_pets);
        LinearLayoutCompat houseDetails_LAY_bills = dialog.findViewById(R.id.houseDetails_LAY_bills);

        if(house.getPurchaseType().equals("Sale")){
            houseDetails_LAY_smoke.setVisibility(View.GONE);
            houseDetails_LAY_pets.setVisibility(View.GONE);
            houseDetails_LAY_bills.setVisibility(View.GONE);
        } else { // The house is for Rent
             if(!house.isCanSmoke()) {
                 houseDetails_TXT_smoke.setTextColor(Color.parseColor("#999898"));
                 houseDetails_IMG_smoke.setColorFilter(ContextCompat.getColor(this.getContext(), R.color.grey_light));
             }

             if(!house.isPetsAllowed()) {
                 houseDetails_TXT_pets.setTextColor(Color.parseColor("#999898"));
                 houseDetails_IMG_pets.setColorFilter(ContextCompat.getColor(this.getContext(), R.color.grey_light));
             }

             if(!house.isBillsIncluded()) {
                 houseDetails_TXT_bills.setTextColor(Color.parseColor("#999898"));
                 houseDetails_IMG_bills.setColorFilter(ContextCompat.getColor(this.getContext(), R.color.grey_light));
             }
        }

        getAgentInfo(house, dialog);
        MaterialButton houseDetails_BTN_close = dialog.findViewById(R.id.houseDetails_BTN_close);
        houseDetails_BTN_close.setOnClickListener( v -> {
            dialog.dismiss();
        });

        dialog.show();



    }

    public void getAgentInfo(House house, Dialog dialog) {

        ShapeableImageView houseDetails_IMG_agent = dialog.findViewById(R.id.houseDetails_IMG_agent);
        MaterialTextView houseDetails_LBL_agent_name = dialog.findViewById(R.id.houseDetails_LBL_agent_name);
        MaterialTextView houseDetails_LBL_agent_agency = dialog.findViewById(R.id.houseDetails_LBL_agent_agency);
        MaterialTextView houseDetails_LBL_agent_phone = dialog.findViewById(R.id.houseDetails_LBL_agent_phone);

        MyDbDataManager.getInstance().getUser("Broker", house.getBrokerId(), new UserCallBack() {
            @Override
            public void onSuccess(User currentUser) {

                Agent broker = (Agent) currentUser;
                String imageUrl = broker.getImageUrl();
                if (imageUrl != null) {
                    Glide.with(dialog.getContext())
                            .load(imageUrl)
                            .placeholder(R.drawable.img_white)
                            .centerCrop()
                            .into(houseDetails_IMG_agent);


                    houseDetails_LBL_agent_name.setText(broker.getFirstName() + " " + broker.getLastName());
                    houseDetails_LBL_agent_agency.setText(broker.getAgencyName());
                    houseDetails_LBL_agent_phone.setText(broker.getPhoneNumber());
                }
            }

            @Override
            public void onFailure() {
                Toast.makeText(HousesListFragment.this.getContext(), "Agent not found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String formatWithCommas(int number) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        return numberFormat.format(number);
    }
}