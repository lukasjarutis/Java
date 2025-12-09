package com.example.kursinisapp;

import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static com.example.kursinisapp.Constants.VALIDATE_USER_URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void validateUser(View view) {

        TextView login = findViewById(R.id.loginField);
        TextView password = findViewById(R.id.passwordField);

        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("login", login.getText().toString());
        jsonObject.addProperty("password", password.getText().toString());

        String data = gson.toJson(jsonObject);
        System.out.println(data);

        Executor  executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {

            try {
                String response = RestOperation.sendPost(VALIDATE_USER_URL, String.valueOf(gson));
                handler.post(() -> {
                    if(!response.equals("Error")) {
                        Intent intent = new Intent(MainActivity.this, WoltMain.class);
                        startActivity(intent);
                    }
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}