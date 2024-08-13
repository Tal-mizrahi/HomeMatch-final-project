package com.example.homematch.Activities;

import android.os.Bundle;
import android.view.Window;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.homematch.R;
import com.example.homematch.Utilities.FullScreenManager;

public class ClientActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_client);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.homeMatch_BNV_agent), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, 0, 0, 0);
            return insets;
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        FullScreenManager.getInstance().fullScreen(getWindow());

    }
}