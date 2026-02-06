package com.kural.openweather

import com.kural.openweather.networking.Main
import com.kural.openweather.networking.Sys
import com.kural.openweather.networking.WeatherRepository
import com.kural.openweather.networking.WeatherResponse
import com.kural.openweather.networking.Wind
import com.kural.openweather.ui.viewmodel.WeatherUiState
import com.kural.openweather.ui.viewmodel.WeatherViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherViewModelTest {

    @Mock
    private lateinit var repository: WeatherRepository

    private lateinit var viewModel: WeatherViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = WeatherViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchWeather updates state to Success when repository returns data`() = runTest {
        val city = "New York"
        val apiKey = "test_key"
        val mockResponse = WeatherResponse(
            main = Main(72.0, 70.0, 65.0, 75.0, 50, 1013),
            name = city,
            weather = emptyList(),
            wind = Wind(10.0, 180),
            sys = Sys("US")
        )

        `when`(repository.getCurrentWeather(city, apiKey)).thenReturn(mockResponse)

        viewModel.fetchWeather(city, apiKey)
        
        // Assert Loading state initially (or after starting)
        assertTrue(viewModel.uiState.value is WeatherUiState.Loading)

        advanceUntilIdle()

        assertTrue(viewModel.uiState.value is WeatherUiState.Success)
        assertEquals(mockResponse, (viewModel.uiState.value as WeatherUiState.Success).weather)
    }

    @Test
    fun `fetchWeather updates state to Error when repository throws exception`() = runTest {
        val city = "Invalid City"
        val apiKey = "test_key"
        val errorMessage = "City not found"

        `when`(repository.getCurrentWeather(city, apiKey)).thenThrow(RuntimeException(errorMessage))

        viewModel.fetchWeather(city, apiKey)
        
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value is WeatherUiState.Error)
        assertEquals(errorMessage, (viewModel.uiState.value as WeatherUiState.Error).message)
    }

    @Test
    fun `fetchWeather does nothing when city is blank`() = runTest {
        viewModel.fetchWeather(" ", "api_key")
        
        assertEquals(WeatherUiState.Idle, viewModel.uiState.value)
    }
}
