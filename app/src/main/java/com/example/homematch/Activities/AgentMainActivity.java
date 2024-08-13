package com.example.homematch.Activities;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

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
    private AllHousesPageFragment allHousesPageFragment;
    private AddingPropertyFragment addingPropertyFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_agent_main);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.homeMatch_BNV_agent), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, 0, 0, 0);
            return insets;
        });
        this.bottomNavigationView = findViewById(R.id.homeMatch_BNV_agent);
        //agentFragment = new AgentFragment();
        //allHousesPageFragment = new AllHousesPageFragment();
        getCurrentUser();
        addingPropertyFragment = new AddingPropertyFragment();
        //setListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        FullScreenManager.getInstance().fullScreen(getWindow());

    }

    public void setListeners(){
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.homeMatch_ITM_profile){
                replaceFragment(new AgentFragment(agentUser));
            }
            if (item.getItemId() == R.id.homeMatch_ITM_all_houses) {
                replaceFragment(new AllHousesPageFragment());
            }
            if (item.getItemId() == R.id.homeMatch_ITM_add_house) {
                replaceFragment(new AddingPropertyFragment());
            }
            return true;
        });

        addingPropertyFragment.setHouseAddedCallBack(() -> {
            replaceFragment(allHousesPageFragment);
        });
    }

    private void replaceFragment(Fragment fragment) {
       getSupportFragmentManager().beginTransaction()
                .replace(R.id.homeMatch_LAY_agent, fragment)
                .commit();

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
                        replaceFragment(new AgentFragment(agentUser));
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
