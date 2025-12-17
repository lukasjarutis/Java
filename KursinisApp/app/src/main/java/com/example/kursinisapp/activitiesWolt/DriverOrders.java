package com.example.kursinisapp.activitiesWolt;

import static com.example.kursinisapp.Utils.Constants.GET_ORDERS_BY_USER;
import static com.example.kursinisapp.Utils.Constants.UPDATE_ORDER_STATUS;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.kursinisapp.R;
import com.example.kursinisapp.Utils.RestOperations;
import com.example.kursinisapp.model.AuthResponse;
import com.example.kursinisapp.model.OrderResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DriverOrders extends AppCompatActivity implements DriverOrdersAdapter.OnOrderActionListener {

    private AuthResponse currentUser;
    private List<OrderResponse> activeOrders = new ArrayList<>();
    private DriverOrdersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_driver_orders);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String userJson = getIntent().getStringExtra("userJsonObject");
        currentUser = new Gson().fromJson(userJson, AuthResponse.class);

        ListView ordersList = findViewById(R.id.driverOrdersList);
        adapter = new DriverOrdersAdapter(this, activeOrders, this);
        ordersList.setAdapter(adapter);

        loadActiveOrders();
    }

    private void loadActiveOrders() {
        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            try {
                String response = RestOperations.sendGet(GET_ORDERS_BY_USER);
                handler.post(() -> handleOrdersResponse(response));
            } catch (IOException e) {
                handler.post(() -> Toast.makeText(this, R.string.driver_orders_error, Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void handleOrdersResponse(String response) {
        if (response.equals("Error")) {
            Toast.makeText(this, R.string.driver_orders_error, Toast.LENGTH_SHORT).show();
            return;
        }

        Type ordersListType = new TypeToken<List<OrderResponse>>() { }.getType();
        List<OrderResponse> ordersListFromJson = new com.google.gson.Gson().fromJson(response, ordersListType);
        activeOrders.clear();
        for (OrderResponse order : ordersListFromJson) {
            if (!"Pristatytas".equalsIgnoreCase(order.getStatus())) {
                activeOrders.add(order);
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onStatusChange(OrderResponse order, String newStatus) {
        JsonObject payload = new JsonObject();
        payload.addProperty("status", newStatus);
        payload.addProperty("driverId", currentUser.getId());

        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            try {
                String response = RestOperations.sendPut(UPDATE_ORDER_STATUS + order.getId(), payload.toString());
                handler.post(() -> {
                    if (!response.equals("Error")) {
                        Toast.makeText(this, R.string.driver_order_updated, Toast.LENGTH_SHORT).show();
                        loadActiveOrders();
                    } else {
                        Toast.makeText(this, R.string.driver_order_update_error, Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (IOException e) {
                handler.post(() -> Toast.makeText(this, R.string.driver_order_update_error, Toast.LENGTH_SHORT).show());
            }
        });
    }

    public void viewMyAccount(View view) {
        Intent intent = new Intent(DriverOrders.this, MyAccountActivity.class);
        intent.putExtra("userJsonObject", new Gson().toJson(currentUser));
        startActivity(intent);
    }
}
