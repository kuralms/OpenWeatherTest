package com.kural.openweather.networking

import com.kural.openweather.data.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "imperial" // Use imperial for US cities (Fahrenheit)
    ): WeatherResponse
}