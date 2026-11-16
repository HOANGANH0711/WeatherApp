package com.example.weatherapp.model

import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponse(
    val weather: List<WeatherInfo>,
    val main: MainWeather,
    val wind: WindInfo,
    val name:String
)
@Serializable
data class WeatherInfo(
    val main:String,
    val description:String,
    val icon:String
)
@Serializable
data class WindInfo(val speed: Double)
@Serializable
data class MainWeather(
    val temp: Double,
    val feels_like: Double,
    val humidity: Int,
    val temp_min: Double? = null,
    val temp_max: Double? = null
)
