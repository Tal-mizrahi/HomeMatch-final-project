
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
import com.example.homematch.Interfaces.UserCallBack;
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

    private static Context context;
    private static volatile ShowDetailDialogManager instance;

    private ShowDetailDialogManager(Context context) {
        this.context = context;
    }


    public static void init(Context context) {
        if (instance == null) {
            instance = new ShowDetailDialogManager(context);
        }
    }

    public static ShowDetailDialogManager getInstance() {
        return instance;
    }

    public void showHouseDetailsDialog(Context context, House house) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.house_details_dialog);

        setupImageSlider(dialog, house);
        setupGeneralHouseInfo(dialog, house);
        setupHouseFeatures(dialog, house);
        setupAdditionalFeatures(dialog, house);
        getAgentInfo(house, dialog, context);

        MaterialButton houseDetails_BTN_close = dialog.findViewById(R.id.houseDetails_BTN_close);
        houseDetails_BTN_close.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void setupImageSlider(Dialog dialog, House house) {
        ImageSlider houseDetails_image_slider = dialog.findViewById(R.id.houseDetails_image_slider);
        ArrayList<SlideModel> imageList = new ArrayList<>();
        for (String imageUrl : house.getImagesUrl()) {
            imageList.add(new SlideModel(imageUrl, ScaleTypes.CENTER_CROP));
        }
        houseDetails_image_slider.setImageList(imageList);
    }

    @SuppressLint("SetTextI18n")
    private void setupGeneralHouseInfo(Dialog dialog, House house) {
        MaterialButton houseDetails_LBL_status = dialog.findViewById(R.id.houseDetails_LBL_status);
        houseDetails_LBL_status.setText("For " + house.getPurchaseType());

        MaterialButton houseDetails_LBL_property_type = dialog.findViewById(R.id.houseDetails_LBL_property_type);
        houseDetails_LBL_property_type.setText(house.getHouseType());

        MaterialTextView houseDetails_LBL_price = dialog.findViewById(R.id.houseDetails_LBL_price);
        houseDetails_LBL_price.setText("₪" + formatWithCommas(house.getPrice()));

        MaterialTextView houseDetails_LBL_address = dialog.findViewById(R.id.houseDetails_LBL_address);
        houseDetails_LBL_address.setText(house.getStreet() + " " + house.getStreetNumber() + ", " + house.getCity());

        MaterialTextView houseDetails_LBL_description = dialog.findViewById(R.id.houseDetails_LBL_description);
        if (house.getDescription() != null) {
            houseDetails_LBL_description.setText(house.getDescription());
        } else {
            houseDetails_LBL_description.setVisibility(View.GONE);
        }
    }

    @SuppressLint("SetTextI18n")
    private void setupHouseFeatures(Dialog dialog, House house) {
        MaterialTextView houseDetails_LBL_size = dialog.findViewById(R.id.houseDetails_LBL_area_size);
        houseDetails_LBL_size.setText(formatWithCommas(house.getAreaSize()) + " m²");

        MaterialTextView houseDetails_LBL_rooms = dialog.findViewById(R.id.houseDetails_LBL_rooms);
        houseDetails_LBL_rooms.setText(formatWithCommas(house.getNumberOfRooms()) + " Rooms");

        setupFeature(dialog, house.isHasProtectedRoom(), R.id.houseDetails_TXT_protected, R.id.houseDetails_IMG_protected);
        setupFeature(dialog, house.isHasGarage(), R.id.houseDetails_TXT_garage, R.id.houseDetails_IMG_garage);

        MaterialTextView houseDetails_LBL_balcony_size = dialog.findViewById(R.id.houseDetails_LBL_balcony_size);
        MaterialCardView houseDetails_CARD_balcony_size = dialog.findViewById(R.id.houseDetails_CARD_balcony_size);
        if (house.isHasBalcony()) {
            houseDetails_LBL_balcony_size.setText(formatWithCommas(house.getBalconyOrGardenSize()) + " m²");
        } else {
            houseDetails_CARD_balcony_size.setVisibility(View.GONE);
        }

        setupFeature(dialog, house.isHasElevator(), R.id.houseDetails_TXT_elevator, R.id.houseDetails_IMG_elevator);
        setupFeature(dialog, house.isHasParking(), R.id.houseDetails_TXT_parking, R.id.houseDetails_IMG_parking);
    }

    private void setupAdditionalFeatures(Dialog dialog, House house) {
        LinearLayoutCompat houseDetails_LAY_smoke = dialog.findViewById(R.id.houseDetails_LAY_smoke);
        LinearLayoutCompat houseDetails_LAY_pets = dialog.findViewById(R.id.houseDetails_LAY_pets);
        LinearLayoutCompat houseDetails_LAY_bills = dialog.findViewById(R.id.houseDetails_LAY_bills);

        if (house.getPurchaseType().equals("Sale")) {
            houseDetails_LAY_smoke.setVisibility(View.GONE);
            houseDetails_LAY_pets.setVisibility(View.GONE);
            houseDetails_LAY_bills.setVisibility(View.GONE);
        } else { // The house is for Rent
            setupFeature(dialog, house.isCanSmoke(), R.id.houseDetails_TXT_smoke, R.id.houseDetails_IMG_smoke);
            setupFeature(dialog, house.isPetsAllowed(), R.id.houseDetails_TXT_pets, R.id.houseDetails_IMG_pets);
            setupFeature(dialog, house.isBillsIncluded(), R.id.houseDetails_TXT_bills, R.id.houseDetails_IMG_bills);
        }
    }

    private void setupFeature(Dialog dialog, boolean isEnabled, int textId, int imageId) {
        MaterialTextView featureText = dialog.findViewById(textId);
        AppCompatImageView featureImage = dialog.findViewById(imageId);

        if (!isEnabled) {
            featureText.setTextColor(Color.parseColor("#999898"));
            featureImage.setColorFilter(ContextCompat.getColor(dialog.getContext(), R.color.grey_light));
        }
    }

    public void getAgentInfo(House house, Dialog dialog, Context context) {
        ShapeableImageView houseDetails_IMG_agent = dialog.findViewById(R.id.houseDetails_IMG_agent);
        MaterialTextView houseDetails_LBL_agent_name = dialog.findViewById(R.id.houseDetails_LBL_agent_name);
        MaterialTextView houseDetails_LBL_agent_agency = dialog.findViewById(R.id.houseDetails_LBL_agent_agency);
        MaterialTextView houseDetails_LBL_agent_phone = dialog.findViewById(R.id.houseDetails_LBL_agent_phone);

        MyDbDataManager.getInstance().getUser("Agent", house.getAgentId(), new UserCallBack() {
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
}
