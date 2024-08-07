package com.example.homematch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.homematch.R;
import com.example.homematch.interaces.LoginCallBack;
import com.example.homematch.interaces.UserTypeCallBack;
import com.example.homematch.utilities.FullScreenManager;
import com.example.homematch.utilities.MyDbDataManager;
import com.example.homematch.utilities.MyDbUserManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText login_INP_email;
    private TextInputEditText login_INP_password;
    private TextInputLayout login_LAY_email;
    private TextInputLayout login_LAY_password;
    private MaterialButton login_BTN_login;
    private MaterialTextView login_LBL_create_account;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        findViews();

        login_LBL_create_account.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
        });

        login_BTN_login.setOnClickListener(v -> {
            login();
        });

    }

    public void login() {

        if (!TextUtils.isEmpty(login_INP_email.getText()) && !TextUtils.isEmpty(login_INP_password.getText())) {
            MyDbUserManager.getInstance().loginUser(login_INP_email.getText().toString(), login_INP_password.getText().toString(), this, new LoginCallBack() {
                @Override
                public void onLoginSuccess(String uid) {
                    MyDbDataManager.getInstance().checkUserType(uid, new UserTypeCallBack() {

                        @Override
                        public void onBrokerType() {
                            startActivity(new Intent(LoginActivity.this, HomePageBrokerActivity.class));
                        }

                        @Override
                        public void onClientType() {
                            startActivity(new Intent(LoginActivity.this, ClientActivity.class));
                        }
                    });
                }

                @Override
                public void onLoginFailure(Exception exception) {
                    Toast.makeText(LoginActivity.this, "User does not exist!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(LoginActivity.this, "Enter Email and Password", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        FullScreenManager.getInstance().fullScreen(getWindow());
    }

    public void findViews(){
        login_INP_email = findViewById(R.id.login_INP_email);
        login_INP_password = findViewById(R.id.login_INP_password);

        login_LAY_email = findViewById(R.id.login_LAY_email);
        login_LAY_password = findViewById(R.id.login_LAY_password);

        login_BTN_login = findViewById(R.id.login_BTN_login);
        login_LBL_create_account = findViewById(R.id.login_LBL_create_account);
    }


}