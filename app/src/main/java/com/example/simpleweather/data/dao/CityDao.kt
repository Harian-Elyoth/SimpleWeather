package com.example.simpleweather.data.dao

import androidx.room.*
import com.example.simpleweather.data.model.CityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {
    
    @Query("SELECT * FROM cities WHERE isFavorite = 1 ORDER BY addedAt ASC")
    fun getAllFavoriteCities(): Flow<List<CityEntity>>
    
    @Query("SELECT * FROM cities WHERE cityName = :cityName")
    suspend fun getCityByName(cityName: String): CityEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCity(city: CityEntity)
    
    @Delete
    suspend fun deleteCity(city: CityEntity)
    
    @Query("DELETE FROM cities WHERE cityName = :cityName")
    suspend fun deleteCityByName(cityName: String)
    
    @Query("SELECT COUNT(*) FROM cities WHERE cityName = :cityName")
    suspend fun isCityExists(cityName: String): Int
    
    @Query("UPDATE cities SET isFavorite = :isFavorite WHERE cityName = :cityName")
    suspend fun updateCityFavoriteStatus(cityName: String, isFavorite: Boolean)
} 