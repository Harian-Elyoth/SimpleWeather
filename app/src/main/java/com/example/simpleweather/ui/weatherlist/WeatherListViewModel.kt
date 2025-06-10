package com.example.simpleweather.ui.weatherlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simpleweather.data.model.WeatherCard
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WeatherListViewModel : ViewModel() {
    private val _weatherCards = MutableStateFlow<List<WeatherCard>>(emptyList())
    val weatherCards: StateFlow<List<WeatherCard>> = _weatherCards.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun addWeatherCard(weatherCard: WeatherCard) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val currentList = _weatherCards.value.toMutableList()
                currentList.add(weatherCard)
                _weatherCards.value = currentList
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun removeWeatherCard(city: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val currentList = _weatherCards.value.toMutableList()
                currentList.removeIf { it.city == city }
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

    // Helper function to format date
    fun formatDateTime(timestamp: Long): String {
        val sdf = SimpleDateFormat("EEEE, HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp * 1000))
    }
} 