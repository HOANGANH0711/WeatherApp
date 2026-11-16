package com.example.weatherapp

import android.R.attr.text
import android.util.Log.e
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.example.weatherapp.network.WeatherApi
import com.example.weatherapp.ui.theme.WeatherAppTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.weatherapp.BuildConfig
import coil3.compose.AsyncImage
@Composable
fun WeatherScreen(){
    val weatherApi = remember{ WeatherApi() }

    var temperature by remember { mutableStateOf<Double?>(null)}
    var cityName by remember {mutableStateOf("")}
    var description by remember{mutableStateOf("")}
    var weatherIcon by remember{mutableStateOf("")}

    var isLoading by remember{mutableStateOf(true)}
    var errorMessage by remember{mutableStateOf<String?>(null)}

    LaunchedEffect(Unit) {
        while(true) {
            isLoading = temperature == null
            errorMessage = null
            try {
                val response = weatherApi.getWeather(
                    city = "Ho Chi Minh City",
                    apiKey = BuildConfig.OPENWEATHER_API_KEY
                )
                temperature = response.main.temp
                cityName = response.name
                description = response.weather.firstOrNull()?.description
                    ?: "Unknown"
                weatherIcon = response.weather.firstOrNull()?.icon ?: ""

            }catch(e: kotlinx.coroutines.CancellationException){
                throw e
            }
            catch (e: Exception) {
                errorMessage = e.message ?: "không thể tải dữ liệu thời tiết"
            } finally {
                isLoading = false
            }

            kotlinx.coroutines.delay(15*60*1000L)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding((24.dp)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Text(
            text = "Weather App",
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(24.dp))

        when{
            isLoading -> {
                CircularProgressIndicator()
                Text("Đang tải thờitiết ....")
            }
            errorMessage != null ->{
                Text(
                    text = "Lỗi: $errorMessage",
                    color = MaterialTheme.colorScheme.error
                )
            }

            else ->{
                Text(
                    text = cityName,
                    style = MaterialTheme.typography.headlineMedium
                )
                if(weatherIcon.isNotEmpty()){
                    AsyncImage(
                        model = "https://openweathermap.org/img/wn/$weatherIcon@2x.png",
                        contentDescription = "Weather Icon",
                        modifier = Modifier.size(100.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "${temperature?.toInt()}°C",
                    style = MaterialTheme.typography.displayLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}