package com.example.homematch.activities;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Window;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.homematch.R;
import com.example.homematch.fragments.AllHousesPageFragment;
import com.example.homematch.fragments.BrokerFragment;
import com.example.homematch.utilities.FullScreenManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomePageBrokerActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home_page_broker);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.homeMatch_BNV_broker), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, 0, 0, 0);
            return insets;
        });
        this.bottomNavigationView = findViewById(R.id.homeMatch_BNV_broker);
        setListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        FullScreenManager.getInstance().fullScreen(getWindow());

    }

    public void setListeners(){
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.homeMatch_ITM_profile){
                replaceFragment(new BrokerFragment());
            }
            if (item.getItemId() == R.id.homeMatch_ITM_add_house){

            }
            if (item.getItemId() == R.id.homeMatch_ITM_all_houses) {
                replaceFragment(new AllHousesPageFragment());
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
       getSupportFragmentManager().beginTransaction()
                .replace(R.id.homeMatch_LAY_broker_home, fragment)
                .commit();

    }

}
