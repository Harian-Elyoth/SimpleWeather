package com.example.simpleweather.ui.weatherlist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.simpleweather.data.model.WeatherCard
import com.example.simpleweather.data.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import retrofit2.HttpException

class WeatherListViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository = WeatherRepository(application)
    
    private val _weatherCards = MutableStateFlow<List<WeatherCard>>(emptyList())
    val weatherCards: StateFlow<List<WeatherCard>> = _weatherCards.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadFavoriteCitiesWeather()
    }

    fun addCity(cityName: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            try {
                repository.addCity(cityName)
                // Load weather for the new city
                loadWeatherForCity(cityName)
            } catch (e: Exception) {
                _error.value = "Failed to add city: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun removeCity(cityName: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.removeCity(cityName)
                // Remove from current list
                val currentList = _weatherCards.value.toMutableList()
                currentList.removeIf { it.city == cityName }
                _weatherCards.value = currentList
            } catch (e: Exception) {
                _error.value = "Failed to remove city: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun refreshWeatherData() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            try {
                repository.refreshAllWeatherData()
            } catch (e: Exception) {
                _error.value = "Failed to refresh weather data: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadWeatherForCity(city: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            repository.getCurrentWeather(city)
                .onSuccess { weatherCard ->
                    addWeatherCard(weatherCard)
                }
                .onFailure { exception ->
                    var message = exception.message
                    if (exception is HttpException && exception.code() == 404) {
                        message = "City \"$city\" was not found. Please check the spelling."
                    }
                    _error.value = message ?: "Failed to load weather for $city"
                }
            
            _isLoading.value = false
        }
    }
    
    fun loadForecastForCity(city: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            repository.getForecast(city)
                .onSuccess { weatherCards ->
                    _weatherCards.value = weatherCards
                }
                .onFailure { exception ->
                    _error.value = exception.message ?: "Failed to load forecast for $city"
                }
            
            _isLoading.value = false
        }
    }
    
    private fun loadFavoriteCitiesWeather() {
        viewModelScope.launch {
            repository.getWeatherDataForFavoriteCities().collect { weatherCards ->
                _weatherCards.value = weatherCards
            }
        }
    }

    fun addWeatherCard(weatherCard: WeatherCard) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val currentList = _weatherCards.value.toMutableList()
                // Check if city already exists, if so update it
                val existingIndex = currentList.indexOfFirst { it.city == weatherCard.city }
                if (existingIndex != -1) {
                    currentList[existingIndex] = weatherCard
                } else {
                    currentList.add(weatherCard)
                }
                _weatherCards.value = currentList
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateWeatherCard(weatherCard: WeatherCard) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val currentList = _weatherCards.value.toMutableList()
                val index = currentList.indexOfFirst { it.city == weatherCard.city }
                if (index != -1) {
                    currentList[index] = weatherCard
                    _weatherCards.value = currentList
                }
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun clearError() {
        _error.value = null
    }

    // Helper function to format date
    fun formatDateTime(timestamp: Long): String {
        val sdf = SimpleDateFormat("EEEE, HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp * 1000))
    }
} 