package com.example.homematch.Activities;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.homematch.Models.ShowPropertiesType;
import com.example.homematch.R;
import com.example.homematch.Fragments.AddingPropertyFragment;
import com.example.homematch.Fragments.AllHousesPageFragment;
import com.example.homematch.Fragments.AgentFragment;
import com.example.homematch.Models.Agent;
import com.example.homematch.Models.User;
import com.example.homematch.Utilities.FullScreenManager;
import com.example.homematch.Utilities.MyDbDataManager;
import com.example.homematch.Utilities.MyDbUserManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AgentActivity extends AppCompatActivity {

    private static final String AGENT = "Agent";
    private BottomNavigationView bottomNavigationView;
    //private Agent agentUser;
    //private boolean isOnCreate = true;
    private AgentFragment agentFragment;
    private AddingPropertyFragment addingPropertyFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_agent);
        this.bottomNavigationView = findViewById(R.id.homeMatch_BNV_agent);
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        View decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    // Navigation bar is visible
                    FullScreenManager.getInstance().fullScreen(getWindow());
                    AgentActivity.this.bottomNavigationView.setVisibility(View.GONE);
                } else {
                    // Navigation bar is hidden
                    AgentActivity.this.bottomNavigationView.setVisibility(View.VISIBLE);
                }
            }
        });
        addingPropertyFragment = new AddingPropertyFragment();
        agentFragment = new AgentFragment();
        setListeners();
        replaceFragment(agentFragment);
        //getCurrentUser();
    }



    @Override
    protected void onResume() {
        super.onResume();

    }

    public void setListeners() {
        //Log.d("agent", "AgentMainActivity: " + agentUser.toString());
        //addingPropertyFragment.setAgentUser(agentUser);

        setAgentFragmentListeners();
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.homeMatch_ITM_profile) {
                agentFragment = new AgentFragment();
                setAgentFragmentListeners();
                replaceFragment(agentFragment);
            }
            if (item.getItemId() == R.id.homeMatch_ITM_all_houses) {
                replaceFragment(new AllHousesPageFragment(ShowPropertiesType.ALL_HOUSES_AGENT));
            }
            //addingPropertyFragment = new AddingPropertyFragment();
            if (item.getItemId() == R.id.homeMatch_ITM_add_house) {
                //addingPropertyFragment.resetUI();
                replaceFragment(addingPropertyFragment);
            }
            return true;
        });

        addingPropertyFragment.setHouseAddedCallBack(() -> {

            replaceFragment(new AllHousesPageFragment(ShowPropertiesType.ALL_HOUSES_AGENT));
            bottomNavigationView.setSelectedItemId(R.id.homeMatch_ITM_all_houses);
        });
        setAgentFragmentListeners();

    }

    private void replaceFragment(Fragment fragment) {
       getSupportFragmentManager().beginTransaction()
                .replace(R.id.homeMatch_LAY_agent, fragment)
                .commit();

    }

    public void setAgentFragmentListeners(){
        agentFragment.setLogoutCallBack(() -> {
            Log.d("Logout", "Logout");
            startActivity(new Intent(AgentActivity.this, LoginActivity.class));
        });

        agentFragment.setManagePropertiesCallBack(() -> {
            Log.d("ManageProperties", "show manage properties ");
            AgentActivity.this.replaceFragment(new AllHousesPageFragment(ShowPropertiesType.AGENT_PROPERTIES));
        });
    }

//    public void getCurrentUser(){
//        String uid = MyDbUserManager.getInstance().getUidOfCurrentUser();
//        if(uid == null){
//            Toast.makeText(this, "There is no user connected", Toast.LENGTH_SHORT).show();
//
//        } else {
//            MyDbDataManager.getInstance().getUser(AGENT, uid, new MyDbDataManager.UserCallBack() {
//
//                @Override
//                public void onSuccess(User currentUser) {
//
//                    agentUser = (Agent) currentUser;
//                    if(isOnCreate){
//                        isOnCreate = false;
//                        //agentFragment.setAgentUser(agentUser);
//                        agentFragment = new AgentFragment(agentUser);
//                        setAgentFragmentListeners();
//                        replaceFragment(agentFragment);
//                    }
//                    Log.d("Agent", "updated");
//                    setListeners();
//                    Log.d(AGENT, agentUser.toString());
//
//                }
//
//                @Override
//                public void onFailure() {
//                    Toast.makeText(AgentActivity.this, "Agent not found", Toast.LENGTH_SHORT).show();
//                }
//            });
//
//        }
//    }


}
