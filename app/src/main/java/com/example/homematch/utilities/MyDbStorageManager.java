package com.example.homematch.utilities;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.homematch.interaces.ImgCallBack;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MyDbStorageManager {

    private static Context context;
    private static MyDbStorageManager instance;
    private FirebaseStorage storage;

    private MyDbStorageManager(Context context) {
        this.context = context;
        this.storage = FirebaseStorage.getInstance();
    }

    public static void init(Context context) {
        if (instance == null) {
            instance = new MyDbStorageManager(context);
        }
    }

    public static MyDbStorageManager getInstance() {
        return instance;
    }

    public void uploadImage(Uri uri, String userUid, ImgCallBack imgcallBack) {
        StorageReference imageRef = storage.getReference().child("Users").child(userUid + ".jpg");

        UploadTask uploadTask = imageRef.putFile(uri);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                imgcallBack.onFailure(exception);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                imageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        String imageUrl = task.getResult().toString();
                        imgcallBack.onSuccess(imageUrl);
                        //MyDbManager.updateUserImage(imageUrl, res -> updateUI());
                    }
                });
            }
        });
    }

}
