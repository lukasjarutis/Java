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
import com.example.kursinisapp.model.OrderResponse;

import java.util.List;

public class MyOrdersAdapter extends ArrayAdapter<OrderResponse> {

    public MyOrdersAdapter(@NonNull Context context, @NonNull List<OrderResponse> orders) {
        super(context, 0, orders);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_order, parent, false);
        }

        OrderResponse order = getItem(position);

        TextView restaurantLabel = view.findViewById(R.id.orderRestaurant);
        TextView orderTitle = view.findViewById(R.id.orderTitle);
        TextView orderPrice = view.findViewById(R.id.orderPrice);

        if (order != null) {
            String restaurantName = order.getRestaurant() != null ? order.getRestaurant().getName() : "Unknown";
            restaurantLabel.setText(restaurantName);

            orderTitle.setText("Order #" + order.getId() + " (" + order.getStatus() + ")");
            orderPrice.setText("€" + String.format("%.2f", order.getTotalPrice()));
        }

        return view;
    }
}
