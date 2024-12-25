package com.example.app_assignment3;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherService {
    @GET("weather")
    com.example.app_assignment3.Call<WeatherResponse> getWeather(
            @Query("q") String city,
            @Query("appid") String apiKey
    );
}
