package com.example.homematch.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.homematch.R;
import com.example.homematch.Interfaces.HouseDetailsCallBack;
import com.example.homematch.Models.House;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class HouseAdapter extends RecyclerView.Adapter<HouseAdapter.HouseViewHolder> {

    private Context context;
    private ArrayList<House> allHousesList;
    private ArrayList<SlideModel> imageList;
    private HouseDetailsCallBack houseDetailsCallBack;

    public HouseAdapter(Context context, ArrayList<House> allHousesList) {
        this.context = context;
        this.allHousesList = allHousesList;
        imageList = new ArrayList<>();

    }

    @NonNull
    @Override
    public HouseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.house_item, parent, false);
        return new HouseViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull HouseViewHolder holder, int position) {
        House house = getItem(position);
        setAllImagesList(house);
        holder.property_image_slider.setImageList(imageList);
//        holder.property_IMG_favorite =
        holder.property_LBL_status.setText(String.valueOf("For " + house.getPurchaseType()));
//        holder.property_CARD_data =
        holder.property_LBL_address.setText(String.valueOf(house.getStreet() + " " + house.getStreetNumber() + ", " + house.getCity()));
        holder.property_LBL_size.setText(formatWithCommas(house.getAreaSize()) + " m²");
        holder.property_LBL_rooms.setText(formatWithCommas(house.getNumberOfRooms()) + " Rooms" );
        holder.property_LBL_price.setText("₪" + formatWithCommas(house.getPrice()));

    }

    public void setHouseDetailsCallBack(HouseDetailsCallBack houseDetailsCallBack) {
        this.houseDetailsCallBack = houseDetailsCallBack;
    }



    public void setAllImagesList(House house){
        imageList.clear();
        for(String imageUrl : house.getImagesUrl()){
            imageList.add(new SlideModel(imageUrl, ScaleTypes.CENTER_CROP));
            //imageList.add(new SlideModel("https://bit.ly/2YoJ77H", "The animal population decreased by 58 percent in 42 years.", ScaleTypes.CENTER_CROP));
        }
    }

    public String formatWithCommas(int number) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        return numberFormat.format(number);
    }

    @Override
    public int getItemCount() {
        return allHousesList == null ? 0 : allHousesList.size();

    }

    private House getItem(int position) {

        return allHousesList.get(position);
    }

    public class HouseViewHolder extends RecyclerView.ViewHolder {

        private ImageSlider property_image_slider;
        private ShapeableImageView property_IMG_favorite;
        private MaterialButton property_LBL_status;
        private CardView property_CARD_data;
        private MaterialTextView property_LBL_address;
        private MaterialTextView property_LBL_size;
        private MaterialTextView property_LBL_rooms;
        private MaterialTextView property_LBL_price;



        public HouseViewHolder(@NonNull View itemView) {
            super(itemView);

            imageList = new ArrayList<>();
            property_image_slider = itemView.findViewById(R.id.property_image_slider);
            property_IMG_favorite = itemView.findViewById(R.id.property_IMG_favorite);
            property_LBL_status = itemView.findViewById(R.id.property_LBL_status);
            property_CARD_data = itemView.findViewById(R.id.property_CARD_data);
            property_LBL_address = itemView.findViewById(R.id.property_LBL_address);
            property_LBL_size = itemView.findViewById(R.id.property_LBL_size);
            property_LBL_rooms = itemView.findViewById(R.id.property_LBL_rooms);
            property_LBL_price = itemView.findViewById(R.id.property_LBL_price);

            itemView.setOnClickListener(v -> {
                if (houseDetailsCallBack != null) {
                    houseDetailsCallBack.watchHouseDetails(getItem(getAdapterPosition()));
                }
            });
        }
    }

}
