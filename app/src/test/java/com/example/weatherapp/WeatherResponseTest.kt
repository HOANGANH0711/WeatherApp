package com.example.weatherapp

import com.example.weatherapp.model.WeatherResponse
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class WeatherResponseTest {
    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun decodesMinMaxFromApiResponse() {
        val response = decodeWeather(
            """, "temp_min": 26.4, "temp_max": 33.7"""
        )

        assertEquals(26.4, response.main.temp_min!!, 0.001)
        assertEquals(33.7, response.main.temp_max!!, 0.001)
        assertEquals(30.0, response.main.temp, 0.001)
    }

    @Test
    fun missingMinMaxRemainUnknown() {
        val response = decodeWeather("")

        assertNull(response.main.temp_min)
        assertNull(response.main.temp_max)
    }

    @Test
    fun nullMinMaxRemainUnknown() {
        val response = decodeWeather(
            """, "temp_min": null, "temp_max": null"""
        )

        assertNull(response.main.temp_min)
        assertNull(response.main.temp_max)
    }

    private fun decodeWeather(extraMainFields: String): WeatherResponse =
        json.decodeFromString<WeatherResponse>(
            """
            {
                "name": "Ho Chi Minh City",
                "weather": [],
                "wind": { "speed": 2.5 },
                "main": {
                    "temp": 30.0,
                    "feels_like": 32.0,
                    "humidity": 70,
                    "pressure": 1010
                    $extraMainFields
                }
            }
            """.trimIndent()
        )
}
