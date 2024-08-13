package com.example.homematch.Utilities;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.homematch.Interfaces.ImgListCallBack;
import com.example.homematch.Interfaces.ImgCallBack;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

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

//        UploadTask uploadTask = imageRef.putFile(uri);

        uploadTask(imageRef, imageRef.putFile(uri), imgcallBack);
        // Register observers to listen for when the download is done or if it fails
//        uploadTask.addOnFailureListener((exception) -> {
//            imgcallBack.onFailure(exception);
//        }).addOnSuccessListener((taskSnapshot) -> {
//                imageRef.getDownloadUrl().addOnCompleteListener((task) -> {
//                        String imageUrl = task.getResult().toString();
//                        imgcallBack.onSuccess(imageUrl);
//                        //MyDbManager.updateUserImage(imageUrl, res -> updateUI());
//                });
//        });
    }

    public void uploadHouseImage(Uri uri, String houseUuid, String imgId, ImgCallBack imgcallBack) {
        StorageReference imageRef = storage.getReference("Houses").child(houseUuid).child(imgId + ".jpg");

//        UploadTask uploadTask = imageRef.putFile(uri);
        uploadTask(imageRef, imageRef.putFile(uri), imgcallBack);
        // Register observers to listen for when the download is done or if it fails
//        uploadTask.addOnFailureListener((exception) -> {
//            imgcallBack.onFailure(exception);
//        }).addOnSuccessListener((taskSnapshot) -> {
//            imageRef.getDownloadUrl().addOnCompleteListener((task) -> {
//                String imageUrl = task.getResult().toString();
//                imgcallBack.onSuccess(imageUrl);
//                //MyDbManager.updateUserImage(imageUrl, res -> updateUI());
//            });
//        });
    }

    public void uploadHouseImages(ArrayList<Uri> imagesUri, String houseUuid, ImgListCallBack imagesCallBack) {
        String imgId;
        StorageReference rootRef = storage.getReference("Houses").child(houseUuid);
        UploadTask uploadTask;
        ArrayList<String> imageUrls = new ArrayList<>();
        for(int i = 0 ; i < imagesUri.size() ; i++){
            int j = i;
            imgId = "img" + i;
            StorageReference imageRef = rootRef.child(imgId + ".jpg");
            uploadTask = imageRef.putFile(imagesUri.get(i));
            // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(imagesCallBack::onFailure)
                .addOnSuccessListener((taskSnapshot) -> {
            imageRef.getDownloadUrl().addOnCompleteListener((task) -> {
                String imageUrl = task.getResult().toString();
                imageUrls.add(imageUrl);
                Log.d("AddingApartmentFragment", "DB onSuccess: " + imageUrl);
               Log.d("AddingApartmentFragment", "DB onSuccess j: " + j);
                if(j == imagesUri.size()-1) {
                    imagesCallBack.onSuccess(imageUrls);
                }
            });
        });

        }


    }

    private void uploadTask(StorageReference imageRef, UploadTask uploadTask, ImgCallBack imgcallBack){
        uploadTask.addOnFailureListener((exception) -> {
            imgcallBack.onFailure(exception);
        }).addOnSuccessListener((taskSnapshot) -> {
            imageRef.getDownloadUrl().addOnCompleteListener((task) -> {
                String imageUrl = task.getResult().toString();
                imgcallBack.onSuccess(imageUrl);
            });
        });
    }

}
