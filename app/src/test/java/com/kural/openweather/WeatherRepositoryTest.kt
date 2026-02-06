package com.kural.openweather

import com.kural.openweather.networking.WeatherRepository
import com.kural.openweather.networking.WeatherResponse
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

    @Test
    fun `getCurrentWeather appends US suffix when not present`() = runTest {
        val city = "New York"
        val apiKey = "test_key"
        val mockResponse = mockWeatherResponse("New York")
        
        `when`(weatherService.getCurrentWeather(eq("New York,US"), eq(apiKey), any())).thenReturn(mockResponse)

        val result = repository.getCurrentWeather(city, apiKey)

        assertEquals("New York", result.name)
    }

    @Test
    fun `getCurrentWeather does not append US suffix when already present`() = runTest {
        val city = "Miami,US"
        val apiKey = "test_key"
        val mockResponse = mockWeatherResponse("Miami")

        `when`(weatherService.getCurrentWeather(eq("Miami,US"), eq(apiKey), any())).thenReturn(mockResponse)

        val result = repository.getCurrentWeather(city, apiKey)

        assertEquals("Miami", result.name)
    }

    private fun mockWeatherResponse(name: String): WeatherResponse {
        return WeatherResponse(
            main = com.kural.openweather.networking.Main(0.0, 0.0, 0.0, 0.0, 0, 0),
            name = name,
            weather = emptyList(),
            wind = com.kural.openweather.networking.Wind(0.0, 0),
            sys = com.kural.openweather.networking.Sys("US")
        )
    }
}
