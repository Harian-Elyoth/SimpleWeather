package com.example.simpleweather.data.repository

import android.content.Context
import com.example.simpleweather.data.api.NetworkModule
import com.example.simpleweather.data.api.WeatherUtils
import com.example.simpleweather.data.database.WeatherDatabase
import com.example.simpleweather.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class WeatherRepository(private val context: Context) {
    
    private val weatherApi = NetworkModule.weatherApi
    private val cityDao = WeatherDatabase.getDatabase(context).cityDao()
    private val weatherDataDao = WeatherDatabase.getDatabase(context).weatherDataDao()
    
    // Cache duration: 30 minutes
    private val CACHE_DURATION = 30 * 60 * 1000L
    
    // City management
    suspend fun addCity(cityName: String, country: String? = null, lat: Double? = null, lon: Double? = null) {
        withContext(Dispatchers.IO) {
            val city = CityEntity(
                cityName = cityName,
                country = country,
                latitude = lat,
                longitude = lon
            )
            cityDao.insertCity(city)
        }
    }
    
    suspend fun removeCity(cityName: String) {
        withContext(Dispatchers.IO) {
            cityDao.deleteCityByName(cityName)
            weatherDataDao.deleteWeatherDataForCity(cityName)
        }
    }
    
    fun getAllFavoriteCities(): Flow<List<CityEntity>> {
        return cityDao.getAllFavoriteCities()
    }
    
    suspend fun isCityFavorite(cityName: String): Boolean {
        return withContext(Dispatchers.IO) {
            cityDao.isCityExists(cityName) > 0
        }
    }
    
    // Weather data management
    suspend fun getCurrentWeather(city: String): Result<WeatherCard> {
        return withContext(Dispatchers.IO) {
            try {
                // First check if we have fresh cached data
                val cachedData = weatherDataDao.getWeatherDataForCity(city)
                if (cachedData != null && !isDataStale(cachedData.lastUpdated)) {
                    return@withContext Result.success(mapWeatherDataEntityToWeatherCard(cachedData))
                }
                
                // Fetch fresh data from API
                val response = weatherApi.getCurrentWeather(
                    city = city,
                    appid = WeatherUtils.getApiKey(context),
                    units = "metric"
                )
                
                val weatherCard = mapWeatherResponseToWeatherCard(response)
                
                // Cache the new data
                val weatherDataEntity = mapWeatherCardToWeatherDataEntity(weatherCard)
                weatherDataDao.insertWeatherData(weatherDataEntity)
                
                Result.success(weatherCard)
            } catch (e: Exception) {
                // If API fails, try to return cached data even if stale
                val cachedData = weatherDataDao.getWeatherDataForCity(city)
                if (cachedData != null) {
                    Result.success(mapWeatherDataEntityToWeatherCard(cachedData))
                } else {
                    Result.failure(e)
                }
            }
        }
    }
    
    suspend fun getCurrentWeatherByCoordinates(lat: Double, lon: Double): Result<WeatherCard> {
        return withContext(Dispatchers.IO) {
            try {
                val response = weatherApi.getCurrentWeatherByCoordinates(
                    lat = lat,
                    lon = lon,
                    appid = WeatherUtils.getApiKey(context),
                    units = "metric"
                )
                
                val weatherCard = mapWeatherResponseToWeatherCard(response)
                
                // Cache the data
                val weatherDataEntity = mapWeatherCardToWeatherDataEntity(weatherCard)
                weatherDataDao.insertWeatherData(weatherDataEntity)
                
                Result.success(weatherCard)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    suspend fun getForecast(city: String): Result<List<WeatherCard>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = weatherApi.getForecast(
                    city = city,
                    appid = WeatherUtils.getApiKey(context),
                    units = "metric"
                )
                
                val weatherCards = response.list.map { forecastItem ->
                    mapForecastItemToWeatherCard(forecastItem, response.city.name)
                }
                
                Result.success(weatherCards)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    fun getWeatherDataForFavoriteCities(): Flow<List<WeatherCard>> {
        return weatherDataDao.getWeatherDataForFavoriteCities().map { entities: List<WeatherDataEntity> ->
            entities.map { entity: WeatherDataEntity -> mapWeatherDataEntityToWeatherCard(entity) }
        }
    }
    
    suspend fun refreshAllWeatherData() {
        withContext(Dispatchers.IO) {
            val cities = cityDao.getAllFavoriteCities().first()
            cities.forEach { city ->
                try {
                    getCurrentWeather(city.cityName)
                } catch (e: Exception) {
                    // Log error but continue with other cities
                }
            }
        }
    }
    
    suspend fun clearOldCache() {
        withContext(Dispatchers.IO) {
            val cutoffTime = System.currentTimeMillis() - CACHE_DURATION
            weatherDataDao.deleteOldWeatherData(cutoffTime)
        }
    }
    
    private fun isDataStale(timestamp: Long): Boolean {
        return System.currentTimeMillis() - timestamp > CACHE_DURATION
    }
    
    private fun mapWeatherResponseToWeatherCard(response: WeatherResponse): WeatherCard {
        val dateFormat = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
        val dateTime = dateFormat.format(Date(response.dt * 1000))
        
        return WeatherCard(
            city = response.name,
            dateTime = dateTime,
            temperature = response.main.temp,
            weatherIcon = response.weather.firstOrNull()?.icon ?: "",
            weatherDescription = response.weather.firstOrNull()?.description ?: "",
            humidity = response.main.humidity,
            windSpeed = response.wind.speed,
            pressure = response.main.pressure
        )
    }
    
    private fun mapForecastItemToWeatherCard(forecastItem: ForecastItem, cityName: String): WeatherCard {
        val dateFormat = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
        val dateTime = dateFormat.format(Date(forecastItem.dt * 1000))
        
        return WeatherCard(
            city = cityName,
            dateTime = dateTime,
            temperature = forecastItem.main.temp,
            weatherIcon = forecastItem.weather.firstOrNull()?.icon ?: "",
            weatherDescription = forecastItem.weather.firstOrNull()?.description ?: "",
            humidity = forecastItem.main.humidity,
            windSpeed = forecastItem.wind.speed,
            pressure = forecastItem.main.pressure
        )
    }
    
    private fun mapWeatherCardToWeatherDataEntity(weatherCard: WeatherCard): WeatherDataEntity {
        return WeatherDataEntity(
            cityName = weatherCard.city,
            temperature = weatherCard.temperature,
            weatherIcon = weatherCard.weatherIcon,
            weatherDescription = weatherCard.weatherDescription,
            humidity = weatherCard.humidity,
            windSpeed = weatherCard.windSpeed,
            pressure = weatherCard.pressure,
            timestamp = System.currentTimeMillis(),
            lastUpdated = System.currentTimeMillis()
        )
    }
    
    private fun mapWeatherDataEntityToWeatherCard(entity: WeatherDataEntity): WeatherCard {
        val dateFormat = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
        val dateTime = dateFormat.format(Date(entity.timestamp))
        
        return WeatherCard(
            city = entity.cityName,
            dateTime = dateTime,
            temperature = entity.temperature,
            weatherIcon = entity.weatherIcon,
            weatherDescription = entity.weatherDescription,
            humidity = entity.humidity,
            windSpeed = entity.windSpeed,
            pressure = entity.pressure
        )
    }
}
