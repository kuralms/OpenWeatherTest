package com.kural.openweather.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kural.openweather.networking.WeatherRepository
import kotlinx.coroutines.launch


class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {

    private val _uiState = mutableStateOf<WeatherUiState>(WeatherUiState.Idle)
    val uiState: State<WeatherUiState> = _uiState

    fun fetchWeather(city: String, apiKey: String) {
        if (city.isBlank()) return

        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading
            try {
                val result = repository.getCurrentWeather(city, apiKey)
                _uiState.value = WeatherUiState.Success(result)
            } catch (e: Exception) {
                when(e.localizedMessage) {
                    e.message -> _uiState.value = WeatherUiState.Error(e.message?: "Not found")
                    else -> _uiState.value = WeatherUiState.Error(e.localizedMessage ?: "Failure")
                }
            }
        }
    }
}
