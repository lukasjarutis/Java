package com.example.kursinisapp.activitiesWolt;

import static com.example.kursinisapp.Utils.Constants.GET_ALL_RESTAURANTS_URL;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.kursinisapp.R;
import com.example.kursinisapp.Utils.LocalDateTimeAdapter;
import com.example.kursinisapp.Utils.RestOperations;
import com.example.kursinisapp.model.AuthResponse;
import com.example.kursinisapp.model.RestaurantResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class WoltRestaurants extends AppCompatActivity {

    private AuthResponse currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_wolt_restaurants);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        String userInfo = intent.getStringExtra("userJsonObject");

        GsonBuilder build = new GsonBuilder();
        build.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        Gson gson = build.setPrettyPrinting().create();
        currentUser = gson.fromJson(userInfo, AuthResponse.class);

        loadRestaurants();
    }

    private void loadRestaurants() {
        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            try {
                String response = RestOperations.sendGet(GET_ALL_RESTAURANTS_URL);
                handler.post(() -> handleRestaurantResponse(response));
            } catch (IOException e) {
                handler.post(() -> Toast.makeText(this, "Nepavyko pasiekti serverio", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void handleRestaurantResponse(String response) {
        try {
            if (!response.equals("Error")) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
                Gson gsonRestaurants = gsonBuilder.setPrettyPrinting().create();
                Type restaurantListType = new TypeToken<List<RestaurantResponse>>() {
                }.getType();
                List<RestaurantResponse> restaurantListFromJson = gsonRestaurants.fromJson(response, restaurantListType);

                ListView restaurantListElement = findViewById(R.id.restaurantList);
                RestaurantAdapter adapter = new RestaurantAdapter(this, restaurantListFromJson);
                restaurantListElement.setAdapter(adapter);

                restaurantListElement.setOnItemClickListener((parent, view, position, id) -> {
                    RestaurantResponse selectedRestaurant = restaurantListFromJson.get(position);
                    AlertDialog.Builder builder = new AlertDialog.Builder(WoltRestaurants.this);
                    builder.setTitle(selectedRestaurant.getName());

                    StringBuilder details = new StringBuilder();
                    if (selectedRestaurant.getPopularityScore() > 0) {
                        details.append("⭐ ").append(String.format("%.1f", selectedRestaurant.getPopularityScore())).append("\n");
                    }
                    if (selectedRestaurant.getAddress() != null && !selectedRestaurant.getAddress().isEmpty()) {
                        details.append("📍 ").append(selectedRestaurant.getAddress()).append("\n");
                    }
                    details.append(selectedRestaurant.isOpen() ? "✅ Open" : "⛔ Closed");

                    builder.setMessage(details.toString());
                    builder.setPositiveButton("View menu", (dialog, which) -> {
                        Intent intentMenu = new Intent(WoltRestaurants.this, MenuActivity.class);
                        intentMenu.putExtra("restaurantId", selectedRestaurant.getId());
                        intentMenu.putExtra("userId", currentUser.getId());
                        startActivity(intentMenu);
                    });
                    builder.setNegativeButton("Close", null);
                    builder.show();
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Nepavyko nuskaityti restoranų", Toast.LENGTH_SHORT).show();
        }
    }

    public void viewPurchaseHistory(View view) {
        Intent intent = new Intent(WoltRestaurants.this, MyOrders.class);
        intent.putExtra("id", currentUser.getId());
        startActivity(intent);
    }

    public void viewMyAccount(View view) {
    }
}
