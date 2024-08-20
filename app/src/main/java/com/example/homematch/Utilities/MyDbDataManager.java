package com.example.homematch.Utilities;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

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
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class MyDbDataManager {
    private static Context context;
    private static final String HOUSES = "Houses";
    private static final String USERS = "Users";
    private static final String AGENT = "Agent";
    private static final String CLIENT = "Client";
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
        mDatabase.getReference().child(USERS).child(type).child(user.getUid()).setValue(user);
    }

    public void setHouse(House house) {
        mDatabase.getReference(HOUSES).child(house.getPurchaseType()).child(house.getHouseType()).child(house.getUuid()).setValue(house);
    }

    public void housePurchase(String purchaseType, String houseType,String houseId, HousePurchasedCallBack housePurchasedCallBack) {

        Log.d("Delete", "delete house: " + houseId);
        mDatabase.getReference(HOUSES).child(purchaseType).child(houseType).child(houseId).removeValue().addOnSuccessListener(v ->{
            housePurchasedCallBack.OnPurchased();
        });
    }


    public void checkUserType(String uid, UserTypeCallBack userTypeCallBack) {
        mDatabase.getReference().child(USERS).child(CLIENT).child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
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
        mDatabase.getReference().child(USERS).child(userType).child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user;
                if(userType.equals(CLIENT)){
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

    public void setUserPropertiesAmount(String purchaseType, boolean isIncrease, Context context) {
        String uid = MyDbUserManager.getInstance().getUidOfCurrentUser();
        mDatabase.getReference().child(USERS).child(AGENT).child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Agent agent;
                agent = snapshot.getValue(Agent.class);
                if (agent != null) {
                    if(isIncrease)
                        increaseHouseAmount(purchaseType, agent);
                    else
                        decreaseHouseAmount(purchaseType, agent);
                   setUser(agent, AGENT);
                   Toast.makeText(context, "Action completed successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Action failed!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled (@NonNull DatabaseError error){

            }

        });
    }

    public void increaseHouseAmount(String purchaseType, Agent agent) {
        if (purchaseType.equals("Sale")) {
            agent.addSaleProperty();
        } else {
            agent.addRentProperty();

        }
    }

    public void decreaseHouseAmount(String purchaseType, Agent agent) {
        if (purchaseType.equals("Sale")) {
            agent.propertySold();
        } else {
            agent.propertyRented();
        }
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


    public void getWeeklyOpenHousesList(String clientUid, HousesListCallBack housesListCallBack) {
        DatabaseReference housesRef = mDatabase.getReference(HOUSES);
        housesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<House> houses = new ArrayList<>();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                Calendar calendar = Calendar.getInstance();

                // Get the current date
                String currentDate = dateFormat.format(calendar.getTime());
                String currentTime = timeFormat.format(calendar.getTime());

                // Get the start (Sunday) and end (Saturday) dates of the current week
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                String startDate = dateFormat.format(calendar.getTime());
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                String endDate = dateFormat.format(calendar.getTime());

                for (DataSnapshot purchaseTypeSnapshot : snapshot.getChildren()){
                    for (DataSnapshot houseTypeSnapshot : purchaseTypeSnapshot.getChildren()){
                        for (DataSnapshot houseSnapshot : houseTypeSnapshot.getChildren()) {
                            House house;
                            String houseType = houseSnapshot.child("houseType").getValue(String.class);
                            if (houseType != null) {
                                if (houseType.equals("Private House")) {
                                    house = houseSnapshot.getValue(PrivateHouse.class);
                                } else {
                                    house = houseSnapshot.getValue(Apartment.class);
                                }
                                if (house != null && house.getOpenHouseDate() != null && house.getOpenHouseSignUps() != null
                                        && house.getOpenHouseSignUps().containsKey(clientUid)) {
                                    String openHouseDate = house.getOpenHouseDate();
                                    if (openHouseDate.compareTo(startDate) >= 0 && openHouseDate.compareTo(endDate) <= 0) {
                                        // The house is within the current week's range
                                        if (openHouseDate.compareTo(currentDate) < 0 ||
                                                (openHouseDate.equals(currentDate) && house.getOpenHouseTime().compareTo(currentTime) < 0)) {
                                            // If the date/time has passed, reset the attributes
                                            house.resetOpenHouseData();
                                            houseSnapshot.getRef().setValue(house);
                                        } else { // The house is ok
                                            houses.add(house);
                                        }

                                    }
                                }
                            }

                        }
                    }
                }
                // Sort the list by open house date and time
                houses.sort(Comparator.comparing(House::getOpenHouseDate).thenComparing(House::getOpenHouseTime));
                housesListCallBack.onSuccess(houses);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
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
                houses.sort(Comparator.comparing(House::getOpenHouseDate).thenComparing(House::getOpenHouseTime));

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
        DatabaseReference databaseRef = mDatabase.getReference(HOUSES).child(purchaseType).child(houseType);
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
                        Log.d("MyDbDataManager","loadAgentProperties: " +  house.toString());
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

    public interface UserTypeCallBack {

        void onAgentType();
        void onClientType();
    }

    public interface HousesListCallBack {
        void onSuccess(ArrayList<House> allHouses);
        void onFailure(Exception exception);
    }

    public interface HousePurchasedCallBack {

        void OnPurchased();
    }

    public interface UserCallBack {
        void onSuccess(User currentUser);
        void onFailure();
    }

}
