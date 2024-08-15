package com.example.homematch.Activities;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.homematch.Models.ShowPropertiesType;
import com.example.homematch.R;
import com.example.homematch.Fragments.AddingPropertyFragment;
import com.example.homematch.Fragments.AllHousesPageFragment;
import com.example.homematch.Fragments.AgentFragment;
import com.example.homematch.Interfaces.UserCallBack;
import com.example.homematch.Models.Agent;
import com.example.homematch.Models.User;
import com.example.homematch.Utilities.FullScreenManager;
import com.example.homematch.Utilities.MyDbDataManager;
import com.example.homematch.Utilities.MyDbUserManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AgentMainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private static final String AGENT = "Agent";
    private Agent agentUser;
    private boolean isOnCreate = true;
    private AgentFragment agentFragment;
    private AddingPropertyFragment addingPropertyFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_agent_main);
        this.bottomNavigationView = findViewById(R.id.homeMatch_BNV_agent);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

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

       // agentFragment = new AgentFragment();
        //allHousesPageFragment = new AllHousesPageFragment();
        getCurrentUser();
        addingPropertyFragment = new AddingPropertyFragment();
        //setListeners();
    }



    @Override
    protected void onResume() {
        super.onResume();
        //FullScreenManager.getInstance().fullScreen(getWindow());

    }

    public void setListeners() {
        agentFragment = new AgentFragment(agentUser);
        setAgentFragmentListeners();
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.homeMatch_ITM_profile) {
                replaceFragment(agentFragment);
            }
            if (item.getItemId() == R.id.homeMatch_ITM_all_houses) {
                replaceFragment(new AllHousesPageFragment(ShowPropertiesType.ALL_HOUSES));
            }
            if (item.getItemId() == R.id.homeMatch_ITM_add_house) {
                replaceFragment(new AddingPropertyFragment());
            }
            return true;
        });

        addingPropertyFragment.setHouseAddedCallBack(() -> {
            replaceFragment(new AllHousesPageFragment(ShowPropertiesType.ALL_HOUSES));
        });


    }

    private void replaceFragment(Fragment fragment) {
       getSupportFragmentManager().beginTransaction()
                .replace(R.id.homeMatch_LAY_agent, fragment)
                .commit();

    }

    public void setAgentFragmentListeners(){
        agentFragment.setLogoutCallBack(() -> {
            Log.d("Logout", "Logout");
            startActivity(new Intent(AgentMainActivity.this, LoginActivity.class));
        });

        agentFragment.setManagePropertiesCallBack(() -> {
            Log.d("ManageProperties", "show manage properties ");
            AgentMainActivity.this.replaceFragment(new AllHousesPageFragment(ShowPropertiesType.AGENT_PROPERTIES));
        });
    }

    public void getCurrentUser(){
        String uid = MyDbUserManager.getInstance().getUidOfCurrentUser();
        if(uid == null){
            Toast.makeText(this, "There is no user connected", Toast.LENGTH_SHORT).show();

        } else {
            MyDbDataManager.getInstance().getUser(AGENT, uid, new UserCallBack() {

                @Override
                public void onSuccess(User currentUser) {

                    agentUser = (Agent) currentUser;
                    if(isOnCreate){
                        isOnCreate = false;
                        //agentFragment.setAgentUser(agentUser);
                        agentFragment = new AgentFragment(agentUser);
                        replaceFragment(agentFragment);
                        setAgentFragmentListeners();
                    }
                    setListeners();
                    Log.d(AGENT, agentUser.toString());

                }

                @Override
                public void onFailure() {
                    Toast.makeText(AgentMainActivity.this, "Agent not found", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }


}
