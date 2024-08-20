package com.example.homematch.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homematch.Models.House;
import com.example.homematch.R;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

public class WeeklyOpenHousesAdapter extends RecyclerView.Adapter<WeeklyOpenHousesAdapter.WeeklyOpenHousesViewHolder> {

    private ArrayList<House> houses;
    private Context context;

    public WeeklyOpenHousesAdapter(ArrayList<House> houses, Context context) {
        this.houses = houses;
        this.context = context;
    }

    @NonNull
    @Override
    public WeeklyOpenHousesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.weekly_open_houses_table_row_item, parent, false);
        return new WeeklyOpenHousesViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull WeeklyOpenHousesViewHolder holder, int position) {
        House house = houses.get(position);
        holder.client_MTV_date.setText(house.getOpenHouseDate());
        holder.client_MTV_time.setText(house.getOpenHouseTime());
        holder.client_MTV_city.setText(house.getCity());
        holder.client_MTV_street.setText(house.getStreet() + " " + house.getStreetNumber());

    }

    @Override
    public int getItemCount() {
        return houses == null ? 0 : houses.size();
    }

    public static class WeeklyOpenHousesViewHolder extends RecyclerView.ViewHolder {

        MaterialTextView client_MTV_date;
        MaterialTextView client_MTV_time;
        MaterialTextView client_MTV_city;
        MaterialTextView client_MTV_street;

        public WeeklyOpenHousesViewHolder(@NonNull View itemView) {
            super(itemView);
            client_MTV_date = itemView.findViewById(R.id.client_MTV_date);
            client_MTV_time = itemView.findViewById(R.id.client_MTV_time);
            client_MTV_city = itemView.findViewById(R.id.client_MTV_city);
            client_MTV_street = itemView.findViewById(R.id.client_MTV_street);
        }
    }

}
