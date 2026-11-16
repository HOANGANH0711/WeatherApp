package com.example.weatherapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.network.WeatherApi
import com.example.weatherapp.ui.theme.WeatherAppTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import com.example.weatherapp.model.WeatherResponse
import androidx.compose.foundation.Image
import com.example.weatherapp.model.ForecastResponse

private val cardColor = Color.White.copy(alpha = 0.14f)
private const val REFRESH_INTERVAL_MILLIS = 15 * 60 * 1000L
 @Composable
 fun WeatherDashboard(){
     val gradient = Brush.verticalGradient(
         colors = listOf(
             Color(0xFF172D68),
             Color(0xFF4169B2),
             Color(0xFF789CDF)
         )
     )
     val weatherApi = remember { WeatherApi() }

     var cityName by remember{
         mutableStateOf("Ho Chi Minh City")
     }
     var temperature by remember{
         mutableStateOf<Double?>(null)
     }
     var minTemperature by remember { mutableStateOf<Double?>(null) }
     var maxTemperature by remember { mutableStateOf<Double?>(null) }
     var description by remember{
         mutableStateOf("")
     }

     var isLoading by remember{
         mutableStateOf(true)
     }
     var errorMessage by remember{
         mutableStateOf<String?>(null)
     }
     var humidity by remember{
         mutableStateOf(0)
     }
     var windSpeed by remember{
         mutableStateOf(0.0)
     }
     var feelsLike by remember{
         mutableStateOf(0.0)
     }
     var weatherIcon by remember{
         mutableStateOf("")
     }
     val otherCities = remember{
         listOf(
             "Hanoi",
             "Da Nang",
             "Da Lat"
         )
     }
     var otherCitiesWeather by remember{
         mutableStateOf<Map<String, WeatherResponse>>(emptyMap())
     }
     var forecastData by remember{
         mutableStateOf<ForecastResponse?>(null)
     }
     var forecastError by remember { mutableStateOf<String?>(null) }
     LaunchedEffect(Unit) {
         while(true){
             isLoading = temperature ==null
             errorMessage = null

             try{
                 val response = weatherApi.getWeather(
                     city = "Ho Chi Minh City",
                     apiKey = BuildConfig.OPENWEATHER_API_KEY
                 )

                 cityName = response.name
                 temperature = response.main.temp
                 minTemperature = response.main.temp_min
                 maxTemperature = response.main.temp_max
                 description = response.weather.firstOrNull()?.description
                     ?: "Không xác định"
                 humidity = response.main.humidity
                 windSpeed = response.wind.speed
                 feelsLike = response.main.feels_like
                 weatherIcon = response.weather.firstOrNull()?.icon ?: ""
             }catch(e: CancellationException){
                 throw e
             }catch(e: Exception){
                 errorMessage = e.message ?: "Không thể tải dữ liệu thời tiết"
             }finally{
                 isLoading = false
             }

             delay(REFRESH_INTERVAL_MILLIS)
         }
     }
     LaunchedEffect(Unit) {
         while(true){
             val results = mutableMapOf<String, WeatherResponse>()
             for(city in otherCities){
                    try{
                        val response = weatherApi.getWeather(
                            city = city,
                            apiKey = BuildConfig.OPENWEATHER_API_KEY
                        )

                        results[city] = response
                        android.util.Log.d(
                            "WeatherCities",
                            "$city: ${response.main.temp}°C - " +
                                    "${response.weather.firstOrNull()?.description}"
                        )
                    }catch(e: CancellationException){
                        throw e
                    }catch(e: Exception){
                        android.util.Log.e(
                            "WeatherCities",
                            "Không thể tải thời tiết: $city",
                            e
                        )
                    }
             }
             otherCitiesWeather = otherCitiesWeather + results

             delay(REFRESH_INTERVAL_MILLIS)
         }
     }
     LaunchedEffect(Unit) {
         while (true) {
             forecastError = null
             try {
                 forecastData = weatherApi.getForecast(
                     city = "Ho Chi Minh City",
                     apiKey = BuildConfig.OPENWEATHER_API_KEY
                 )
                 android.util.Log.d(
                     "ForecastDebug",
                     "Số mốc dự báo: ${forecastData?.list?.size}"
                 )
             } catch (e: CancellationException) {
                 throw e
             } catch (e: Exception) {
                 forecastError = "Không thể cập nhật dự báo. Sẽ thử lại sau 15 phút."
                 android.util.Log.e(
                     "ForecastDebug",
                     "Không thể tải dữ liệu dự báo",
                     e
                 )
             }
             delay(REFRESH_INTERVAL_MILLIS)
         }
     }
     Box(
         modifier = Modifier
             .fillMaxSize()
             .background(gradient)
     ){
         LazyColumn(
             modifier = Modifier.fillMaxWidth(),
             contentPadding = PaddingValues(20.dp),
             verticalArrangement = Arrangement.spacedBy(24.dp)
         ) {
             item{
                 CurrentWeatherCard(
                     city = cityName,
                     temperature = temperature,
                     description = if(isLoading){
                         "Đang cập nhật thời tiết"
                     }else{
                         description
                     },
                     icon = weatherIcon,
                     minTemperature = minTemperature,
                     maxTemperature = maxTemperature
                 )
                 errorMessage?.let {
                     Text(
                         text = "Không thể cập nhật thời tiết. Sẽ thử lại sau 15 phút.",
                         color = Color.White
                     )
                 }
             }
             item{
                 Text(
                     text = "Dự báo theo giờ",
                     color = Color.White,
                     style = MaterialTheme.typography.titleLarge
                 )
                 Spacer(Modifier.height(12.dp))
                 val forecasts = forecastData?.list.orEmpty()
                 val timezoneOffset = forecastData?.city?.timezone ?: 0
                 forecastError?.let { message ->
                     Text(text = message, color = Color.White)
                 }

                 if(forecasts.isEmpty()){
                     Text(
                         text = if (forecastError != null) "Chưa có dữ liệu dự báo"
                             else "Đang tải dự báo",
                         color = Color.White
                     )
                 }else{
                     LazyRow(
                         horizontalArrangement = Arrangement.spacedBy(12.dp)
                     ){
                         items(forecasts){forecast ->
                             val time = remember(forecast.dt,timezoneOffset){
                                 java.time.Instant
                                     .ofEpochSecond(forecast.dt + timezoneOffset)
                                     .atOffset(java.time.ZoneOffset.UTC)
                                     .format(
                                         java.time.format.DateTimeFormatter.ofPattern("HH:mm")
                                     )
                             }
                             val iconCode = forecast.weather
                                 .firstOrNull()
                                 ?.icon ?: "03d"

                             ForecastWeatherCard(
                                 time= time,
                                 icon = iconCode,
                                 temperature = "${forecast.main.temp.toInt()}°C"
                             )
                         }
                     }
                 }
             }
             item{
                 Text(
                     text = "Thành phố khác",
                     color = Color.White,
                     style = MaterialTheme.typography.titleLarge
                 )
                 Spacer(Modifier.height(12.dp))

                 LazyRow(
                     horizontalArrangement = Arrangement.spacedBy(12.dp)
                 ){
                     items(
                        otherCities
                     ){city ->
                         val weather = otherCitiesWeather[city]

                         val cityDisplayName = when(city){
                             "Hanoi" -> "Hà Nội"
                             "Da Nang" -> "Đà Nẵng"
                             "Da Lat" -> "Đà Lạt"
                             else -> city
                         }

                         val temperatureText = weather?.main?.temp?.let {
                             "${it.toInt()}°C"
                         } ?: "--°C"

                         val iconCode = weather
                             ?.weather
                             ?.firstOrNull()
                             ?.icon

                         WeatherSmaillcard(
                             title = cityDisplayName,
                             icon = iconCode ?: "",
                             value = temperatureText
                         )
                     }
                 }
             }
             item{
                 Text(
                     text = "Chi tiết thời tiết",
                     color = Color.White,
                     style = MaterialTheme.typography.titleLarge
                 )
                 Spacer(Modifier.height(12.dp))

                 Row(
                     modifier =Modifier.fillMaxWidth(),
                     horizontalArrangement = Arrangement.spacedBy(12.dp)
                 ){
                     Box(
                         modifier = Modifier.weight(1f)
                     ){
                         WeatherDetailCard(
                             title ="💧 Độ ẩm",
                             value ="${humidity}%")
                     }
                     Box(
                         modifier = Modifier.weight(1f)){
                         WeatherDetailCard(
                             title="💨 Tốc độ gió", value="${windSpeed} m/s")
                     }
                 }
                 Spacer(Modifier.height(20.dp))

                 WeatherDetailCard(
                     title = "🌡️ Nhiệt độ cảm nhận",
                     value = "${feelsLike.toInt()}°C"
                 )
             }
         }
     }
 }

@Composable
fun CurrentWeatherCard(
    city: String,
    temperature: Double?,
    description: String,
    icon: String,
    minTemperature: Double?,
    maxTemperature: Double?
){
    Column(
        modifier  = Modifier
            .fillMaxWidth()
            .padding(vertical = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = city,
            color = Color.White,
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(12.dp))

        if (icon.isNotEmpty()) {
            Image(
                painter = painterResource(
                    id = getWeatherIcon(icon)
                ),
                contentDescription = description,
                modifier = Modifier.size(120.dp)
            )
        }


        Text(
            text = temperature?.let {"${it.toInt()}°C"} ?: "--°C",
            color = Color.White,
            fontSize = 72.sp
        )
        Text(
            text = description,
            color = Color.White,
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(Modifier.height(8.dp))

        Text(
            "Cao nhất hiện tại: ${maxTemperature?.let { "${it.toInt()}°C" } ?: "--"}" +
                "  •  Thấp nhất: ${minTemperature?.let { "${it.toInt()}°C" } ?: "--"}",
            color = Color.White.copy(alpha = 0.8f)
        )

    }
}

@Composable
private fun WeatherSmaillcard(
    title:String,
    icon:String,
    value:String
){
   Card(modifier = Modifier.width(115.dp),
       shape = RoundedCornerShape(22.dp),
       colors  = CardDefaults.cardColors(
           containerColor = cardColor
       )
       ){
       Column(
           modifier = Modifier.padding(16.dp),
           horizontalAlignment = Alignment.CenterHorizontally
       ){
           Text(title,color = Color.White)
           Spacer(Modifier.height(12.dp))
           if(icon.isNotEmpty()){
               Image(
                   painter = painterResource(
                       id = getWeatherIcon(icon)
                   ),
                   contentDescription = "Thời tiết $title",
                   modifier = Modifier.size(50.dp)
               )
           }else{
               Box(
                   modifier = Modifier.size(48.dp),
                   contentAlignment = Alignment.Center
               ){
                   CircularProgressIndicator(
                       modifier = Modifier.size(24.dp),
                       strokeWidth = 2.dp,
                       color = Color.White
                   )
               }
           }
           Spacer(Modifier.height(12.dp))
           Text(
               value,
               color = Color.White,
               style = MaterialTheme.typography.titleLarge
           )
       }
   }
}

@Composable
private fun WeatherDetailCard(
    title:String,
    value:String
){
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        )
    ){
        Column(Modifier.padding(20.dp)){
            Text(
                title,
                color = Color.White.copy(alpha = 0.8f)
            )

            Spacer(Modifier.height(12.dp))

            Text(
                value,
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}
@Preview(
    showBackground = true,
    showSystemUi = true,
    device = "id:pixel_6"
)
@Composable
fun CurrentWeatherCardPreview() {

    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF172D68),
            Color(0xFF4169B2),
            Color(0xFF789CDF)
        )
    )

    WeatherAppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient),
            contentAlignment = Alignment.Center
        ) {
            WeatherSmaillcard("Hà Nội", "🌤️", "25°C")
        }
    }
}
@Composable
private fun ForecastWeatherCard(
    time: String,
    icon: String,
    temperature: String
) {
    Card(
        modifier = Modifier.width(90.dp),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.14f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = time,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(12.dp))

            Image(
                painter = painterResource(
                    id = getWeatherIcon(icon)
                ),
                contentDescription = "Dự báo thời tiết",
                modifier = Modifier.size(52.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = temperature,
                color = Color.White,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}
