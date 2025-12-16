package com.example.kursinisapp.activitiesWolt;

import static com.example.kursinisapp.Utils.Constants.CREATE_ORDER;
import static com.example.kursinisapp.Utils.Constants.GET_RESTAURANT_MENU;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.kursinisapp.R;
import com.example.kursinisapp.Utils.LocalDateTimeAdapter;
import com.example.kursinisapp.Utils.RestOperations;
import com.example.kursinisapp.model.Cuisine;
import com.example.kursinisapp.model.MenuItemResponse;
import com.example.kursinisapp.model.RestaurantResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MenuActivity extends AppCompatActivity implements MenuAdapter.OnQuantityChangeListener {

    private long userId;
    private long restaurantId;
    private MenuAdapter menuAdapter;
    private TextView orderTotalTextView;
    private TextView orderItemsCountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        userId = intent.getLongExtra("userId", 0);
        restaurantId = intent.getLongExtra("restaurantId", 0);

        orderTotalTextView = findViewById(R.id.orderTotal);
        orderItemsCountTextView = findViewById(R.id.orderItemsCount);

        loadMenu();
    }

    private void loadMenu() {
        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            try {
                String response = RestOperations.sendGet(GET_RESTAURANT_MENU + restaurantId);
                handler.post(() -> handleMenuResponse(response));
            } catch (IOException e) {
                handler.post(() -> Toast.makeText(this, "Network error", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void handleMenuResponse(String response) {
        try {
            if (!response.equals("Error")) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
                Gson gsonMenu = gsonBuilder.setPrettyPrinting().create();
                RestaurantResponse restaurant = gsonMenu.fromJson(response, RestaurantResponse.class);

                List<Cuisine> menuListFromJson = new ArrayList<>();
                if (restaurant.getMenuItems() != null) {
                    for (MenuItemResponse item : restaurant.getMenuItems()) {
                        Cuisine cuisine = new Cuisine();
                        cuisine.setId((int) item.getId());
                        cuisine.setName(item.getName());
                        cuisine.setIngredients(item.getDescription());
                        cuisine.setPrice(item.getCurrentPrice());
                        cuisine.setSpicy(false);
                        cuisine.setVegan(false);
                        menuListFromJson.add(cuisine);
                    }
                }

                if (menuListFromJson.isEmpty()) {
                    Toast.makeText(this, "Šiuo metu nėra patiekalų", Toast.LENGTH_SHORT).show();
                }

                ListView menuListElement = findViewById(R.id.menuItems);
                menuAdapter = new MenuAdapter(this, menuListFromJson);
                menuAdapter.setOnQuantityChangeListener(this);
                menuListElement.setAdapter(menuAdapter);

                updateOrderSummary();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error loading menu", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateOrderSummary() {
        if (menuAdapter == null) return;

        Map<Integer, Integer> quantities = menuAdapter.getQuantities();
        List<Cuisine> menuItems = menuAdapter.getMenuItems();

        double total = 0.0;
        int itemCount = 0;

        for (Cuisine cuisine : menuItems) {
            int quantity = quantities.getOrDefault(cuisine.getId(), 0);
            if (quantity > 0) {
                total += cuisine.getPrice() * quantity;
                itemCount += quantity;
            }
        }

        orderTotalTextView.setText(String.format("Total: €%.2f", total));
        orderItemsCountTextView.setText(String.format("Items: %d", itemCount));
    }

    public void placeOrder(View view) {
        if (menuAdapter == null) {
            Toast.makeText(this, "Menu not loaded yet", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<Integer, Integer> quantities = menuAdapter.getQuantities();
        List<Cuisine> menuItems = menuAdapter.getMenuItems();

        boolean hasItems = false;
        double total = 0.0;
        for (Cuisine cuisine : menuItems) {
            int quantity = quantities.getOrDefault(cuisine.getId(), 0);
            if (quantity > 0) {
                hasItems = true;
                total += cuisine.getPrice() * quantity;
            }
        }

        if (!hasItems) {
            Toast.makeText(this, "Please add items to your order", Toast.LENGTH_SHORT).show();
            return;
        }

        Gson gson = new Gson();
        JsonObject orderJson = new JsonObject();
        orderJson.addProperty("customerId", userId);
        orderJson.addProperty("restaurantId", restaurantId);
        orderJson.addProperty("totalPrice", total);

        String orderData = gson.toJson(orderJson);

        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            try {
                String response = RestOperations.sendPost(CREATE_ORDER, orderData);
                handler.post(() -> {
                    if (!response.equals("Error") && !response.isEmpty()) {
                        Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_SHORT).show();
                        menuAdapter.getQuantities().clear();
                        menuAdapter.notifyDataSetChanged();
                        updateOrderSummary();
                        finish();
                    } else {
                        Toast.makeText(this, "Failed to place order", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (IOException e) {
                handler.post(() -> Toast.makeText(this, "Network error", Toast.LENGTH_SHORT).show());
            }
        });
    }

    @Override
    public void onQuantityChanged() {
        updateOrderSummary();
    }
}
