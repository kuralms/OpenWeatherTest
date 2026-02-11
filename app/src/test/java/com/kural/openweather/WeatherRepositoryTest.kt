package com.kural.openweather

import com.kural.openweather.data.Main
import com.kural.openweather.data.Sys
import com.kural.openweather.data.WeatherResponse
import com.kural.openweather.data.Wind
import com.kural.openweather.networking.WeatherRepository
import com.kural.openweather.networking.WeatherService
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.eq

class WeatherRepositoryTest {

    @Mock
    private lateinit var weatherService: WeatherService

    private lateinit var repository: WeatherRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository = WeatherRepository(weatherService)
    }
    val apiKey = BuildConfig.OPEN_WEATHER_API_KEY
    @Test
    fun `getCurrentWeather appends US suffix when not present`() = runTest {
        val city = "New York"
        val mockResponse = mockWeatherResponse("New York")
        
        `when`(weatherService.getCurrentWeather(eq("New York,US"), eq(apiKey), any())).thenReturn(mockResponse)

        val result = repository.getCurrentWeather(city, apiKey)

        assertEquals("New York", result.name)
    }

    @Test
    fun `getCurrentWeather does not append US suffix when already present`() = runTest {
        val city = "Miami,US"
        val mockResponse = mockWeatherResponse("Miami")

        `when`(weatherService.getCurrentWeather(eq("Miami,US"), eq(apiKey), any())).thenReturn(mockResponse)

        val result = repository.getCurrentWeather(city, apiKey)

        assertEquals("Miami", result.name)
    }

    private fun mockWeatherResponse(name: String): WeatherResponse {
        return WeatherResponse(
            main = Main(0.0, 0.0, 0.0, 0.0, 0, 0),
            name = name,
            weather = emptyList(),
            wind = Wind(0.0, 0),
            sys = Sys("US")
        )
    }
}
