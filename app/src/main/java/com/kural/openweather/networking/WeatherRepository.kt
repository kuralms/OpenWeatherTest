package com.kural.openweather.networking

import com.kural.openweather.data.WeatherResponse

class WeatherRepository(private val weatherService: WeatherService) {
    suspend fun getCurrentWeather(city: String, apiKey: String): WeatherResponse {
        val query = if (city.contains(",US", ignoreCase = true)) city else "$city,US"
        return weatherService.getCurrentWeather(query, apiKey)
    }
}
