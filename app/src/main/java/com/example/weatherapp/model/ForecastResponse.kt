package com.example.weatherapp.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForecastResponse(
    val list: List<ForecastItem>,
    val city: ForecastCity
)

@Serializable
data class ForecastItem(
    val dt: Long,
    val main: ForecastMain,
    val weather: List<WeatherInfo>,
    @SerialName("dt_txt")
    val dateTime:String
)

@Serializable
data class ForecastMain(
    val temp: Double
)
@Serializable
data class ForecastCity(
    val name:String,
    val timezone:Int
)