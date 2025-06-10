package com.example.simpleweather.ui.weatherlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simpleweather.adapter.WeatherCardAdapter
import com.example.simpleweather.data.model.WeatherCard
import com.example.simpleweather.databinding.FragmentWeatherListBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WeatherListFragment : Fragment() {
    private var _binding: FragmentWeatherListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: WeatherCardAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        loadMockData()
    }

    private fun setupRecyclerView() {
        adapter = WeatherCardAdapter()
        binding.weatherRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@WeatherListFragment.adapter
        }
    }

    private fun loadMockData() {
        val mockWeatherCards = listOf(
            createMockWeatherCard("Paris", 22.5, "Sunny", 65, 5.2, 1013),
            createMockWeatherCard("London", 18.3, "Cloudy", 70, 4.1, 1012),
            createMockWeatherCard("New York", 25.7, "Clear", 60, 3.8, 1014),
            createMockWeatherCard("Tokyo", 28.2, "Rainy", 80, 6.5, 1011),
            createMockWeatherCard("Sydney", 30.1, "Sunny", 55, 7.2, 1015)
        )
        adapter.submitList(mockWeatherCards)
    }

    private fun createMockWeatherCard(
        city: String,
        temperature: Double,
        description: String,
        humidity: Int,
        windSpeed: Double,
        pressure: Int
    ): WeatherCard {
        val currentTime = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("EEEE, HH:mm", Locale.getDefault())
        return WeatherCard(
            city = city,
            dateTime = dateFormat.format(Date(currentTime)),
            temperature = temperature,
            weatherIcon = "01d", // Using a default icon
            weatherDescription = description,
            humidity = humidity,
            windSpeed = windSpeed,
            pressure = pressure
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 