package com.kural.openweather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kural.openweather.networking.RetrofitClient
import com.kural.openweather.networking.WeatherRepository
import com.kural.openweather.ui.composables.WeatherScreen
import com.kural.openweather.ui.theme.OpenWeatherTheme
import com.kural.openweather.ui.viewmodel.WeatherViewModel
import com.kural.openweather.ui.viewmodel.WeatherViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OpenWeatherTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val repository = WeatherRepository(RetrofitClient.weatherService)
                    val viewModel: WeatherViewModel = viewModel(
                        factory = WeatherViewModelFactory(repository)
                    )
                    
                    WeatherScreen(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
