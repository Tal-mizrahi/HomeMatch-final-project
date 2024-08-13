package com.example.homematch.Utilities;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.homematch.Interfaces.HousesListCallBack;
import com.example.homematch.Interfaces.LoadImgCallBack;
import com.example.homematch.Interfaces.UserCallBack;
import com.example.homematch.Interfaces.UserTypeCallBack;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyDbDataManager {
    private static Context context;
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


    public void storeNewUser(User user, String type) {
        mDatabase.getReference().child("Users").child(type).child(user.getUid()).setValue(user);
    }

    public void storeNewHouse(House house) {
        mDatabase.getReference("Houses").child(house.getPurchaseType()).child(house.getHouseType()).child(house.getUuid()).setValue(house);
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
        mDatabase.getReference("Houses").child(purchaseType).child(houseType).addListenerForSingleValueEvent(new ValueEventListener() {
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

                    if(house != null)
                        houses.add(house);
                }

                housesListCallBack.onSuccess(houses);
            }
            @Override
            public void onCancelled (@NonNull DatabaseError error){
                housesListCallBack.onFailure(error.toException());
            }

        });
    }

    public void getUserImage(String userId,String userType, LoadImgCallBack loadImgCallBack) {
        DatabaseReference usersRef = mDatabase.getReference("Users");

        usersRef.child(userType).child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                if (user != null) {
                    loadImgCallBack.OnLoadImg(user.getImageUrl());
                } else {
                    loadImgCallBack.OnLoadImg(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void updateUserImage(String uid, String userType, String imageUrl) {
        //String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase.getReference("Users").child(userType).child(uid).child("imageUrl").setValue(imageUrl);

//                .addOnSuccessListener(unused -> {
//                    callBack.res(null);
//                });
    }

}
