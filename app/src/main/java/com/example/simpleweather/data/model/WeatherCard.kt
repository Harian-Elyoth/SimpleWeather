package com.example.simpleweather.data.model

data class WeatherCard(
    val city: String,
    val dateTime: String,
    val temperature: Double,
    val weatherIcon: String,
    val weatherDescription: String,
    val humidity: Int,
    val windSpeed: Double,
    val pressure: Int
)