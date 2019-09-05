package com.example.kotlinforecast.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.kotlinforecast.data.model.forecastWeather.FORECAST_ID
import com.example.kotlinforecast.data.model.forecastWeather.Forecast

@Dao
interface ForecastDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun update(forecast: Forecast)

    @Query("SELECT * FROM forecast WHERE idKey = $FORECAST_ID")
    fun getForecast(): LiveData<Forecast>
}