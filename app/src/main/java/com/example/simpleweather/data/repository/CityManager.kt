package com.example.simpleweather.data.repository

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CityManager(private val context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences("weather_prefs", Context.MODE_PRIVATE)
    private val repository = WeatherRepository(context)
    
    companion object {
        private const val KEY_FIRST_LAUNCH = "first_launch"
        private val DEFAULT_CITIES = listOf("London", "Paris", "New York", "Tokyo", "Sydney")
    }
    
    fun initializeDefaultCities() {
        val isFirstLaunch = prefs.getBoolean(KEY_FIRST_LAUNCH, true)
        
        if (isFirstLaunch) {
            CoroutineScope(Dispatchers.IO).launch {
                DEFAULT_CITIES.forEach { city ->
                    try {
                        repository.addCity(city)
                        // Also fetch weather data for the city
                        repository.getCurrentWeather(city)
                    } catch (e: Exception) {
                        // Log error but continue with other cities
                    }
                }
                
                // Mark first launch as complete
                prefs.edit().putBoolean(KEY_FIRST_LAUNCH, false).apply()
            }
        }
    }
    
    fun resetToDefaultCities() {
        CoroutineScope(Dispatchers.IO).launch {
            // Clear all existing cities
            val existingCities = repository.getAllFavoriteCities().first()
            for (city in existingCities) {
                repository.removeCity(city.cityName)
            }
            
            // Add default cities
            for (city in DEFAULT_CITIES) {
                try {
                    repository.addCity(city)
                    repository.getCurrentWeather(city)
                } catch (e: Exception) {
                    // Log error but continue with other cities
                }
            }
        }
    }
} 