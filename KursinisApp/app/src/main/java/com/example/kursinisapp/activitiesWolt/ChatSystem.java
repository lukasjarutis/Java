package com.example.kursinisapp.activitiesWolt;

import static com.example.kursinisapp.Utils.Constants.GET_MESSAGES_BY_ORDER;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.kursinisapp.R;
import com.example.kursinisapp.Utils.LocalDateAdapter;
import com.example.kursinisapp.Utils.RestOperations;
import com.example.kursinisapp.model.ChatMessageResponse;
import com.example.kursinisapp.model.OrderResponse;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ChatSystem extends AppCompatActivity {

    private long orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat_system);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        orderId = intent.getLongExtra("orderId", 0);

        loadMessages();
    }

    private void loadMessages() {
        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            try {
                String response = RestOperations.sendGet(GET_MESSAGES_BY_ORDER + orderId);
                handler.post(() -> handleMessageResponse(response));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }

    private void handleMessageResponse(String response) {
        try {
            if (!response.equals("Error")) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
                OrderResponse order = gsonBuilder.create().fromJson(response, OrderResponse.class);

                List<ChatMessageResponse> messages = order.getMessages() != null ? order.getMessages() : new ArrayList<>();
                ListView messagesListElement = findViewById(R.id.messageList);
                ArrayAdapter<ChatMessageResponse> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, messages);
                messagesListElement.setAdapter(adapter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(View view) {
        Toast.makeText(this, "Messaging is not supported on the server", Toast.LENGTH_SHORT).show();
    }
}
