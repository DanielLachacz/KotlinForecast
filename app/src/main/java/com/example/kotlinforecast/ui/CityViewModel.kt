package com.example.kotlinforecast.ui

import androidx.databinding.*
import androidx.lifecycle.ViewModel
import com.example.kotlinforecast.data.repository.WeatherRepository
import com.example.kotlinforecast.internal.lazyDeferred

class CityViewModel(
    private val weatherRepository: WeatherRepository
): ViewModel() {

    var city = ObservableField<String>()
    private val metric: String = "metric"


    val currentWeatherByCity by lazyDeferred {
        weatherRepository.getCurrentWeatherByCity(city.get().toString(), metric)
    }

     val forecastByCity by lazyDeferred {
        weatherRepository.getForecastByCity(city.get().toString(), metric)
    }
}