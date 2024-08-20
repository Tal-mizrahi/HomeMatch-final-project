package com.example.homematch.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.homematch.Adapters.WeeklyOpenHousesAdapter;
import com.example.homematch.Interfaces.LogoutCallBack;
import com.example.homematch.Models.Client;
import com.example.homematch.Models.House;
import com.example.homematch.R;
import com.example.homematch.Utilities.MyDbDataManager;
import com.example.homematch.Utilities.MyDbStorageManager;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ClientFragment extends Fragment {

    private static final String CLIENT = "Client";
    private MaterialTextView client_MTV_UserName;
    private ExtendedFloatingActionButton client_FAB_edit_profile;
    private CircleImageView client_IMG_user;
    private LinearLayoutCompat client_LAY_logout;
    private ActivityResultLauncher<Intent> pickMedia;
    private RecyclerView client_LST_open_houses;
    private LogoutCallBack logoutCallBack;
    private Client client;
    private WeeklyOpenHousesAdapter weeklyOpenHousesAdapter;
    private ArrayList<House> weeklyOpenHouses = new ArrayList<>();;
    public ClientFragment() {
        // Required empty public constructor
    }

    public ClientFragment(Client client) {
        this.client = client;
        Log.d("ClientFragment", "client constructor: " + client.toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client, container, false);
        findViews(view);
        initClientUI();
        client_FAB_edit_profile.setOnClickListener(v -> editImage());
        setPickMedia();
        Log.d("ClientFragment", "replaced");
        client_LAY_logout.setOnClickListener(v -> logout());
        initAdapter();
        return view;
    }

    private void initAdapter() {
        weeklyOpenHousesAdapter = new WeeklyOpenHousesAdapter(weeklyOpenHouses, getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        client_LST_open_houses.setLayoutManager(linearLayoutManager);
        client_LST_open_houses.setAdapter(weeklyOpenHousesAdapter);
    }

    public void setLogoutCallBack(LogoutCallBack logoutCallBack) {
        this.logoutCallBack = logoutCallBack;
    }

    private void logout() {
        if(logoutCallBack != null){
            logoutCallBack.onLogout();
        }
    }

    public void setUserImgUI() {
        String imageUrl = client.getImageUrl();
        if (imageUrl != null) {
            Glide.with(ClientFragment.this)
                    .load(imageUrl)
                    .placeholder(R.drawable.img_white)
                    .centerCrop()
                    .into(client_IMG_user);
        }
    }

    public void updateProfileImg(Uri uri) {
        Glide.with(ClientFragment.this)
                .load(uri)
                .placeholder(R.drawable.img_white)
                .centerCrop()
                .into(client_IMG_user);
    }


    public void setPickMedia() {
        this.pickMedia =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                    int resultCode = result.getResultCode();
                    Intent data = result.getData();

                    if (resultCode == Activity.RESULT_OK) {
                        Uri uri = data.getData();
                        if (uri != null) {
                            MyDbStorageManager.getInstance().uploadImage(uri, client.getUid(), new MyDbStorageManager.ImgCallBack() {

                                @Override
                                public void onSuccess(String imageUrl) {
                                    client.setImageUrl(imageUrl);
                                    MyDbDataManager.getInstance().setUser(client, CLIENT);
                                    updateProfileImg(uri);
                                }

                                @Override
                                public void onFailure(Exception exception) {
                                    Toast.makeText(ClientFragment.this.getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                            Log.d("PhotoPicker", "Selected URI: " + uri);
                        }
                    } else if (resultCode == ImagePicker.RESULT_ERROR) {
                        Toast.makeText(getContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Task Cancelled", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void editImage() {
        ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .createIntent(intent -> {
                    pickMedia.launch(intent);
                    return null;
                });
    }

    public void initClientUI() {
        String fullName = client.getFirstName() + "!";
        client_MTV_UserName.setText(fullName);
        setUserImgUI();

        MyDbDataManager.getInstance().getWeeklyOpenHousesList(client.getUid(), new MyDbDataManager.HousesListCallBack(){

            @Override
            public void onSuccess(ArrayList<House> allHouses) {
                weeklyOpenHouses.clear();
                weeklyOpenHouses.addAll(allHouses);
                weeklyOpenHousesAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Exception exception) {
                Toast.makeText(ClientFragment.this.getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }






    public void findViews(View view) {
        client_MTV_UserName = view.findViewById(R.id.client_MTV_UserName);
        client_FAB_edit_profile = view.findViewById(R.id.client_FAB_edit_profile);
        client_IMG_user = view.findViewById(R.id.client_IMG_user);
        client_LAY_logout = view.findViewById(R.id.client_LAY_logout);
        client_LST_open_houses = view.findViewById(R.id.client_LST_open_houses);

    }
}