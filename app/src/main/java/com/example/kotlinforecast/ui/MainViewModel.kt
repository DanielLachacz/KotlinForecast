package com.example.kotlinforecast.ui

import androidx.lifecycle.ViewModel
import com.example.kotlinforecast.data.repository.WeatherRepository
import com.example.kotlinforecast.internal.lazyDeferred

class MainViewModel(
    private val weatherRepository: WeatherRepository
    ) : ViewModel() {


    val currentWeatherByCoordinates by lazyDeferred {
        weatherRepository.getCurrentWeather() }

    val forecastByCoordinates by lazyDeferred {
        weatherRepository.getForecast() }

}