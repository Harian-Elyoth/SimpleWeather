package com.example.simpleweather.data.api

import com.example.simpleweather.data.model.*
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    
    /**
     * Get current weather data for a city
     * @param city City name
     * @param appid API key from OpenWeather
     * @param units Units of measurement (metric, imperial, kelvin)
     * @param lang Language for weather descriptions
     */
    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("q") city: String,
        @Query("appid") appid: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "en"
    ): WeatherResponse
    
    /**
     * Get 5-day weather forecast for a city
     * @param city City name
     * @param appid API key from OpenWeather
     * @param units Units of measurement (metric, imperial, kelvin)
     * @param lang Language for weather descriptions
     */
    @GET("data/2.5/forecast")
    suspend fun getForecast(
        @Query("q") city: String,
        @Query("appid") appid: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "en"
    ): ForecastResponse
    
    /**
     * Get current weather by coordinates
     * @param lat Latitude
     * @param lon Longitude
     * @param appid API key from OpenWeather
     * @param units Units of measurement (metric, imperial, kelvin)
     * @param lang Language for weather descriptions
     */
    @GET("data/2.5/weather")
    suspend fun getCurrentWeatherByCoordinates(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appid: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "en"
    ): WeatherResponse
} 