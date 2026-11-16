package com.example.weatherapp

fun getWeatherIcon(iconCode: String): Int {
    return when (iconCode) {
        "01d" -> R.drawable.clearday
        "01n" -> R.drawable.clearnight

        "02d" -> R.drawable.partlycloudyday
        "02n" -> R.drawable.clearnight

        "03d", "03n",
        "04d", "04n" -> R.drawable.cloudy

        "09d", "09n",
        "10d", "10n",
        "11d", "11n" -> R.drawable.rain

        else -> R.drawable.cloudy
    }
}

