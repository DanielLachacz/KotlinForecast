package com.example.kotlinforecast.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinforecast.data.repository.WeatherRepository

class MainViewModelFactory(
    private val weatherRepository: WeatherRepository
): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(weatherRepository) as T
    }
}