package com.example.kotlinforecast.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinforecast.data.repository.WeatherRepository

class CityViewModelFactory(
    private val weatherRepository: WeatherRepository
): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CityViewModel(weatherRepository) as T
    }
}