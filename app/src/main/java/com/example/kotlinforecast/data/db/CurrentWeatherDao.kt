package com.example.kotlinforecast.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.kotlinforecast.data.model.currentWeather.CURRENT_WEATHER_ID
import com.example.kotlinforecast.data.model.currentWeather.CurrentWeather

@Dao
interface CurrentWeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun update(currentWeather: CurrentWeather)

    @Query("SELECT * FROM current_weather WHERE idKey = $CURRENT_WEATHER_ID")
    fun getCurrentWeather(): LiveData<CurrentWeather>
}