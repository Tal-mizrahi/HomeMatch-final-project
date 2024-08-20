package com.example.homematch.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.homematch.Fragments.AgentFragment;
import com.example.homematch.Fragments.AllHousesPageFragment;
import com.example.homematch.Fragments.ClientFragment;
import com.example.homematch.Models.Agent;
import com.example.homematch.Models.Client;
import com.example.homematch.Models.ShowPropertiesType;
import com.example.homematch.Models.User;
import com.example.homematch.R;
import com.example.homematch.Utilities.FullScreenManager;
import com.example.homematch.Utilities.MyDbDataManager;
import com.example.homematch.Utilities.MyDbUserManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ClientActivity extends AppCompatActivity {

    private static final String CLIENT = "Client";
    private BottomNavigationView bottomNavigationView;
    private Client clientUser;
    private boolean isOnCreate = true;
    private ClientFragment clientFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_client);
        this.bottomNavigationView = findViewById(R.id.homeMatch_BNV_client);
        View decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    // Navigation bar is visible
                    FullScreenManager.getInstance().fullScreen(getWindow());
                }
            }
        });

        getCurrentUser();
    }

    private void setClientFragmentListeners() {
        clientFragment.setLogoutCallBack(() -> {
            Log.d("Logout", "Logout");
            startActivity(new Intent(ClientActivity.this, LoginActivity.class));
        });
    }

    private void setListeners() {
        clientFragment = new ClientFragment(clientUser);
        Log.d("client", "ClientMainActivity: " + clientUser.toString());
        setClientFragmentListeners();
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.client_ITM_profile) {
                replaceFragment(clientFragment);
            }
            if (item.getItemId() == R.id.client_ITM_all_houses) {
                replaceFragment(new AllHousesPageFragment(ShowPropertiesType.ALL_HOUSES_CLIENT));
            }
            if (item.getItemId() == R.id.client_ITM_open_houses) {
                replaceFragment(new AllHousesPageFragment(ShowPropertiesType.OPEN_HOUSES_CLIENT));
            }
            return true;
        });

    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.homeMatch_LAY_client, fragment)
                .commit();

    }

    public void getCurrentUser(){
        String uid = MyDbUserManager.getInstance().getUidOfCurrentUser();
        if(uid == null){
            Toast.makeText(this, "There is no user connected", Toast.LENGTH_SHORT).show();

        } else {
            MyDbDataManager.getInstance().getUser(CLIENT, uid, new MyDbDataManager.UserCallBack() {

                @Override
                public void onSuccess(User currentUser) {

                    clientUser = (Client) currentUser;
                    if(isOnCreate){
                        isOnCreate = false;
                        //agentFragment.setAgentUser(agentUser);
                        clientFragment = new ClientFragment(clientUser);
                        setClientFragmentListeners();
                        replaceFragment(clientFragment);
                    }
                    Log.d("Agent", "updated");
                    setListeners();
                    Log.d(CLIENT, clientUser.toString());

                }

                @Override
                public void onFailure() {
                    Toast.makeText(ClientActivity.this, "Client not found", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }




}