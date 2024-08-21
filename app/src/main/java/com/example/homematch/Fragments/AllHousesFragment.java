package com.example.homematch.Fragments;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.homematch.Adapters.HouseAdapter;
import com.example.homematch.Models.House;
import com.example.homematch.Models.ShowPropertiesType;
import com.example.homematch.R;
import com.example.homematch.Models.HouseTypeButton;
import com.example.homematch.Utilities.MyDbDataManager;
import com.example.homematch.Utilities.MyDbStorageManager;
import com.example.homematch.Utilities.MyDbUserManager;
import com.example.homematch.Utilities.ScheduleOpenHouseDialogManager;
import com.example.homematch.Utilities.ShowDetailDialogManager;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class AllHousesFragment extends Fragment {

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
    private String currentPurchaseType;
    private ShowPropertiesType showPropertiesType;
    private HouseAdapter houseAdapter;
    private ArrayList<House> allHousesList;
    private RecyclerView allHouses_LST_houses;
    private ScheduleOpenHouseDialogManager scheduleOpenHouse;
    private ShowDetailDialogManager showDetailDialogManager;
    private MaterialTextView allHouses_LBL_no_houses;




    public AllHousesFragment() {
        // Required empty public constructor
    }

    public AllHousesFragment(ShowPropertiesType showPropertiesType) {
        this.showPropertiesType = showPropertiesType;
    }

        @SuppressLint("SetTextI18n")
        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_houses, container, false);
        findViews(view);
        if(showPropertiesType == ShowPropertiesType.AGENT_PROPERTIES){
            allHouses_LBL_title.setText("Manage Your Properties");
            allHouses_LBL_title.setGravity(Gravity.CENTER_HORIZONTAL);
        } else if (showPropertiesType == ShowPropertiesType.OPEN_HOUSES_CLIENT) {
            allHouses_LBL_title.setText("My Open Houses");
            allHouses_LBL_title.setGravity(Gravity.CENTER_HORIZONTAL);

        }
            //Initial values
        currentHouseType = buttons[0];
        currentPurchaseType = FOR_SALE;
        currentHouseType.getButton().setBackgroundColor(Color.parseColor("#F56C42"));;
        currentHouseType.getButton().setIconTint(ColorStateList.valueOf(Color.parseColor("#FFE6D5")));;
        toggleGroup.check(R.id.allHouses_BTN_for_sale);
        Log.d("AllHousesPageFragment", "onCreateView: " + showPropertiesType.name());
        scheduleOpenHouse = new ScheduleOpenHouseDialogManager(this.getContext());
        showDetailDialogManager = new ShowDetailDialogManager(this.getContext());
        allHousesList = new ArrayList<>();
        initAdapter();
        setListeners();
        setAdapterListeners();
        return view;
    }

    private void setListeners() {
        toggleGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (!isChecked)
                return;
            if (checkedId == R.id.allHouses_BTN_for_sale) {
                currentPurchaseType = FOR_SALE;
                setHousesListUI(currentPurchaseType, currentHouseType.getHouseType());
            } else if (checkedId == R.id.allHouses_BTN_for_rent) {
                currentPurchaseType = FOR_RENT;
                Log.d("AllHousesPageFragment", "setListeners: " + currentPurchaseType + " " + currentHouseType.getHouseType());
                setHousesListUI(currentPurchaseType, currentHouseType.getHouseType());
            }
        });

        for (HouseTypeButton button : buttons){
            button.getButton().setOnClickListener(v -> {
                currentHouseType.getButton().setBackgroundColor(Color.parseColor("#FFE6D5"));
                currentHouseType.getButton().setIconTint(ColorStateList.valueOf(Color.parseColor("#F56C42")));

                button.getButton().setBackgroundColor(Color.parseColor("#F56C42"));
                button.getButton().setIconTint(ColorStateList.valueOf(Color.parseColor("#FFE6D5")));
                currentHouseType = button;
                setHousesListUI(currentPurchaseType, currentHouseType.getHouseType());
            });
        }

    }

    public void setAdapterListeners(){
        houseAdapter.setHouseDetailsCallBack(house ->{
            showDetailDialogManager.showHouseDetailsDialog(AllHousesFragment.this.getContext(), house);
        });

        houseAdapter.setOpenHouseSignUpCallBack((house, position, isSignUp) -> {
            Log.d("house", house.toString());
            String clientId = MyDbUserManager.getInstance().getUidOfCurrentUser();
            if(isSignUp){
                house.addClientToOpenHouse(clientId);
                houseAdapter.notifyItemChanged(position);
            } else {
                house.removeClientFromOpenHouse(clientId);
                if(showPropertiesType.equals(ShowPropertiesType.OPEN_HOUSES_CLIENT)) {
                    allHousesList.remove(position);
                    houseAdapter.notifyItemRemoved(position);
                } else {
                    houseAdapter.notifyItemChanged(position);
                }
            }
            MyDbDataManager.getInstance().setHouse(house);


        });

        houseAdapter.setHouseDeleteCallBack((house, position) -> {
            MyDbDataManager.getInstance().housePurchase(house.getPurchaseType(), house.getHouseType(), house.getUuid(), () -> {
                allHousesList.remove(position);
                houseAdapter.notifyItemRemoved(position);
                MyDbDataManager.getInstance().setUserPropertiesAmount(house.getPurchaseType(), false, AllHousesFragment.this.getContext());
                MyDbStorageManager.getInstance().deleteFile(house);
            });
        });

        if(showPropertiesType.equals(ShowPropertiesType.AGENT_PROPERTIES)){
            houseAdapter.setScheduleCallBack((house, position) -> {
                Log.d("Schedule open house", "1 " + allHousesList.get(position).toString());
                scheduleOpenHouse.showDialog(house, position, this.getContext());
                houseAdapter.notifyItemChanged(position);
            });

            houseAdapter.setCancelOpenHouseCallBack((house, position) -> {
                house.resetOpenHouseData();
                houseAdapter.notifyItemChanged(position);
                MyDbDataManager.getInstance().setHouse(house);
            });
        }
        scheduleOpenHouse.setScheduleCallBack((house, position) -> {
            houseAdapter.notifyItemChanged(position);
            Log.d("Schedule open house", "2 " + allHousesList.get(position).toString());
            MyDbDataManager.getInstance().setHouse( allHousesList.get(position));
            Log.d("Schedule open house", "3 " + allHousesList.get(position).toString());
        });

    }


    public void initAdapter() {
        houseAdapter = new HouseAdapter(getContext(), allHousesList, showPropertiesType);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        allHouses_LST_houses.setLayoutManager(linearLayoutManager);
        allHouses_LST_houses.setAdapter(houseAdapter);



    }

    public void setHousesListUI(String currentPurchaseType, String currentHouseType) {
        Log.d("HousesListFragment", "setAllHousesUI: " + currentPurchaseType + " " + currentHouseType);
        if(showPropertiesType.equals(ShowPropertiesType.ALL_HOUSES_AGENT) || showPropertiesType.equals(ShowPropertiesType.ALL_HOUSES_CLIENT)){
            setAllHousesUI(currentPurchaseType, currentHouseType);
        } else if(showPropertiesType.equals(ShowPropertiesType.AGENT_PROPERTIES)) {
            setAgentPropertiesUI(currentPurchaseType, currentHouseType);
        } else if(showPropertiesType.equals(ShowPropertiesType.OPEN_HOUSES_CLIENT)){
            setClientOpenHousesUI(currentPurchaseType, currentHouseType);
        }

    }

    public void setAllHousesUI(String currentPurchaseType, String currentHouseType) {
        MyDbDataManager.getInstance().getHouseList(currentPurchaseType, currentHouseType, new MyDbDataManager.HousesListCallBack() {

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onSuccess(ArrayList<House> allHouses) {
                setAllHousesList(allHouses);

            }

            @Override
            public void onFailure(Exception exception) {
                Toast.makeText(AllHousesFragment.this.getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setAgentPropertiesUI(String currentPurchaseType, String currentHouseType) {
        MyDbDataManager.getInstance().loadAgentProperties(currentPurchaseType, currentHouseType, new MyDbDataManager.HousesListCallBack() {

            @Override
            public void onSuccess(ArrayList<House> allHouses) {
                setAllHousesList(allHouses);
            }

            @Override
            public void onFailure(Exception exception) {
                Toast.makeText(AllHousesFragment.this.getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setClientOpenHousesUI(String currentPurchaseType, String currentHouseType) {
        MyDbDataManager.getInstance().loadClientOpenHouses(currentPurchaseType, currentHouseType, new MyDbDataManager.HousesListCallBack() {

            @Override
            public void onSuccess(ArrayList<House> allHouses) {
                setAllHousesList(allHouses);
            }

            @Override
            public void onFailure(Exception exception) {
                Toast.makeText(AllHousesFragment.this.getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setAllHousesList(ArrayList<House> allHouses){
        allHousesList.clear();
        allHousesList.addAll(allHouses);
        houseAdapter.notifyDataSetChanged();
        houseAdapter.setAllHousesList(allHousesList);
        if(allHousesList.isEmpty()){
            allHouses_LBL_no_houses.setVisibility(View.VISIBLE);
        } else {
            allHouses_LBL_no_houses.setVisibility(View.GONE);
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

        allHouses_LST_houses = view.findViewById(R.id.allHouses_LST_houses);
        allHouses_LBL_no_houses = view.findViewById(R.id.allHouses_LBL_no_houses);

    }


}