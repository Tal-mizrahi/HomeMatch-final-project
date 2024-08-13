package com.example.homematch.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.homematch.R;
import com.example.homematch.Interfaces.CreationCallBack;
import com.example.homematch.Models.Agent;
import com.example.homematch.Models.Client;
import com.example.homematch.Models.User;
import com.example.homematch.Utilities.FullScreenManager;
import com.example.homematch.Utilities.MyDbDataManager;
import com.example.homematch.Utilities.MyDbUserManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText register_INP_first_name;
    private TextInputEditText register_INP_last_name;
    private TextInputEditText register_INP_agency_name;
    private TextInputEditText register_INP_email;
    private TextInputEditText register_INP_phone;
    private TextInputEditText register_INP_password;

    private TextInputLayout register_LAY_first_name;
    private TextInputLayout register_LAY_last_name;
    private TextInputLayout register_LAY_agency_name;
    private TextInputLayout register_LAY_email;
    private TextInputLayout register_LAY_phone;
    private TextInputLayout register_LAY_password;

    private RelativeLayout register_REL_main;

    private CardView register_CARD_loading;

    private MaterialButton btnRegister;
    private MaterialTextView haveAccount;
    private MaterialButtonToggleGroup toggleGroup;

    private boolean isAgent = true;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);
        findViews();
        // Set default checked button
        toggleGroup.check(R.id.register_BTN_agent);
        setListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        FullScreenManager.getInstance().fullScreen(getWindow());
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void setListeners() {
        toggleGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (!isChecked)
                return;
            if (checkedId == R.id.register_BTN_agent) {
                isAgent = true;
                showAgentFields();
            } else if (checkedId == R.id.register_BTN_client) {
                isAgent = false;
                showClientFields();
            }
        });



        btnRegister.setOnClickListener(v -> {
            if (validateFields()) {
                FullScreenManager.getInstance().hideKeyboard(this);
                register_REL_main.setAlpha(0.5f);
                register_REL_main.setEnabled(false);
                register_CARD_loading.setVisibility(View.VISIBLE);

                // Set the screen to not touch
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                String firstName = fixName(Objects.requireNonNull(register_INP_first_name.getText()).toString());
                String lastName = fixName(Objects.requireNonNull(register_INP_last_name.getText()).toString());
                String email = Objects.requireNonNull(register_INP_email.getText()).toString();
                String phoneNum = Objects.requireNonNull(register_INP_phone.getText()).toString();
                String password = Objects.requireNonNull(register_INP_password.getText()).toString();
                Log.d("Register", "Register"+email+password);
                MyDbUserManager.getInstance().createNewUser(email, password, this, new CreationCallBack() {
                    @Override
                    public void onCreated(String uid) {
                        String userType;
                        User theUser;
                        String imageUrl = null; // the user will init the image url in the homepage
                        if(isAgent){
                            String agencyName = Objects.requireNonNull(register_INP_agency_name.getText()).toString();
                            userType = "Agent";
                            theUser = new Agent(firstName, lastName, email, phoneNum, password,uid, imageUrl, agencyName);
                        } else {
                            userType = "Client";
                            theUser = new Client(firstName, lastName, email, phoneNum, password, uid, imageUrl);
                        }
                        MyDbDataManager.getInstance().storeNewUser(theUser, userType);
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));

                    }

                    @Override
                    public void onCreationFailed(Exception exception) {
                        Toast.makeText(RegisterActivity.this,  exception.getMessage(), Toast.LENGTH_SHORT).show();
                        register_REL_main.setAlpha(1);
                        register_REL_main.setEnabled(true);
                        register_CARD_loading.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                    }
                });
            }
        });

        haveAccount.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        });
    }

    public void showClientFields() {
        resetLayout();
        register_LAY_agency_name.setVisibility(View.GONE);
    }

    public void showAgentFields() {
        resetLayout();
        register_LAY_agency_name.setVisibility(View.VISIBLE);
    }

    public void resetLayout() {
        register_LAY_first_name.setError(null);
        register_LAY_last_name.setError(null);
        register_LAY_agency_name.setError(null);
        register_LAY_email.setError(null);
        register_LAY_phone.setError(null);
        register_LAY_password.setError(null);
    }

    private boolean validateFields() {
        boolean isValid = true;

        // Validate first name
        if (TextUtils.isEmpty(register_INP_first_name.getText())) {
            register_LAY_first_name.setError("First name is required");
            isValid = false;
        } else {
            register_LAY_first_name.setError(null);
        }

        // Validate last name
        if (TextUtils.isEmpty(register_INP_last_name.getText())) {
            register_LAY_last_name.setError("Last name is required");
            isValid = false;
        } else {
            register_LAY_last_name.setError(null);
        }

        // Validate agency name
        if (isAgent && TextUtils.isEmpty(register_INP_agency_name.getText())) {
            register_LAY_agency_name.setError("Agency name is required");
            isValid = false;
        } else {
            register_LAY_agency_name.setError(null);
        }

        // Validate email
        if (TextUtils.isEmpty(register_INP_email.getText())) {
            register_LAY_email.setError("Email is required");
            isValid = false;
        } else {
            register_LAY_email.setError(null);
        }

        // Validate phone number
        if (TextUtils.isEmpty(register_INP_phone.getText())) {
            register_LAY_phone.setError("Phone number is required");
            isValid = false;
        } else {
            register_LAY_phone.setError(null);
        }

        // Validate password
        if (TextUtils.isEmpty(register_INP_password.getText())) {
            register_LAY_password.setError("Password is required");
            isValid = false;
        } else {
            register_LAY_password.setError(null);
        }

        return isValid;
    }

    public static String fixName(String name) {

        String firstLetter = name.substring(0, 1).toUpperCase();
        String remainingLetters = name.substring(1).toLowerCase();

        return firstLetter + remainingLetters;
    }

    private void findViews() {
        toggleGroup = findViewById(R.id.register_GRP_user_type);

        register_INP_first_name = findViewById(R.id.register_INP_first_name);
        register_INP_last_name = findViewById(R.id.register_INP_last_name);
        register_INP_agency_name = findViewById(R.id.register_INP_agency_name);
        register_INP_email = findViewById(R.id.register_INP_email);
        register_INP_phone = findViewById(R.id.register_INP_phone);
        register_INP_password = findViewById(R.id.register_INP_password);

        register_LAY_first_name = findViewById(R.id.register_LAY_first_name);
        register_LAY_last_name = findViewById(R.id.register_LAY_last_name);
        register_LAY_agency_name = findViewById(R.id.register_LAY_agency_name);
        register_LAY_email = findViewById(R.id.register_LAY_email);
        register_LAY_phone = findViewById(R.id.register_LAY_phone);
        register_LAY_password = findViewById(R.id.register_LAY_password);

        register_REL_main = findViewById(R.id.register_REL_main);

        register_CARD_loading = findViewById(R.id.register_CARD_loading);

        btnRegister = findViewById(R.id.register_BTN_register);
        haveAccount = findViewById(R.id.register_LBL_have_account);
    }
}
