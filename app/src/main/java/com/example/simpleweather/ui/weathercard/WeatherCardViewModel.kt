package com.example.simpleweather.ui.weathercard

import androidx.lifecycle.ViewModel
import com.example.simpleweather.data.model.WeatherCard
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class WeatherCardViewModel : ViewModel() {
    private val _weatherCard = MutableStateFlow<WeatherCard?>(null)
    val weatherCard: StateFlow<WeatherCard?> = _weatherCard.asStateFlow()

    fun updateWeatherCard(weatherCard: WeatherCard) {
        _weatherCard.value = weatherCard
    }
}
