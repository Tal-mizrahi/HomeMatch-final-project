package com.example.homematch.Utilities;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.homematch.Models.Apartment;
import com.example.homematch.Models.Agent;
import com.example.homematch.Models.Client;
import com.example.homematch.Models.House;
import com.example.homematch.Models.PrivateHouse;
import com.example.homematch.Models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MyDbDataManager {
    private static Context context;
    private static final String HOUSES = "Houses";
    private static volatile MyDbDataManager instance;
    private FirebaseDatabase mDatabase;

    private MyDbDataManager(Context context) {
        this.context = context;
        this.mDatabase = FirebaseDatabase.getInstance();

    }


    public static void init(Context context) {
        if (instance == null) {
            instance = new MyDbDataManager(context);
        }
    }

    public static MyDbDataManager getInstance() {
        return instance;
    }



    public void setUser(User user, String type) {
        mDatabase.getReference().child("Users").child(type).child(user.getUid()).setValue(user);
    }

    public void setHouse(House house) {
        mDatabase.getReference(HOUSES).child(house.getPurchaseType()).child(house.getHouseType()).child(house.getUuid()).setValue(house);
    }


    public void checkUserType(String uid, UserTypeCallBack userTypeCallBack) {
        mDatabase.getReference().child("Users").child("Client").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Client client = snapshot.getValue(Client.class);
                if (client != null) {
                    userTypeCallBack.onClientType();
                } else {
                    userTypeCallBack.onAgentType();
                }
            }
            @Override
            public void onCancelled (@NonNull DatabaseError error){

            }

        });
    }

    public void getUser(String userType, String uid, UserCallBack userCallBack) {
        mDatabase.getReference().child("Users").child(userType).child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user;
                if(userType.equals("Client")){
                    user = snapshot.getValue(Client.class);
                } else {
                    user = snapshot.getValue(Agent.class);

                }
                if (user != null) {
                    Log.d("Agent DB", user.toString());
                    userCallBack.onSuccess(user);
                } else {
                    userCallBack.onFailure();
                }
            }
            @Override
            public void onCancelled (@NonNull DatabaseError error){

            }

        });
    }
    public void getHouseList(String purchaseType, String houseType, HousesListCallBack housesListCallBack) {
        mDatabase.getReference(HOUSES).child(purchaseType).child(houseType).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<House> houses = new ArrayList<>();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                String currentDate = dateFormat.format(new Date());
                String currentTime = timeFormat.format(new Date());

                for (DataSnapshot houseSnapshot : snapshot.getChildren()) {
                    House house;
                    if (houseType.equals("Private House")) {
                        house = houseSnapshot.getValue(PrivateHouse.class);
                    } else {
                        house = houseSnapshot.getValue(Apartment.class);
                    }

                    if (house != null) {
                        //Log.d("compare", "Date/Time passed for house: " + house.getOpenHouseDate() + " " + house.getOpenHouseTime() + " " + house.getOpenHouseDate().equals(currentDate));
                       // Log.d("compare2", "Date/Time passed for house: " +currentDate + " " + currentTime + " " + house.getOpenHouseTime().compareTo(currentTime));

                        if (house.getOpenHouseDate() != null) {
                            if (house.getOpenHouseDate().compareTo(currentDate) < 0 ||
                                    (house.getOpenHouseDate().equals(currentDate) && house.getOpenHouseTime() != null && house.getOpenHouseTime().compareTo(currentTime) < 0)) {

                                Log.d("compare", "Date/Time passed for house: " + house.getOpenHouseDate() + " " + house.getOpenHouseTime());

                                // Reset the open house attributes if the date/time has passed
                                house.resetOpenHouseData();

                                // Update the changes in the database
                                houseSnapshot.getRef().setValue(house);
                            }
                        }
                        houses.add(house);
                    }
                }

                housesListCallBack.onSuccess(houses);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                housesListCallBack.onFailure(error.toException());
            }
        });
    }


    public void getWeeklyOpenHousesList(String purchaseType, String houseType, HousesListCallBack housesListCallBack) {
        mDatabase.getReference(HOUSES).child(purchaseType).child(houseType).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<House> houses = new ArrayList<>();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                String currentDate = dateFormat.format(new Date());
                String currentTime = timeFormat.format(new Date());
                for (DataSnapshot houseSnapshot : snapshot.getChildren()) {
                    House house;
                    if(houseType.equals("Private House")){
                        house = houseSnapshot.getValue(PrivateHouse.class);
                    }else {
                        house = houseSnapshot.getValue(Apartment.class);
                    }

                    if (house != null) {
                        //Log.d("compare", "Date/Time passed for house: " + house.getOpenHouseDate() + " " + house.getOpenHouseTime() + " " + house.getOpenHouseDate().equals(currentDate));
                        // Log.d("compare2", "Date/Time passed for house: " +currentDate + " " + currentTime + " " + house.getOpenHouseTime().compareTo(currentTime));

                        if (house.getOpenHouseDate() != null) {
                            if (house.getOpenHouseDate().compareTo(currentDate) < 0 ||
                                    (house.getOpenHouseDate().equals(currentDate) && house.getOpenHouseTime() != null && house.getOpenHouseTime().compareTo(currentTime) < 0)) {

                                Log.d("compare", "Date/Time passed for house: " + house.getOpenHouseDate() + " " + house.getOpenHouseTime());

                                // Reset the open house attributes if the date/time has passed
                                house.resetOpenHouseData();

                                // Update the changes in the database
                                houseSnapshot.getRef().setValue(house);
                            }
                        }
                        houses.add(house);
                    }
                }

                housesListCallBack.onSuccess(houses);
            }
            @Override
            public void onCancelled (@NonNull DatabaseError error){
                housesListCallBack.onFailure(error.toException());
            }

        });
    }
    public void loadClientOpenHouses(String purchaseType, String houseType, HousesListCallBack housesListCallBack) {
        String clientUid = MyDbUserManager.getInstance().getUidOfCurrentUser();
        Log.d("load", clientUid);
        DatabaseReference databaseRef = mDatabase.getReference(HOUSES).child(purchaseType).child(houseType);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());
        String currentTime = timeFormat.format(new Date());

        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<House> houses = new ArrayList<>();
                for (DataSnapshot houseSnapshot : snapshot.getChildren()) {
                    House house;
                    if (houseType.equals("Private House")) {
                        house = houseSnapshot.getValue(PrivateHouse.class);
                    } else {
                        house = houseSnapshot.getValue(Apartment.class);
                    }

                    if (house != null) {
                        if (house.getOpenHouseDate() != null) {
                            if (house.getOpenHouseDate().compareTo(currentDate) < 0 ||
                                    (house.getOpenHouseDate().equals(currentDate) && house.getOpenHouseTime() != null && house.getOpenHouseTime().compareTo(currentTime) < 0)) {

                                Log.d("compare", "Date/Time passed for house: " + house.getOpenHouseDate() + " " + house.getOpenHouseTime());

                                // Reset the open house attributes if the date/time has passed
                                house.resetOpenHouseData();

                                // Update the changes in the database
                                houseSnapshot.getRef().setValue(house);
                            }
                        }

                        // Add houses to the list if they contain the client's UID in the open house sign-ups
                        if (house.getOpenHouseSignUps() != null && house.getOpenHouseSignUps().containsKey(clientUid)) {
                            houses.add(house);
                        }
                    }
                }

                // Sort the list by open house date and time
                houses.sort((house1, house2) -> {
                    int dateComparison = house1.getOpenHouseDate().compareTo(house2.getOpenHouseDate());
                    if (dateComparison != 0) {
                        return dateComparison;
                    }
                    return house1.getOpenHouseTime().compareTo(house2.getOpenHouseTime());
                });

                housesListCallBack.onSuccess(houses);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                housesListCallBack.onFailure(error.toException());
            }
        });
    }



    public void loadAgentProperties( String purchaseType, String houseType,HousesListCallBack housesListCallBack) {


        String agentUid = MyDbUserManager.getInstance().getUidOfCurrentUser();
        Log.d("load", agentUid);
        DatabaseReference databaseRef = mDatabase.getReference("Houses").child(purchaseType).child(houseType);
        Query eventsQuery = databaseRef.orderByChild("agentId").equalTo(agentUid);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());
        String currentTime = timeFormat.format(new Date());
        eventsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<House> houses = new ArrayList<>();
                for (DataSnapshot houseSnapshot : snapshot.getChildren()) {
                    House house;
                    if(houseType.equals("Private House")){
                        house = houseSnapshot.getValue(PrivateHouse.class);
                    }else {
                        house = houseSnapshot.getValue(Apartment.class);
                    }

                    if (house != null) {
                        //Log.d("compare", "Date/Time passed for house: " + house.getOpenHouseDate() + " " + house.getOpenHouseTime() + " " + house.getOpenHouseDate().equals(currentDate));
                        // Log.d("compare2", "Date/Time passed for house: " +currentDate + " " + currentTime + " " + house.getOpenHouseTime().compareTo(currentTime));

                        if (house.getOpenHouseDate() != null) {
                            if (house.getOpenHouseDate().compareTo(currentDate) < 0 ||
                                    (house.getOpenHouseDate().equals(currentDate) && house.getOpenHouseTime() != null && house.getOpenHouseTime().compareTo(currentTime) < 0)) {

                                Log.d("compare", "Date/Time passed for house: " + house.getOpenHouseDate() + " " + house.getOpenHouseTime());

                                // Reset the open house attributes if the date/time has passed
                                house.resetOpenHouseData();

                                // Update the changes in the database
                                houseSnapshot.getRef().setValue(house);
                            }
                        }
                        houses.add(house);
                    }
                }

                housesListCallBack.onSuccess(houses);
            }
            @Override
            public void onCancelled (@NonNull DatabaseError error){
                housesListCallBack.onFailure(error.toException());
            }

        });
    }

    public void housePurchase(String purchaseType, String houseType,String houseId) {

        Log.d("Delete", "delete house: " + houseId);
        mDatabase.getReference(HOUSES).child(purchaseType).child(houseType).child(houseId).removeValue();
    }

    public interface UserTypeCallBack {

        void onAgentType();
        void onClientType();
    }

    public interface HousesListCallBack {
        void onSuccess(ArrayList<House> allHouses);
        void onFailure(Exception exception);
    }

    public interface LoadImgCallBack {

        void OnLoadImg(String imageUrl);
    }

    public interface UserCallBack {
        void onSuccess(User currentUser);
        void onFailure();
    }

}
