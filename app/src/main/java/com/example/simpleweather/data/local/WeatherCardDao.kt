package com.example.simpleweather.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherCardDao {
    @Query("SELECT * FROM weather_cards")
    fun getAllWeatherCards(): Flow<List<WeatherCardEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeatherCard(weatherCard: WeatherCardEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllWeatherCards(weatherCards: List<WeatherCardEntity>)

    @Query("DELETE FROM weather_cards WHERE city = :city")
    suspend fun deleteWeatherCard(city: String)

    @Query("DELETE FROM weather_cards")
    suspend fun deleteAllWeatherCards()
} 