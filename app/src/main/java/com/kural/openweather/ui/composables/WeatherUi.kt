package com.kural.openweather.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kural.openweather.networking.WeatherResponse
import com.kural.openweather.ui.viewmodel.WeatherUiState
import com.kural.openweather.ui.viewmodel.WeatherViewModel


@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel,
    modifier: Modifier = Modifier
) {
    var cityInput by remember { mutableStateOf("") }
    val uiState by viewModel.uiState

    // Using the provided API key move this to gradle to be used on build flavors
    val apiKey = "143b52bf7bbc806159b27ec0861117e6"

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        OutlinedTextField(
            value = cityInput,
            onValueChange = { cityInput = it },
            label = { Text("Enter US City (e.g., New York)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { viewModel.fetchWeather(cityInput, apiKey) },
            modifier = Modifier.fillMaxWidth(),
            enabled = uiState !is WeatherUiState.Loading
        ) {
            Text(if (uiState is WeatherUiState.Loading) "Loading..." else "Get Weather")
        }

        Spacer(modifier = Modifier.height(24.dp))

        when (val state = uiState) {
            is WeatherUiState.Loading -> {
                CircularProgressIndicator()
            }
            is WeatherUiState.Error -> {
                Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(8.dp)
                )
            }
            is WeatherUiState.Success -> {
                WeatherCard(state.weather)
            }
            WeatherUiState.Idle -> {
                Text("Enter a city to see the weather")
            }
        }
    }
}

@Composable
fun WeatherCard(weather: WeatherResponse) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "${weather.name}, ${weather.sys.country}",
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "${weather.main.temp.toInt()}°F",
                style = MaterialTheme.typography.displayMedium
            )
            Text(
                text = weather.weather.firstOrNull()?.description?.replaceFirstChar { it.uppercase() } ?: "",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
            // TODO Move the Hard coded string to String .xml for localization
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                WeatherDetailItem("Feels like", "${weather.main.feelsLike.toInt()}°F")
                WeatherDetailItem("Humidity", "${weather.main.humidity}%")
                WeatherDetailItem("Wind", "${weather.wind.speed.toInt()} mph")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                WeatherDetailItem("Low", "${weather.main.tempMin.toInt()}°F")
                WeatherDetailItem("High", "${weather.main.tempMax.toInt()}°F")
                WeatherDetailItem("Pressure", "${weather.main.pressure} hPa")
            }
        }
    }
}

@Composable
fun WeatherDetailItem(label: String, value: String) {
    Column {
        Text(text = label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.outline)
        Text(text = value, style = MaterialTheme.typography.bodyLarge)
    }
}
