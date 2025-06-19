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

class WeatherCardFragment : Fragment() {
    private var _binding: FragmentWeatherCardBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: WeatherCardViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherCardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.weatherCard.collect { weatherCard ->
                weatherCard?.let { updateUI(it) }
            }
        }
    }

    private fun updateUI(weatherCard: WeatherCard) {
        binding.apply {
            // Set city name
            cityName.text = weatherCard.city
            
            // Set date and time
            dateTimeText.text = weatherCard.dateTime
            
            // Set temperature with °C
            temperatureText.text = "${weatherCard.temperature.toInt()}°C"
            
            // Set weather description
            weatherDescription.text = weatherCard.weatherDescription
            
            // Set weather icon (using a placeholder for now)
            weatherIcon.setImageResource(android.R.drawable.ic_menu_gallery)
            
            // Set weather details
            humidityText.text = "Humidity: ${weatherCard.humidity}%"
            windSpeedText.text = "Wind: ${weatherCard.windSpeed} km/h"
            pressureText.text = "Pressure: ${weatherCard.pressure} hPa"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
