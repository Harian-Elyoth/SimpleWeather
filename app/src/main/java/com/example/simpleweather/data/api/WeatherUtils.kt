package com.example.simpleweather.data.api

import android.content.Context
import android.content.SharedPreferences

object WeatherUtils {
    
    // TODO: Replace with your actual API key from https://openweathermap.org/api
    const val API_KEY = "4757662e4476edf0c9cb720942b7a195"
    
    /**
     * Get API key from SharedPreferences or use default
     */
    fun getApiKey(context: Context): String {
        val prefs: SharedPreferences = context.getSharedPreferences("weather_prefs", Context.MODE_PRIVATE)
        return prefs.getString("api_key", API_KEY) ?: API_KEY
    }
    
    /**
     * Generate weather icon URL for OpenWeather API
     * @param iconCode The icon code from the API response (e.g., "01d", "02n")
     * @param size Icon size: "2x" for high resolution, "4x" for ultra high resolution
     * @return Complete URL for the weather icon
     */
    fun getWeatherIconUrl(iconCode: String, size: String = "2x"): String {
        return "https://openweathermap.org/img/wn/${iconCode}@${size}.png"
    }
    
    /**
     * Get weather icon URL for different sizes
     */
    fun getWeatherIconUrlSmall(iconCode: String): String = getWeatherIconUrl(iconCode, "1x")
    fun getWeatherIconUrlMedium(iconCode: String): String = getWeatherIconUrl(iconCode, "2x")
    fun getWeatherIconUrlLarge(iconCode: String): String = getWeatherIconUrl(iconCode, "4x")
    
    /**
     * Format temperature with degree symbol
     */
    fun formatTemperature(temp: Double): String {
        return "${temp.toInt()}°C"
    }
    
    /**
     * Format wind speed with units
     */
    fun formatWindSpeed(speed: Double): String {
        return "${speed} m/s"
    }
    
    /**
     * Format pressure with units
     */
    fun formatPressure(pressure: Int): String {
        return "${pressure} hPa"
    }
    
    /**
     * Format humidity with percentage
     */
    fun formatHumidity(humidity: Int): String {
        return "${humidity}%"
    }
} 