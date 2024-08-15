package com.example.homematch.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.homematch.Interfaces.LogoutCallBack;
import com.example.homematch.Interfaces.ManagePropertiesCallBack;
import com.example.homematch.R;
import com.example.homematch.Models.Agent;
import com.example.homematch.Interfaces.ImgCallBack;
import com.example.homematch.Utilities.MyDbDataManager;
import com.example.homematch.Utilities.MyDbStorageManager;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

public class AgentFragment extends Fragment {

    private static final String AGENT = "Agent";
    private MaterialTextView agent_MTV_UserName;
    private ExtendedFloatingActionButton agent_FAB_edit_profile;
    private ShapeableImageView agent_IMG_user;
    private MaterialButton agent_BTN_manage_properties;
    private LinearLayoutCompat agent_LAY_logout;
    private Agent agentUser;
    private ActivityResultLauncher<Intent> pickMedia;
    private LogoutCallBack logoutCallBack;
    private ManagePropertiesCallBack managePropertiesCallBack;
    public AgentFragment() {}

    public AgentFragment(Agent agentUser) {
        this.agentUser = agentUser;
        Log.d("AgentFragment", "AgentFragment: " + agentUser.toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_agent, container, false);
        findViews(view);
        initAgentUI();
        agent_FAB_edit_profile.setOnClickListener(v -> editImage());
        setPickMedia();
        agent_LAY_logout.setOnClickListener(v -> logout());
        agent_BTN_manage_properties.setOnClickListener(v -> moveToManageProperties());
        return view;
    }

    private void moveToManageProperties() {
        if(managePropertiesCallBack != null){
            managePropertiesCallBack.onManageProperties();
        }
    }

    public void setLogoutCallBack(LogoutCallBack logoutCallBack) {
        this.logoutCallBack = logoutCallBack;
    }
    public void setManagePropertiesCallBack(ManagePropertiesCallBack managePropertiesCallBack) {
        this.managePropertiesCallBack = managePropertiesCallBack;
    }

    private void logout() {
        if(logoutCallBack != null){
            logoutCallBack.onLogout();
        }
    }

    public void setUserImgUI() {
        String imageUrl = agentUser.getImageUrl();
        if (imageUrl != null) {
            Glide.with(AgentFragment.this)
                    .load(imageUrl)
                    .placeholder(R.drawable.img_white)
                    .centerCrop()
                    .into(agent_IMG_user);
        }
    }

    public void updateProfileImg(Uri uri) {
        Glide.with(AgentFragment.this)
                .load(uri)
                .placeholder(R.drawable.img_white)
                .centerCrop()
                .into(agent_IMG_user);
    }

    public void setPickMedia() {
        this.pickMedia =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                    int resultCode = result.getResultCode();
                    Intent data = result.getData();

                    if (resultCode == Activity.RESULT_OK) {
                        Uri uri = data.getData();
                        if (uri != null) {
                            MyDbStorageManager.getInstance().uploadImage(uri, agentUser.getUid(), new ImgCallBack() {

                                @Override
                                public void onSuccess(String imageUrl) {
                                    agentUser.setImageUrl(imageUrl);
                                    MyDbDataManager.getInstance().setUser(agentUser, AGENT);
                                    updateProfileImg(uri);
                                }

                                @Override
                                public void onFailure(Exception exception) {
                                    Toast.makeText(AgentFragment.this.getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
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

    public void initAgentUI() {
        String fullName = agentUser.getFirstName() + " " + agentUser.getLastName() + "!";
        agent_MTV_UserName.setText(fullName);
        setUserImgUI();
    }

    public void findViews(View view) {
        agent_MTV_UserName = view.findViewById(R.id.agent_MTV_UserName);
        agent_FAB_edit_profile = view.findViewById(R.id.agent_FAB_edit_profile);
        agent_IMG_user = view.findViewById(R.id.agent_IMG_user);
        agent_BTN_manage_properties = view.findViewById(R.id.agent_BTN_manage_properties);
        agent_LAY_logout = view.findViewById(R.id.agent_LAY_logout);
    }
}
