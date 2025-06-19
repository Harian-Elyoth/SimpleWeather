package com.example.simpleweather.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.simpleweather.data.dao.CityDao
import com.example.simpleweather.data.dao.WeatherDataDao
import com.example.simpleweather.data.model.CityEntity
import com.example.simpleweather.data.model.WeatherDataEntity

@Database(
    entities = [CityEntity::class, WeatherDataEntity::class],
    version = 1,
    exportSchema = false
)
abstract class WeatherDatabase : RoomDatabase() {
    
    abstract fun cityDao(): CityDao
    abstract fun weatherDataDao(): WeatherDataDao
    
    companion object {
        @Volatile
        private var INSTANCE: WeatherDatabase? = null
        
        fun getDatabase(context: Context): WeatherDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WeatherDatabase::class.java,
                    "weather_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
} 