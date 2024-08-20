
package com.example.homematch.Utilities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.homematch.Models.Feature;
import com.example.homematch.R;
import com.example.homematch.Models.Agent;
import com.example.homematch.Models.House;
import com.example.homematch.Models.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.LinearLayoutCompat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ShowDetailDialogManager {

    private MaterialButton houseDetails_LBL_status;
    private ImageSlider houseDetails_image_slider;
    private ShapeableImageView houseDetails_IMG_agent;
    private MaterialTextView houseDetails_LBL_agent_name;
    //private MaterialTextView houseDetails_LBL_balcony;
    private MaterialTextView houseDetails_LBL_agent_agency;
    private MaterialTextView houseDetails_LBL_agent_phone;
    private MaterialButton houseDetails_BTN_close;
    private MaterialTextView houseDetails_LBL_size;
    private MaterialTextView houseDetails_LBL_rooms;
    private MaterialTextView houseDetails_LBL_balcony_size;
    private MaterialCardView houseDetails_CARD_balcony_size;
    private AppCompatImageView houseDetails_IMG_balcony_size;
    //private AppCompatImageView houseDetails_IMG_balcony;
    private LinearLayoutCompat houseDetails_LAY_smoke;
    private LinearLayoutCompat houseDetails_LAY_pets;
    private LinearLayoutCompat houseDetails_LAY_bills;
    private LinearLayoutCompat houseDetails_LAY_balcony;
    private LinearLayoutCompat houseDetails_LAY_garden;

    private MaterialButton houseDetails_LBL_property_type;
    private MaterialTextView houseDetails_LBL_price;
    private MaterialTextView houseDetails_LBL_address;
    private MaterialTextView houseDetails_LBL_description;

    private House house;

    private Dialog dialog;

    private Feature[] features;

    public ShowDetailDialogManager(Context context) {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.house_details_dialog);
        findViews();
    }


    public void showHouseDetailsDialog(Context context, House house) {
        this.house = house;
        setFeaturesArray();
        setupImageSlider();
        setupGeneralHouseInfo();
        setupHouseFeatures();
        getAgentInfo(context);
        houseDetails_BTN_close.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void setupImageSlider() {
        ArrayList<SlideModel> imageList = new ArrayList<>();
        for (String imageUrl : house.getImagesUrl()) {
            imageList.add(new SlideModel(imageUrl, ScaleTypes.CENTER_CROP));
        }
        houseDetails_image_slider.setImageList(imageList);
    }

    @SuppressLint("SetTextI18n")
    private void setupGeneralHouseInfo() {
        houseDetails_LBL_status.setText("For " + house.getPurchaseType());
        houseDetails_LBL_property_type.setText(house.getHouseType());
        houseDetails_LBL_price.setText("₪" + formatWithCommas(house.getPrice()));
        houseDetails_LBL_address.setText(house.getStreet() + " " + house.getStreetNumber() + ", " + house.getCity());
        if (house.getDescription() != null) {
            houseDetails_LBL_description.setVisibility(View.VISIBLE);
            houseDetails_LBL_description.setText(house.getDescription());
        } else {
            houseDetails_LBL_description.setVisibility(View.GONE);
        }
    }

    @SuppressLint("SetTextI18n")
    private void setupHouseFeatures() {
        houseDetails_LBL_size.setText(formatWithCommas(house.getAreaSize()) + " m²");
        houseDetails_LBL_rooms.setText(formatWithCommas(house.getNumberOfRooms()) + " Rooms");
        if (house.isHasBalcony()) {
            houseDetails_CARD_balcony_size.setVisibility(View.VISIBLE);
            houseDetails_LBL_balcony_size.setText(formatWithCommas(house.getBalconyOrGardenSize()) + " m²");
            if (house.getHouseType().equals("Garden Apartment")) {
                houseDetails_IMG_balcony_size.setImageResource(R.drawable.ic_garden);
            }
        } else {
            houseDetails_CARD_balcony_size.setVisibility(View.GONE);
        }
        setupFeature();
    }


    private void setupFeature() {

        if(house.getHouseType().equals("Garden Apartment")){
            houseDetails_LAY_balcony.setVisibility(View.GONE);
            houseDetails_LAY_garden.setVisibility(View.VISIBLE);
        }else{
            houseDetails_LAY_balcony.setVisibility(View.VISIBLE);
            houseDetails_LAY_garden.setVisibility(View.GONE);
        }

        for (Feature feature : features ){
            if (feature.isRent()) {
                feature.getFeatureLayout().setVisibility(View.VISIBLE);
            }
            if (!feature.isEnabled()) {
                feature.getFeatureText().setTextColor(Color.parseColor("#999898"));
                feature.getFeatureImage().setColorFilter(ContextCompat.getColor(dialog.getContext(), R.color.grey_light));
            }
        }
    }

    public void getAgentInfo(Context context) {
        MyDbDataManager.getInstance().getUser("Agent", house.getAgentId(), new MyDbDataManager.UserCallBack() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(User currentUser) {
                Agent agent = (Agent) currentUser;
                String imageUrl = agent.getImageUrl();
                if (imageUrl != null) {
                    Glide.with(dialog.getContext())
                            .load(imageUrl)
                            .placeholder(R.drawable.img_white)
                            .centerCrop()
                            .into(houseDetails_IMG_agent);

                    houseDetails_LBL_agent_name.setText(agent.getFirstName() + " " + agent.getLastName());
                    houseDetails_LBL_agent_agency.setText(agent.getAgencyName());
                    houseDetails_LBL_agent_phone.setText(agent.getPhoneNumber());
                }
            }

            @Override
            public void onFailure() {
                Toast.makeText(context, "Agent not found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String formatWithCommas(int number) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        return numberFormat.format(number);
    }

    private void findViews() {
        houseDetails_image_slider = dialog.findViewById(R.id.houseDetails_image_slider);
        houseDetails_IMG_agent = dialog.findViewById(R.id.houseDetails_IMG_agent);
        houseDetails_LBL_agent_name = dialog.findViewById(R.id.houseDetails_LBL_agent_name);
        houseDetails_LBL_agent_agency = dialog.findViewById(R.id.houseDetails_LBL_agent_agency);
        houseDetails_LBL_agent_phone = dialog.findViewById(R.id.houseDetails_LBL_agent_phone);
        houseDetails_BTN_close = dialog.findViewById(R.id.houseDetails_BTN_close);
        houseDetails_LBL_size = dialog.findViewById(R.id.houseDetails_LBL_area_size);
        houseDetails_LBL_rooms = dialog.findViewById(R.id.houseDetails_LBL_rooms);
        houseDetails_LBL_balcony_size = dialog.findViewById(R.id.houseDetails_LBL_balcony_size);
        houseDetails_CARD_balcony_size = dialog.findViewById(R.id.houseDetails_CARD_balcony_size);
        houseDetails_LAY_smoke = dialog.findViewById(R.id.houseDetails_LAY_smoke);
        houseDetails_LAY_pets = dialog.findViewById(R.id.houseDetails_LAY_pets);
        houseDetails_LAY_bills = dialog.findViewById(R.id.houseDetails_LAY_bills);
        houseDetails_LBL_status = dialog.findViewById(R.id.houseDetails_LBL_status);
        houseDetails_LBL_property_type = dialog.findViewById(R.id.houseDetails_LBL_property_type);
        houseDetails_LBL_price = dialog.findViewById(R.id.houseDetails_LBL_price);
        houseDetails_LBL_address = dialog.findViewById(R.id.houseDetails_LBL_address);
        houseDetails_LBL_description = dialog.findViewById(R.id.houseDetails_LBL_description);
        houseDetails_IMG_balcony_size = dialog.findViewById(R.id.houseDetails_IMG_balcony_size);
        houseDetails_LAY_balcony = dialog.findViewById(R.id.houseDetails_LAY_balcony);
        houseDetails_LAY_garden = dialog.findViewById(R.id.houseDetails_LAY_garden);

    }

    public void setFeaturesArray(){
       AppCompatImageView balconyOrGardenImg = house.getHouseType().equals("Garden Apartment") ? dialog.findViewById(R.id.houseDetails_IMG_garden) : dialog.findViewById(R.id.houseDetails_IMG_balcony);
       MaterialTextView balconyOrGardenLBL = house.getHouseType().equals("Garden Apartment") ? dialog.findViewById(R.id.houseDetails_LBL_garden) : dialog.findViewById(R.id.houseDetails_LBL_balcony);
       boolean isRent = house.getPurchaseType().equals("Rent");
        features = new Feature[] {
                new Feature(house.isHasProtectedRoom(), dialog.findViewById(R.id.houseDetails_LBL_protected), dialog.findViewById(R.id.houseDetails_IMG_protected), false),
                new Feature(house.isHasGarage(), dialog.findViewById( R.id.houseDetails_LBL_garage), dialog.findViewById(R.id.houseDetails_IMG_garage), false),
                new Feature(house.isHasElevator(), dialog.findViewById(R.id.houseDetails_LBL_elevator), dialog.findViewById(R.id.houseDetails_IMG_elevator), false),
                new Feature(house.isHasParking(), dialog.findViewById(R.id.houseDetails_LBL_parking), dialog.findViewById(R.id.houseDetails_IMG_parking), false),
                new Feature(house.isHasBalcony(), balconyOrGardenLBL, balconyOrGardenImg, false),
                new Feature(house.isCanSmoke(), dialog.findViewById(R.id.houseDetails_LBL_smoke), dialog.findViewById(R.id.houseDetails_IMG_smoke), isRent)
                        .setFeatureLayout(houseDetails_LAY_smoke), // setting additional feature that for Rental house and set the layout
                new Feature(house.isPetsAllowed(), dialog.findViewById(R.id.houseDetails_LBL_pets), dialog.findViewById(R.id.houseDetails_IMG_pets), isRent)
                        .setFeatureLayout(houseDetails_LAY_pets), // setting additional feature that for Rental house and set the layout
                new Feature(house.isBillsIncluded(), dialog.findViewById(R.id.houseDetails_LBL_bills), dialog.findViewById(R.id.houseDetails_IMG_bills), isRent)
                        .setFeatureLayout(houseDetails_LAY_bills) // setting additional feature that for Rental house and set the layout

        };
    }
}
