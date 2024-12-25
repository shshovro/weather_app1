package com.example.app_assignment3;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText cityInput;
    private TextView weatherInfo;
    private TextView selectedCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        cityInput = findViewById(R.id.cityInput);
        Button getWeatherButton = findViewById(R.id.getWeatherButton);
        weatherInfo = findViewById(R.id.weatherInfo);
        selectedCity = findViewById(R.id.selectedCity); // New TextView for selected city

        // Set click listener on the "Get Weather" button
        getWeatherButton.setOnClickListener(v -> {
            String city = cityInput.getText().toString().trim();
            if (TextUtils.isEmpty(city)) {
                Toast.makeText(MainActivity.this, "Search by your City", Toast.LENGTH_SHORT).show();
            } else {
                selectedCity.setText(String.format("Selected City: %s", city));
                fetchWeather(city);
            }
        });
    }

    private void fetchWeather(String city) {
        new Thread(() -> {
            try {
                String apiKey = "d9b60cd6a31ee04a89c737740e6e94cf";
                String apiUrl = String.format("https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s", city, apiKey);

                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                int responseCode = connection.getResponseCode();
                if (responseCode == 200) { // HTTP OK
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    // Parse the JSON response
                    JSONObject jsonResponse = new JSONObject(response.toString());
                    JSONObject main = jsonResponse.getJSONObject("main");
                    double temp = main.getDouble("temp") - 273.15; // Convert Kelvin to Celsius

                    // Update the UI
                    updateWeatherInfo(String.format("Temperature in %s: %.2f°C", city, temp));
                } else {
                    updateWeatherInfo("Selected City not found.");
                }

            } catch (Exception e) {
                e.printStackTrace();
                updateWeatherInfo("Error: Unable to fetch weather. Please try again.");
            }
        }).start();
    }

    private void updateWeatherInfo(String message) {
        // Update UI on the main thread
        new Handler(Looper.getMainLooper()).post(() -> weatherInfo.setText(message));
    }
}
