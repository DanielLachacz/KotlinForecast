package com.example.kotlinforecast.data.db


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.kotlinforecast.data.model.city.City
import com.example.kotlinforecast.data.model.currentWeather.CurrentWeather
import com.example.kotlinforecast.data.model.currentWeatherCity.CurrentWeatherCity
import com.example.kotlinforecast.data.model.forecastWeather.Forecast
import com.example.kotlinforecast.data.model.forecastWeatherCity.ForecastCity

import com.example.kotlinforecast.internal.Converters

@Database(entities = [CurrentWeather::class, Forecast::class, CurrentWeatherCity::class, ForecastCity::class, City::class]
    , version = 21, exportSchema = false)
@TypeConverters(Converters::class)
abstract class WeatherDatabase : RoomDatabase() {

    abstract fun currentWeatherDao(): CurrentWeatherDao
    abstract fun forecastDao(): ForecastDao
    abstract fun currentWeatherCityDao(): CurrentWeatherCityDao
    abstract fun forecastWeatherCityDao(): ForecastCityDao
    abstract fun cityDao(): CityDao

    companion object {
        @Volatile private var instance: WeatherDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it}
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                WeatherDatabase::class.java, "weather.db")
                .fallbackToDestructiveMigration()
                .build()
    }
}