package com.example.kotlinforecast.data.network

import androidx.lifecycle.LiveData
import com.example.kotlinforecast.data.model.currentWeather.CurrentWeather
import com.example.kotlinforecast.data.model.currentWeatherCity.CurrentWeatherCity
import com.example.kotlinforecast.data.model.forecastWeather.Forecast
import com.example.kotlinforecast.data.model.forecastWeatherCity.ForecastCity

interface WeatherNetworkDataSource {

    val downloadedCurrentWeather: LiveData<CurrentWeather>
    val downloadedForecast: LiveData<Forecast>
    val downloadedCurrentWeatherByCity: LiveData<CurrentWeatherCity>
    val downloadedForecastByCity: LiveData<ForecastCity>


    suspend fun fetchCurrentWeather(
        lat: String,
        lon: String,
        units: String
    )

    suspend fun fetchForecast(
        lat: String,
        lon: String,
        units: String
    )

    suspend fun fetchCurrentWeatherByCity(
        q: String,
        units: String
    )

    suspend fun fetchForecastByCity(
        q: String,
        units: String
    )



}