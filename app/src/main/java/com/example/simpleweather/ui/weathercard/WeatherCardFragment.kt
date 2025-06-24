package com.example.simpleweather.ui.weathercard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.simpleweather.databinding.FragmentWeatherCardBinding
import kotlinx.coroutines.launch
import com.example.simpleweather.data.model.WeatherCard
import com.bumptech.glide.Glide

class WeatherCardFragment : Fragment() {
    private var _binding: FragmentWeatherCardBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: WeatherCardViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        println("WeatherCardFragment: onCreateView called")
        _binding = FragmentWeatherCardBinding.inflate(inflater, container, false)
        println("WeatherCardFragment: Binding inflated successfully")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        println("WeatherCardFragment: onViewCreated called")
        observeViewModel()
    }

    private fun observeViewModel() {
        println("WeatherCardFragment: observeViewModel called")
        viewLifecycleOwner.lifecycleScope.launch {
            println("WeatherCardFragment: Starting to observe weatherCard flow")
            viewModel.weatherCard.collect { weatherCard ->
                println("WeatherCardFragment: Received weatherCard: $weatherCard")
                weatherCard?.let { 
                    println("WeatherCardFragment: WeatherCard is not null, calling updateUI")
                    updateUI(it) 
                } ?: println("WeatherCardFragment: WeatherCard is null")
            }
        }
    }

    private fun updateUI(weatherCard: WeatherCard) {
        println("updateUI called for city: ${weatherCard.city}, icon: ${weatherCard.weatherIcon}")
        binding.apply {
            // Set city name
            cityName.text = weatherCard.city
            
            // Set date and time
            dateTimeText.text = weatherCard.dateTime
            
            // Set temperature with °C
            temperatureText.text = "${weatherCard.temperature.toInt()}°C"
            
            // Set weather description
            weatherDescription.text = weatherCard.weatherDescription
            
            // Log and set weather icon
            val iconUrl = "https://openweathermap.org/img/wn/${weatherCard.weatherIcon}@2x.png"
            println("Loading icon: $iconUrl")
            Glide.with(weatherIcon.context)
                .load(iconUrl)
                .into(weatherIcon)
            
            // Set weather details
            humidityText.text = "Humidity: ${weatherCard.humidity}%"
            windSpeedText.text = "Wind: ${weatherCard.windSpeed} km/h"
            pressureText.text = "Pressure: ${weatherCard.pressure} hPa"
        }
        println("WeatherCardFragment: updateUI completed")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        println("WeatherCardFragment: onDestroyView called")
        _binding = null
    }
}
