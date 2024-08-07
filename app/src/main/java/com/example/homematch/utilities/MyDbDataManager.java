package com.example.homematch.utilities;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.homematch.interaces.ImgCallBack;
import com.example.homematch.interaces.LoginCallBack;
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
        mDatabase.getReference().child("Users").child(userType).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user;
                if(userType.equals("Client")){
                    user = snapshot.getValue(Client.class);
                } else {
                    user = snapshot.getValue(Broker.class);
                }
                if (user != null) {
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

    public void getUserImage(String userId,String userType, ImgCallBack imgCallBack) {
        DatabaseReference usersRef = mDatabase.getReference("Users");

        usersRef.child(userId).child(userType).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                if (user != null) {
                    imgCallBack.onSuccess(user.getImage());
                } else {
                    imgCallBack.onFailure(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void updateUserImage(String imageUrl) {
        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("users");

        usersRef.child(userUid).child("image").setValue(imageUrl);
//                .addOnSuccessListener(unused -> {
//                    callBack.res(null);
//                });
    }

}
