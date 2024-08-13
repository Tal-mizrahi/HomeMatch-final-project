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
import com.example.homematch.Interfaces.AgentPropertiesCallBack;
import com.example.homematch.R;
import com.example.homematch.Interfaces.HouseDetailsCallBack;
import com.example.homematch.Models.House;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class AgentPropertiesAdapter extends RecyclerView.Adapter<AgentPropertiesAdapter.AgentPropertiesViewHolder> {

    private Context context;
    private ArrayList<House> allHousesList;
    private ArrayList<SlideModel> imageList;
    private HouseDetailsCallBack houseDetailsCallBack;
    private AgentPropertiesCallBack agentPropertiesCallBack;

    public AgentPropertiesAdapter(Context context, ArrayList<House> allHousesList) {
        this.context = context;
        this.allHousesList = allHousesList;
        this.imageList = new ArrayList<>();
    }

    @NonNull
    @Override
    public AgentPropertiesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.agent_property_item, parent, false);
        return new AgentPropertiesViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AgentPropertiesViewHolder holder, int position) {
        House house = getItem(position);
        setAllImagesList(house);
        holder.agentProperty_image_slider.setImageList(imageList);
        holder.agentProperty_LBL_status.setText("For " + house.getPurchaseType());
        holder.agentProperty_LBL_address.setText(house.getStreet() + " " + house.getStreetNumber() + ", " + house.getCity());
        holder.agentProperty_LBL_size.setText(formatWithCommas(house.getAreaSize()) + " m²");
        holder.agentProperty_LBL_rooms.setText(formatWithCommas(house.getNumberOfRooms()) + " Rooms");
        holder.agentProperty_LBL_price.setText("₪" + formatWithCommas(house.getPrice()));

        // Set up Open House button and Purchased button
        holder.agentProperty_BTN_open_house.setOnClickListener(v -> {
            if (agentPropertiesCallBack != null) {
                agentPropertiesCallBack.openHouseEvent(house);
            }
        });

        holder.agentProperty_BTN_purchased.setOnClickListener(v -> {
            if (agentPropertiesCallBack != null) {
                agentPropertiesCallBack.markHouseAsPurchased(house);
            }
        });
    }

    public void setHouseDetailsCallBack(HouseDetailsCallBack houseDetailsCallBack) {
        this.houseDetailsCallBack = houseDetailsCallBack;
    }

    public void setAgentPropertiesCallBack(AgentPropertiesCallBack agentPropertiesCallBack) {
        this.agentPropertiesCallBack = agentPropertiesCallBack;
    }

    public void setAgentView() {

    }

    public void setClientUI() {

    }

    private void setAllImagesList(House house) {
        imageList.clear();
        for (String imageUrl : house.getImagesUrl()) {
            imageList.add(new SlideModel(imageUrl, ScaleTypes.CENTER_CROP));
        }
    }

    private String formatWithCommas(int number) {
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

    public class AgentPropertiesViewHolder extends RecyclerView.ViewHolder {

        private ImageSlider agentProperty_image_slider;
        private MaterialButton agentProperty_LBL_status;
        private MaterialTextView agentProperty_LBL_address;
        private MaterialTextView agentProperty_LBL_size;
        private MaterialTextView agentProperty_LBL_rooms;
        private MaterialTextView agentProperty_LBL_price;
        private MaterialButton agentProperty_BTN_purchased;
        private MaterialButton agentProperty_BTN_open_house;


        public AgentPropertiesViewHolder(@NonNull View itemView) {
            super(itemView);
            agentProperty_image_slider = itemView.findViewById(R.id.agentProperty_image_slider);
            agentProperty_LBL_status = itemView.findViewById(R.id.agentProperty_LBL_status);
            agentProperty_LBL_address = itemView.findViewById(R.id.agentProperty_LBL_address);
            agentProperty_LBL_size = itemView.findViewById(R.id.agentProperty_LBL_size);
            agentProperty_LBL_rooms = itemView.findViewById(R.id.agentProperty_LBL_rooms);
            agentProperty_LBL_price = itemView.findViewById(R.id.agentProperty_LBL_price);
            agentProperty_BTN_purchased = itemView.findViewById(R.id.agentProperty_BTN_purchased);
            agentProperty_BTN_open_house = itemView.findViewById(R.id.agentProperty_BTN_open_house);

            itemView.setOnClickListener(v -> {
                if (houseDetailsCallBack != null) {
                    houseDetailsCallBack.watchHouseDetails(getItem(getAdapterPosition()));
                }
            });
        }
    }
}
