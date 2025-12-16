package com.example.kursinisapp.activitiesWolt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.kursinisapp.R;
import com.example.kursinisapp.model.RestaurantResponse;

import java.util.List;

public class RestaurantAdapter extends ArrayAdapter<RestaurantResponse> {

    public RestaurantAdapter(@NonNull Context context, @NonNull List<RestaurantResponse> restaurants) {
        super(context, 0, restaurants);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_restaurant, parent, false);
        }

        RestaurantResponse restaurant = getItem(position);

        if (restaurant != null) {
            TextView nameTextView = convertView.findViewById(R.id.restaurantName);
            TextView addressTextView = convertView.findViewById(R.id.restaurantAddress);
            TextView phoneTextView = convertView.findViewById(R.id.restaurantPhone);
            TextView typeTextView = convertView.findViewById(R.id.restaurantType);
            TextView hoursTextView = convertView.findViewById(R.id.restaurantHours);

            nameTextView.setText(restaurant.getName());

            if (restaurant.getAddress() != null && !restaurant.getAddress().isEmpty()) {
                addressTextView.setText("📍 " + restaurant.getAddress());
            } else {
                addressTextView.setText("📍 Address not available");
            }

            phoneTextView.setText("📞 Not provided");
            typeTextView.setText("⭐ Popularity: " + String.format("%.1f", restaurant.getPopularityScore()));
            hoursTextView.setText(restaurant.isOpen() ? "✅ Open" : "⛔ Closed");
        }

        return convertView;
    }
}
