package com.example.simpleweather.model

data class WeatherCard(
    val cityName: String,
    val dateTime: String,
    val temperature: Double,
    val weatherIcon: String,
    val weatherDescription: String,
    val humidity: Int,
    val windSpeed: Double,
    val pressure: Int
) 