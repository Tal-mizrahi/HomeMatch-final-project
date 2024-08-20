package com.example.homematch.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.homematch.Interfaces.HouseDeleteCallBack;
import com.example.homematch.Interfaces.ScheduleCallBack;
import com.example.homematch.Models.ShowPropertiesType;
import com.example.homematch.R;
import com.example.homematch.Interfaces.HouseDetailsCallBack;
import com.example.homematch.Models.House;
import com.example.homematch.Interfaces.OpenHouseSignUpCallBack;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class HouseAdapter extends RecyclerView.Adapter<HouseAdapter.HouseViewHolder> {

    private Context context;
    private ArrayList<House> allHousesList;
    private ArrayList<SlideModel> imageList;
    private HouseDetailsCallBack houseDetailsCallBack;
    private ShowPropertiesType showPropertiesType;
    private ScheduleCallBack scheduleCallBack;
    private OpenHouseSignUpCallBack openHouseSignUpCallBack;
    private HouseDeleteCallBack houseDeleteCallBack;

    public HouseAdapter(Context context, ArrayList<House> allHousesList, ShowPropertiesType showPropertiesType) {
        this.context = context;
        this.allHousesList = allHousesList;
        this.showPropertiesType = showPropertiesType;
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
        holder.property_LBL_address.setText(String.valueOf(house.getStreet() + " " + house.getStreetNumber() + ",\n" + house.getCity()));
        holder.property_LBL_size.setText(formatWithCommas(house.getAreaSize()) + " m²");
        holder.property_LBL_rooms.setText(formatWithCommas(house.getNumberOfRooms()) + " Rooms" );
        holder.property_LBL_price.setText("₪" + formatWithCommas(house.getPrice()));

        if(house.getOpenHouseTime() != null && house.getOpenHouseDate() != null){
            holder.property_CARD_open_day.setVisibility(View.VISIBLE);
            holder.property_LBL_open_day_date.setText(house.getOpenHouseDate());
            holder.property_LBL_open_day_time.setText(house.getOpenHouseTime());
            if(showPropertiesType.equals(ShowPropertiesType.ALL_HOUSES_CLIENT)) {
                holder.property_BTN_sign_up_to_house.setVisibility(View.VISIBLE);
                holder.property_BTN_sign_up_to_house.setOnClickListener(v -> {
                    if (openHouseSignUpCallBack != null) {
                        openHouseSignUpCallBack.onSignUpOpenHouse(house, position);
                    }
                });
            }
        }
        if(!showPropertiesType.equals(ShowPropertiesType.AGENT_PROPERTIES)){
            holder.agentProperty_LAY_SOH.setVisibility(View.GONE);
        } else {

            holder.agentProperty_BTN_open_house.setOnClickListener(v -> {
                if(scheduleCallBack != null){
                    scheduleCallBack.onScheduleOpenHouse(getItem(position), position);
                }
            });
            holder.agentProperty_BTN_purchased.setOnClickListener(v -> {
                housePurchased(house);
            });
            
        }

    }

    private void housePurchased(House house) {
        if(houseDeleteCallBack != null){
            houseDeleteCallBack.onHouseDeleted(house);
        }
    }

    public void setHouseDetailsCallBack(HouseDetailsCallBack houseDetailsCallBack) {
        this.houseDetailsCallBack = houseDetailsCallBack;
    }

    public void setScheduleCallBack(ScheduleCallBack scheduleCallBack) {
        this.scheduleCallBack = scheduleCallBack;
    }

    public void setOpenHouseSignUpCallBack(OpenHouseSignUpCallBack openHouseSignUpCallBack) {
        this.openHouseSignUpCallBack = openHouseSignUpCallBack;
    }

    public void setHouseDeleteCallBack(HouseDeleteCallBack houseDeleteCallBack) {
        this.houseDeleteCallBack = houseDeleteCallBack;
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
        private MaterialButton property_LBL_status;
        private CardView property_CARD_data;
        private MaterialTextView property_LBL_address;
        private MaterialTextView property_LBL_size;
        private MaterialTextView property_LBL_rooms;
        private MaterialTextView property_LBL_price;
        private CardView property_CARD_open_day;
        private MaterialTextView property_LBL_open_day_date;
        private MaterialTextView property_LBL_open_day_time;
        private LinearLayoutCompat agentProperty_LAY_SOH;
        private MaterialButton agentProperty_BTN_open_house;
        private MaterialButton agentProperty_BTN_purchased;
        private MaterialButton property_BTN_sign_up_to_house;



        public HouseViewHolder(@NonNull View itemView) {
            super(itemView);

            imageList = new ArrayList<>();
            property_image_slider = itemView.findViewById(R.id.property_image_slider);
            property_LBL_status = itemView.findViewById(R.id.property_LBL_status);
            property_CARD_data = itemView.findViewById(R.id.property_CARD_data);
            property_LBL_address = itemView.findViewById(R.id.property_LBL_address);
            property_LBL_size = itemView.findViewById(R.id.property_LBL_size);
            property_LBL_rooms = itemView.findViewById(R.id.property_LBL_rooms);
            property_LBL_price = itemView.findViewById(R.id.property_LBL_price);
            property_CARD_open_day = itemView.findViewById(R.id.property_CARD_open_day);
            property_LBL_open_day_date = itemView.findViewById(R.id.property_LBL_open_day_date);
            property_LBL_open_day_time = itemView.findViewById(R.id.property_LBL_open_day_time);
            agentProperty_LAY_SOH = itemView.findViewById(R.id.agentProperty_LAY_SOH);
            agentProperty_BTN_open_house = itemView.findViewById(R.id.agentProperty_BTN_open_house);
            agentProperty_BTN_purchased = itemView.findViewById(R.id.agentProperty_BTN_purchased);
            property_BTN_sign_up_to_house = itemView.findViewById(R.id.property_BTN_sign_up_to_house);

            itemView.setOnClickListener(v -> {
                if (houseDetailsCallBack != null) {
                    houseDetailsCallBack.watchHouseDetails(getItem(getAdapterPosition()));
                }
            });
        }
    }

}
