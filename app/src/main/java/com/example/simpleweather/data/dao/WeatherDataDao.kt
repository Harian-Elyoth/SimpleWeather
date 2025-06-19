package com.example.simpleweather.data.dao

import androidx.room.*
import com.example.simpleweather.data.model.WeatherDataEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDataDao {
    
    @Query("SELECT * FROM weather_data WHERE cityName = :cityName")
    suspend fun getWeatherDataForCity(cityName: String): WeatherDataEntity?
    
    @Query("SELECT * FROM weather_data WHERE cityName IN (SELECT cityName FROM cities WHERE isFavorite = 1)")
    fun getWeatherDataForFavoriteCities(): Flow<List<WeatherDataEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeatherData(weatherData: WeatherDataEntity)
    
    @Delete
    suspend fun deleteWeatherData(weatherData: WeatherDataEntity)
    
    @Query("DELETE FROM weather_data WHERE cityName = :cityName")
    suspend fun deleteWeatherDataForCity(cityName: String)
    
    @Query("DELETE FROM weather_data WHERE lastUpdated < :timestamp")
    suspend fun deleteOldWeatherData(timestamp: Long)
    
    @Query("SELECT * FROM weather_data WHERE lastUpdated < :timestamp")
    suspend fun getStaleWeatherData(timestamp: Long): List<WeatherDataEntity>
} 