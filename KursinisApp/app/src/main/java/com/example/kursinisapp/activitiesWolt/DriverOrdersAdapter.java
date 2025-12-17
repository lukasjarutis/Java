package com.example.kursinisapp.activitiesWolt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.kursinisapp.R;
import com.example.kursinisapp.model.OrderResponse;

import java.util.List;

public class DriverOrdersAdapter extends ArrayAdapter<OrderResponse> {

    public interface OnOrderActionListener {
        void onStatusChange(OrderResponse order, String newStatus);
    }

    private final OnOrderActionListener actionListener;

    public DriverOrdersAdapter(@NonNull Context context, @NonNull List<OrderResponse> orders, OnOrderActionListener listener) {
        super(context, 0, orders);
        this.actionListener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_driver_order, parent, false);
        }

        OrderResponse order = getItem(position);

        TextView restaurantLabel = view.findViewById(R.id.driverOrderRestaurant);
        TextView statusLabel = view.findViewById(R.id.driverOrderStatus);
        TextView priceLabel = view.findViewById(R.id.driverOrderPrice);
        Button pickupButton = view.findViewById(R.id.actionPickup);
        Button deliveringButton = view.findViewById(R.id.actionDelivering);
        Button deliveredButton = view.findViewById(R.id.actionDelivered);

        if (order != null) {
            String restaurantName = order.getRestaurant() != null ? order.getRestaurant().getName() : "Unknown";
            restaurantLabel.setText(restaurantName);
            statusLabel.setText(getContext().getString(R.string.order_status_label, order.getStatus()));
            priceLabel.setText(getContext().getString(R.string.order_price_label, order.getTotalPrice()));

            pickupButton.setOnClickListener(v -> actionListener.onStatusChange(order, "Vežamas"));
            deliveringButton.setOnClickListener(v -> actionListener.onStatusChange(order, "Vežamas"));
            deliveredButton.setOnClickListener(v -> actionListener.onStatusChange(order, "Pristatytas"));
        }

        return view;
    }
}
