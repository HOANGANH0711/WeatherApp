package com.example.weatherapp.network

import com.example.weatherapp.model.ForecastResponse
import com.example.weatherapp.model.WeatherResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class WeatherApi{
    private val client = HttpClient(OkHttp){
        install(ContentNegotiation){
            json(Json{
                ignoreUnknownKeys = true //Cấu hình này giúp bỏ qua các trường JSON không có trong data class, tránh lỗi giải mã.
            })
        }
    }

    suspend fun getWeather(
        city: String,
        apiKey:String
    ): WeatherResponse{
        return client.get(
            "https://api.openweathermap.org/data/2.5/weather"
        ){
            parameter("q", city)
            parameter("appid",apiKey)
            parameter("units","metric")
        }.body()
    }

    suspend fun getForecast(
        city: String,
        apiKey: String
    ): ForecastResponse{
        return client.get(
            "https://api.openweathermap.org/data/2.5/forecast"
        ){
            parameter("q", city)
            parameter("appid", apiKey)
            parameter("units", "metric")
            parameter("cnt", 8)
        }.body()
    }
}