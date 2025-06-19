package com.example.simpleweather.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cities")
data class CityEntity(
    @PrimaryKey
    val cityName: String,
    val country: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val addedAt: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = true
) 