package com.example.simpleweather.data.mapper

import com.example.simpleweather.data.model.WeatherCard
import com.example.simpleweather.data.model.WeatherResponse
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object WeatherMapper {
    fun toWeatherCard(response: WeatherResponse): WeatherCard {
        return WeatherCard(
            city = response.name,
            dateTime = formatDateTime(response.dt),
            temperature = response.main.temp,
            weatherIcon = response.weather.firstOrNull()?.icon ?: "",
            weatherDescription = response.weather.firstOrNull()?.description ?: "",
            humidity = response.main.humidity,
            windSpeed = response.wind.speed,
            pressure = response.main.pressure
        )
    }

    private fun formatDateTime(timestamp: Long): String {
        val sdf = SimpleDateFormat("EEEE, HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp * 1000))
    }
}
