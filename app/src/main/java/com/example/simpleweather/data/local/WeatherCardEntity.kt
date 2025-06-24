package com.example.simpleweather.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.simpleweather.data.model.WeatherCard

@Entity(tableName = "weather_cards")
data class WeatherCardEntity(
    @PrimaryKey
    val city: String,
    val dateTime: String,
    val temperature: Double,
    val weatherIcon: String,
    val weatherDescription: String,
    val humidity: Int,
    val windSpeed: Double,
    val pressure: Int
) {
    fun toWeatherCard() = WeatherCard(
        city = city,
        dateTime = dateTime,
        temperature = temperature,
        weatherIcon = weatherIcon,
        weatherDescription = weatherDescription,
        humidity = humidity,
        windSpeed = windSpeed,
        pressure = pressure
    )

    companion object {
        fun fromWeatherCard(weatherCard: WeatherCard) = WeatherCardEntity(
            city = weatherCard.city,
            dateTime = weatherCard.dateTime,
            temperature = weatherCard.temperature,
            weatherIcon = weatherCard.weatherIcon,
            weatherDescription = weatherCard.weatherDescription,
            humidity = weatherCard.humidity,
            windSpeed = weatherCard.windSpeed,
            pressure = weatherCard.pressure
        )
    }
} 