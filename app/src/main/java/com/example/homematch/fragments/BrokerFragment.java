package com.example.homematch.fragments;

import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.homematch.R;
import com.example.homematch.interaces.UserCallBack;
import com.example.homematch.models.Broker;
import com.example.homematch.models.User;
import com.example.homematch.interaces.ImgCallBack;
import com.example.homematch.utilities.MyDbDataManager;
import com.example.homematch.utilities.MyDbStorageManager;
import com.example.homematch.utilities.MyDbUserManager;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;


public class BrokerFragment extends Fragment {

   MaterialTextView broker_MTV_UserName;
   MaterialTextView broker_MTV_edit_profile;
   ShapeableImageView broker_IMG_user;
   Broker brokerUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_broker, container, false);
        findViews(view);
        initUser();
        initBrokerUI();
        broker_MTV_edit_profile.setOnClickListener( v -> editImage());

        return view;
    }

    public void updateUI() {
        MyDbDataManager.getInstance().getUserImage(brokerUser.getUid(), "Broker", new ImgCallBack() {
            @Override
            public void onSuccess(String imageUrl) {
                Glide.with(BrokerFragment.this)
                        .load(imageUrl)
                        .placeholder(R.drawable.add_profile_img)
                        .centerCrop()
                        .into(broker_IMG_user);
            }

            @Override
            public void onFailure(Exception exception) {

            }


        });
    }



    ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null) {
                    MyDbStorageManager.getInstance().uploadImage(uri, brokerUser.getUid(), new ImgCallBack() {

                        @Override
                        public void onSuccess(String imageUrl) {
                            MyDbDataManager.getInstance().updateUserImage(imageUrl);
                            updateUI();
                        }

                        @Override
                        public void onFailure(Exception exception) {
                            Toast.makeText(BrokerFragment.this.getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
                    Log.d("PhotoPicker", "Selected URI: " + uri);
                } else {
                    Log.d("PhotoPicker", "No media selected");
                }
            });

    public void editImage() {
        pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }



    public void initUser(){
        String uid = MyDbUserManager.getInstance().getUidOfCurrentUser();
        if(uid == null){
            Toast.makeText(this.getContext(), "There is no user connected", Toast.LENGTH_SHORT).show();
        } else {
            MyDbDataManager.getInstance().getUser("Broker", uid, new UserCallBack() {

                @Override
                public void onSuccess(User currentUser) {
                    brokerUser = (Broker) currentUser;
                }

                @Override
                public void onFailure() {
                    Toast.makeText(BrokerFragment.this.getContext(), "Broker not found", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }



    public void initBrokerUI(){
        String fullName = brokerUser.getFirstName() + " " + brokerUser.getLastName();
        broker_MTV_UserName.setText(fullName);

    }

    public void findViews(View view){
        broker_MTV_UserName = view.findViewById(R.id.broker_MTV_UserName);
        broker_MTV_edit_profile = view.findViewById(R.id.broker_MTV_edit_profile);
        broker_IMG_user = view.findViewById(R.id.broker_IMG_user);
    }

    public void resetUI(){
        initBrokerUI();


    }
}