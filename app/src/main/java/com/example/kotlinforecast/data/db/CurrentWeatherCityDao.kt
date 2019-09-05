package com.example.kotlinforecast.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.kotlinforecast.data.model.currentWeatherCity.CURRENT_WEATHER_CITY_ID
import com.example.kotlinforecast.data.model.currentWeatherCity.CurrentWeatherCity

@Dao
interface CurrentWeatherCityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun update(currentWeatherCity: CurrentWeatherCity)

    @Query("SELECT * FROM current_weather_city WHERE idKey = $CURRENT_WEATHER_CITY_ID")
    fun getCurrentWeatherCity(): LiveData<CurrentWeatherCity>
}