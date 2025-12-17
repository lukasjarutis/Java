package com.example.kursinisapp.activitiesWolt;

import static com.example.kursinisapp.Utils.Constants.GET_USER_INFO;
import static com.example.kursinisapp.Utils.Constants.UPDATE_USER_INFO;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.kursinisapp.R;
import com.example.kursinisapp.Utils.RestOperations;
import com.example.kursinisapp.model.AuthResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MyAccountActivity extends AppCompatActivity {

    private AuthResponse currentUser;
    private EditText fullNameField;
    private EditText emailField;
    private EditText phoneField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String userJson = getIntent().getStringExtra("userJsonObject");
        currentUser = new Gson().fromJson(userJson, AuthResponse.class);

        fullNameField = findViewById(R.id.accountFullName);
        emailField = findViewById(R.id.accountEmail);
        phoneField = findViewById(R.id.accountPhone);

        loadUserInfo();
    }

    private void loadUserInfo() {
        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            try {
                String response = RestOperations.sendGet(GET_USER_INFO + currentUser.getId());
                handler.post(() -> {
                    if (!response.equals("Error") && !response.isEmpty()) {
                        AuthResponse refreshed = new Gson().fromJson(response, AuthResponse.class);
                        if (refreshed != null) {
                            currentUser = refreshed;
                            fullNameField.setText(refreshed.getFullName());
                            emailField.setText(refreshed.getEmail());
                            phoneField.setText(refreshed.getPhone());
                        }
                    } else {
                        Toast.makeText(this, R.string.account_load_error, Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (IOException e) {
                handler.post(() -> Toast.makeText(this, R.string.account_load_error, Toast.LENGTH_SHORT).show());
            }
        });
    }

    public void saveAccount(View view) {
        String fullName = fullNameField.getText().toString();
        String email = emailField.getText().toString();
        String phone = phoneField.getText().toString();

        JsonObject body = new JsonObject();
        body.addProperty("fullName", fullName);
        body.addProperty("email", email);
        body.addProperty("phone", phone);

        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            try {
                String response = RestOperations.sendPut(UPDATE_USER_INFO + currentUser.getId(), body.toString());
                handler.post(() -> {
                    if (!response.equals("Error")) {
                        Toast.makeText(this, R.string.account_saved, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, R.string.account_save_error, Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (IOException e) {
                handler.post(() -> Toast.makeText(this, R.string.account_save_error, Toast.LENGTH_SHORT).show());
            }
        });
    }
}
