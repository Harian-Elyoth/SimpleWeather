package com.example.simpleweather.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.simpleweather.data.model.WeatherCard
import com.example.simpleweather.databinding.FragmentMeteoCardBinding
import com.bumptech.glide.Glide

class WeatherCardAdapter : ListAdapter<WeatherCard, WeatherCardAdapter.WeatherCardViewHolder>(WeatherCardDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherCardViewHolder {
        val binding = FragmentMeteoCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return WeatherCardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherCardViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class WeatherCardViewHolder(
        private val binding: FragmentMeteoCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(weatherCard: WeatherCard) {
            binding.apply {
                // Set city name
                cityName.text = weatherCard.city
                
                // Set date and time
                dateTimeText.text = weatherCard.dateTime
                
                // Set temperature with °C
                temperatureText.text = "${weatherCard.temperature.toInt()}°C"
                
                // Set weather description
                weatherDescription.text = weatherCard.weatherDescription
                
                // Load weather icon from OpenWeather
                val iconUrl = "https://openweathermap.org/img/wn/${weatherCard.weatherIcon}@2x.png"
                println("Adapter: Loading icon for ${weatherCard.city}: $iconUrl")
                Glide.with(weatherIcon.context)
                    .load(iconUrl)
                    .into(weatherIcon)
                
                // Set weather details
                humidityText.text = "Humidity: ${weatherCard.humidity}%"
                windSpeedText.text = "Wind: ${weatherCard.windSpeed} km/h"
                pressureText.text = "Pressure: ${weatherCard.pressure} hPa"
            }
        }
    }

    private class WeatherCardDiffCallback : DiffUtil.ItemCallback<WeatherCard>() {
        override fun areItemsTheSame(oldItem: WeatherCard, newItem: WeatherCard): Boolean {
            return oldItem.city == newItem.city && oldItem.dateTime == newItem.dateTime
        }

        override fun areContentsTheSame(oldItem: WeatherCard, newItem: WeatherCard): Boolean {
            return oldItem == newItem
        }
    }
} 