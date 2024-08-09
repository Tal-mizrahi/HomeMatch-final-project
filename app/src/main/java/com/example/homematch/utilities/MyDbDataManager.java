package com.example.homematch.utilities;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.homematch.interaces.ImgCallBack;
import com.example.homematch.interaces.LoadImgCallBack;
import com.example.homematch.interaces.UserCallBack;
import com.example.homematch.interaces.UserTypeCallBack;
import com.example.homematch.models.Broker;
import com.example.homematch.models.Client;
import com.example.homematch.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    public void checkUserType(String uid, UserTypeCallBack userTypeCallBack) {
        mDatabase.getReference().child("Users").child("Client").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Client client = snapshot.getValue(Client.class);
                if (client != null) {
                    userTypeCallBack.onClientType();
                } else {
                    userTypeCallBack.onBrokerType();
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
                    user = snapshot.getValue(Broker.class);

                }
                if (user != null) {
                    Log.d("Broker DB", user.toString());
                    userCallBack.onSuccess(user);
                } else {
                    userCallBack.onFailure();
                }
//                Broker broker = snapshot.getValue(Broker.class);
//
//
//                if (broker != null) {
//                    Log.d("Broker DB", broker.toString());
//                    userCallBack.onSuccess(broker);
//                } else {
//                    userCallBack.onFailure();
//                }
            }
            @Override
            public void onCancelled (@NonNull DatabaseError error){

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
