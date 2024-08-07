package com.example.homematch.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.homematch.R;
import com.google.android.material.button.MaterialButton;

public class AllHousesPageFragment extends Fragment {

MaterialButton allHouses_BTN_private_house;
MaterialButton allHouses_BTN_apartments;
MaterialButton allHouses_BTN_penthouse;
MaterialButton allHouses_BTN_duplex;
MaterialButton allHouses_BTN_garden_apartment;
HousesListFragment listFragment;


        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_houses_page, container, false);
        findViews(view);
        listFragment = new HousesListFragment();

        getChildFragmentManager().beginTransaction()
                .add(R.id.allHouses_LAY_FRAME, listFragment)
                .commit();
        return view;
    }

    public void findViews(View view){
        allHouses_BTN_private_house = view.findViewById(R.id.allHouses_BTN_private_house);
        allHouses_BTN_apartments = view.findViewById(R.id.allHouses_BTN_apartments);
        allHouses_BTN_penthouse = view.findViewById(R.id.allHouses_BTN_penthouse);
        allHouses_BTN_duplex = view.findViewById(R.id.allHouses_BTN_duplex);
        allHouses_BTN_garden_apartment = view.findViewById(R.id.allHouses_BTN_garden_apartment);

    }
}