
package com.example.homematch.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.homematch.Adapters.HouseAdapter;
import com.example.homematch.Interfaces.ScheduleCallBack;
import com.example.homematch.Models.ShowPropertiesType;
import com.example.homematch.R;
import com.example.homematch.Interfaces.HousesListCallBack;
import com.example.homematch.Utilities.ScheduleOpenHouseDialogManager;
import com.example.homematch.Utilities.ShowDetailDialogManager;
import com.example.homematch.Models.House;
import com.example.homematch.Utilities.MyDbDataManager;

import java.util.ArrayList;

public class HousesListFragment extends Fragment {

    private HouseAdapter houseAdapter;
    private ArrayList<House> allHousesList;
    private RecyclerView allHouses_LST_houses;
    private ShowPropertiesType showPropertiesType;
    private ScheduleOpenHouseDialogManager scheduleOpenHouse;


    public HousesListFragment() {
    }

    public HousesListFragment(ShowPropertiesType showPropertiesType) {
        this.showPropertiesType = showPropertiesType;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_houses_list, container, false);
        scheduleOpenHouse = new ScheduleOpenHouseDialogManager(this.getContext());
        allHousesList = new ArrayList<>();
        Log.d("HousesList", "onCreateView: " + showPropertiesType.name());
       // houseAdapter = new HouseAdapter(getContext(), allHousesList, showPropertiesType);
        allHouses_LST_houses = view.findViewById(R.id.allHouses_LST_houses);
        initAdapter();

        scheduleOpenHouse.setScheduleCallBack(new ScheduleCallBack() {
            @Override
            public void onScheduleOpenHouse(House house, int position) {
                houseAdapter.notifyItemChanged(position);
                Log.d("Schedule open house", "2 " + allHousesList.get(position).toString());
                MyDbDataManager.getInstance().setHouse( allHousesList.get(position));
                Log.d("Schedule open house", "3 " + allHousesList.get(position).toString());
            }
        });

        return view;
    }

    public void initAdapter() {
        houseAdapter = new HouseAdapter(getContext(), allHousesList, showPropertiesType);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        allHouses_LST_houses.setLayoutManager(linearLayoutManager);
        allHouses_LST_houses.setAdapter(houseAdapter);

        houseAdapter.setHouseDetailsCallBack(house ->{
                ShowDetailDialogManager.getInstance().showHouseDetailsDialog(HousesListFragment.this.getContext(), house);
        });

        if(showPropertiesType.equals(ShowPropertiesType.AGENT_PROPERTIES)){
            houseAdapter.setScheduleCallBack((house, position) -> {
                Log.d("Schedule open house", "1 " + allHousesList.get(position).toString());
                scheduleOpenHouse.showDialog(house, position, this.getContext());


            });
        }



    }

    public void setHousesListUI(String currentPurchaseType, String currentHouseType) {
        Log.d("HousesListFragment", "setAllHousesUI: " + currentPurchaseType + " " + currentHouseType);
        if(showPropertiesType.equals(ShowPropertiesType.ALL_HOUSES)){
            setAllHousesUI(currentPurchaseType, currentHouseType);
        } else if(showPropertiesType.equals(ShowPropertiesType.AGENT_PROPERTIES)) {
            setAgentPropertiesUI(currentPurchaseType, currentHouseType);
        }
    }

    public void setAllHousesUI(String currentPurchaseType, String currentHouseType) {
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

    public void setAgentPropertiesUI(String currentPurchaseType, String currentHouseType) {
        MyDbDataManager.getInstance().loadAgentProperties(currentPurchaseType, currentHouseType, new HousesListCallBack() {

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

    private void showHouseDetailsDialog(House house) {
        ShowDetailDialogManager.getInstance().showHouseDetailsDialog(this.getContext(), house);
    }
}
