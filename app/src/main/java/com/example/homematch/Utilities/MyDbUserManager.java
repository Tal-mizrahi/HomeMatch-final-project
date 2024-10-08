package com.example.homematch.Utilities;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class MyDbUserManager {

    private static Context context;
    private static volatile MyDbUserManager instance;
    private FirebaseAuth mAuth;

    private MyDbUserManager(Context context) {
        this.context = context;
        this.mAuth = FirebaseAuth.getInstance();
    }
    public static void init(Context context) {
        if (instance == null) {
            instance = new MyDbUserManager(context);
        }
    }

    public static MyDbUserManager getInstance() {
        return instance;
    }

    public String getUidOfCurrentUser() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            return currentUser.getUid();
        } else {
            return null;
        }
    }


    public void createNewUser(String email, String password, Activity activity, UserCreationCallBack userCreationCallBack) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(activity, "User Created Successfully\n" , Toast.LENGTH_SHORT).show();
                            userCreationCallBack.onUserCreated(task.getResult().getUser().getUid());

                        } else {
                            userCreationCallBack.onUserCreationFailed(task.getException());
                        }

        });
    }

    public void loginUser(String email, String password, Activity activity, LoginCallBack userCallBack) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        userCallBack.onLoginSuccess(Objects.requireNonNull(task.getResult().getUser()).getUid());
                    } else {
                        userCallBack.onLoginFailure(task.getException());
                    }
                });
    }

    public void logOutUser() {
        mAuth.signOut();
    }


    public interface LoginCallBack {

        void onLoginSuccess(String uid);
        void onLoginFailure(Exception exception);

    }

    public interface UserCreationCallBack {
        void onUserCreated(String uid);
        void onUserCreationFailed(Exception exception);

    }
}




    




