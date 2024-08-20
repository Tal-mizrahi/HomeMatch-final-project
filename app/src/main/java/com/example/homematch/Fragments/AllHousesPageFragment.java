package com.example.homematch.Fragments;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.homematch.Models.ShowPropertiesType;
import com.example.homematch.R;
import com.example.homematch.Models.HouseTypeButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.textview.MaterialTextView;

public class AllHousesPageFragment extends Fragment {

    private static final String FOR_SALE = "Sale";
    private static final String FOR_RENT = "Rent";
    private static final String PRIVATE_HOUSE = "Private House";
    private static final String APARTMENT = "Apartment";
    private static final String DUPLEX = "Duplex";
    private static final String GARDEN_APARTMENT = "Garden Apartment";
    private static final String PENTHOUSE = "Penthouse";


    private MaterialButtonToggleGroup toggleGroup;


    private HouseTypeButton[] buttons;
    private HouseTypeButton currentHouseType;
    MaterialTextView allHouses_LBL_title;
    private HousesListFragment listFragment;
    //private String currentHouseType;
    private String currentPurchaseType;
    private ShowPropertiesType showPropertiesType;



    public AllHousesPageFragment() {
        // Required empty public constructor
    }

    public AllHousesPageFragment(ShowPropertiesType showPropertiesType) {
        this.showPropertiesType = showPropertiesType;
    }

        @SuppressLint("SetTextI18n")
        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_houses_page, container, false);
        findViews(view);
        if(showPropertiesType == ShowPropertiesType.AGENT_PROPERTIES){
            allHouses_LBL_title.setText("Manage Your Properties");
            allHouses_LBL_title.setGravity(Gravity.CENTER_HORIZONTAL);
        }
        //Initial values
        currentHouseType = buttons[4];
        currentPurchaseType = FOR_SALE;
        currentHouseType.getButton().setBackgroundColor(Color.parseColor("#F56C42"));;
        currentHouseType.getButton().setIconTint(ColorStateList.valueOf(Color.parseColor("#FFE6D5")));;
        toggleGroup.check(R.id.allHouses_BTN_for_sale);
        Log.d("AllHousesPageFragment", "onCreateView: " + showPropertiesType.name());
        listFragment = new HousesListFragment(showPropertiesType);
        listFragment.setHousesListUI(currentPurchaseType, currentHouseType.getHouseType());
        setListeners();
        getChildFragmentManager().beginTransaction()
                .add(R.id.allHouses_LAY_FRAME, listFragment)
                .commit();
        return view;
    }

    private void setListeners() {
        toggleGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (!isChecked)
                return;
            if (checkedId == R.id.allHouses_BTN_for_sale) {
                currentPurchaseType = FOR_SALE;
                listFragment.setHousesListUI(currentPurchaseType, currentHouseType.getHouseType());
            } else if (checkedId == R.id.allHouses_BTN_for_rent) {
                currentPurchaseType = FOR_RENT;
                Log.d("AllHousesPageFragment", "setListeners: " + currentPurchaseType + " " + currentHouseType.getHouseType());
                listFragment.setHousesListUI(currentPurchaseType, currentHouseType.getHouseType());
            }
        });

        for (HouseTypeButton button : buttons){
            button.getButton().setOnClickListener(v -> {
                currentHouseType.getButton().setBackgroundColor(Color.parseColor("#FFE6D5"));
                currentHouseType.getButton().setIconTint(ColorStateList.valueOf(Color.parseColor("#F56C42")));

                button.getButton().setBackgroundColor(Color.parseColor("#F56C42"));
                button.getButton().setIconTint(ColorStateList.valueOf(Color.parseColor("#FFE6D5")));
                currentHouseType = button;
                listFragment.setHousesListUI(currentPurchaseType, currentHouseType.getHouseType());
            });
        }



    }

    public void findViews(View view){

        allHouses_LBL_title=view.findViewById(R.id.allHouses_LBL_title);

        toggleGroup = view.findViewById(R.id.allHouses_GRP_for_sale_or_rent);
        buttons = new HouseTypeButton[] {
                  new HouseTypeButton(view.findViewById(R.id.allHouses_BTN_private_house), PRIVATE_HOUSE)
                , new HouseTypeButton(view.findViewById(R.id.allHouses_BTN_penthouse), PENTHOUSE)
                , new HouseTypeButton(view.findViewById(R.id.allHouses_BTN_apartments), APARTMENT)
                , new HouseTypeButton(view.findViewById(R.id.allHouses_BTN_duplex), DUPLEX)
                , new HouseTypeButton(view.findViewById(R.id.allHouses_BTN_garden_apartment), GARDEN_APARTMENT)};




    }


}