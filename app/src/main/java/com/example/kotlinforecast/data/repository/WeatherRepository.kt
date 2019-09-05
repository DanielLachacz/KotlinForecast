package com.example.kotlinforecast.data.repository

import androidx.lifecycle.LiveData
import com.example.kotlinforecast.data.model.currentWeather.CurrentWeather
import com.example.kotlinforecast.data.model.currentWeatherCity.CurrentWeatherCity
import com.example.kotlinforecast.data.model.forecastWeather.Forecast
import com.example.kotlinforecast.data.model.forecastWeatherCity.ForecastCity

interface WeatherRepository {

    suspend fun getCurrentWeather(): LiveData<CurrentWeather>

    suspend fun getForecast(): LiveData<Forecast>

    suspend fun getCurrentWeatherByCity(q: String, units: String): LiveData<CurrentWeatherCity>

    suspend fun getForecastByCity(q: String, units: String): LiveData<ForecastCity>

}