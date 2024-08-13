package com.example.homematch.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.homematch.R;
import com.example.homematch.Interfaces.ImgRemovedCallBack;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private ArrayList<Uri> uriList;
    private Context context;
    private ImgRemovedCallBack imgRemovedCallBack;


    public ImageAdapter(ArrayList<Uri> uriList, Context context) {
        this.uriList = uriList;
        this.context = context;
    }

    public void setImgRemovedCallBack(ImgRemovedCallBack imgRemovedCallBack) {
        this.imgRemovedCallBack = imgRemovedCallBack;
    }

    public Uri getItem (int position){
        return uriList.get(position);
    }

//    public void removeItem(Uri uri){
//        uriList.remove(uri);
//        notifyItemRemoved(position);
//        notifyItemRangeChanged(position, getItemCount());
//    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.property_img_item, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Uri uri = getItem(position);
        Glide.with(context)
                .load(uri)
                .centerCrop()
                .into(holder.property_IMG_item);

        holder.property_BTN_remove.setOnClickListener(v -> {
            if(imgRemovedCallBack != null) {
                uriList.remove(getItem(position));
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, getItemCount());
                imgRemovedCallBack.removeImage(uriList.size());
            }
        });




    }

    @Override
    public int getItemCount() {
        return uriList == null ? 0 : uriList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        ExtendedFloatingActionButton property_BTN_remove;
        ShapeableImageView property_IMG_item;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            property_BTN_remove = itemView.findViewById(R.id.property_BTN_remove);
            property_IMG_item = itemView.findViewById(R.id.property_IMG_item);





        }

    }

}
