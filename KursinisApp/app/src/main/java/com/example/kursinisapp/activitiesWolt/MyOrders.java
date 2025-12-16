package com.example.kursinisapp.activitiesWolt;

import static com.example.kursinisapp.Utils.Constants.GET_ORDERS_BY_USER;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.kursinisapp.R;
import com.example.kursinisapp.Utils.RestOperations;
import com.example.kursinisapp.model.OrderResponse;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MyOrders extends AppCompatActivity {

    private long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_orders);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        userId = intent.getLongExtra("id", 0);

        loadOrders();
    }

    private void loadOrders() {
        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            try {
                String response = RestOperations.sendGet(GET_ORDERS_BY_USER);
                handler.post(() -> handleOrdersResponse(response));
            } catch (IOException e) {
                handler.post(() -> Toast.makeText(this, "Nepavyko pasiekti užsakymų", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void handleOrdersResponse(String response) {
        try {
            if (!response.equals("Error")) {
                Type ordersListType = new TypeToken<List<OrderResponse>>() {
                }.getType();
                List<OrderResponse> ordersListFromJson = new com.google.gson.Gson().fromJson(response, ordersListType);
                List<OrderResponse> userOrders = new ArrayList<>();

                for (OrderResponse order : ordersListFromJson) {
                    if (order.getCustomer() != null && order.getCustomer().getId() == userId) {
                        userOrders.add(order);
                    }
                }

                ListView ordersListElement = findViewById(R.id.myOrderList);
                MyOrdersAdapter adapter = new MyOrdersAdapter(this, userOrders);
                ordersListElement.setAdapter(adapter);

                ordersListElement.setOnItemClickListener((parent, view, position, id) -> {
                    OrderResponse order = userOrders.get(position);
                    Intent intentChat = new Intent(MyOrders.this, ChatSystem.class);
                    intentChat.putExtra("orderId", order.getId());
                    intentChat.putExtra("userId", userId);
                    startActivity(intentChat);
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
