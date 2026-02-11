package com.kural.openweather.data

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    val main: Main,
    val name: String,
    val weather: List<WeatherDescription>,
    val wind: Wind,
    val sys: Sys
)

data class Main(
    val temp: Double,
    @SerializedName("feels_like") val feelsLike: Double,
    @SerializedName("temp_min") val tempMin: Double,
    @SerializedName("temp_max") val tempMax: Double,
    val humidity: Int,
    val pressure: Int
)

data class WeatherDescription(
    val main: String,
    val description: String,
    val icon: String
)

data class Wind(
    val speed: Double,
    val deg: Int
)

data class Sys(
    val country: String
)
