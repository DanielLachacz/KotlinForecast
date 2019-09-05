package com.example.kotlinforecast.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.kotlinforecast.data.model.forecastWeatherCity.FORECAST_CITY_ID
import com.example.kotlinforecast.data.model.forecastWeatherCity.ForecastCity

@Dao
interface ForecastCityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun update(forecastCity: ForecastCity)

    @Query("SELECT * FROM forecast_city WHERE idKey = $FORECAST_CITY_ID")
    fun getForecastCity(): LiveData<ForecastCity>
}