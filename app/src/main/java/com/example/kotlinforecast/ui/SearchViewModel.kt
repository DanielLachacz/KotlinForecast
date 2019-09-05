package com.example.kotlinforecast.ui

import androidx.lifecycle.ViewModel
import com.example.kotlinforecast.data.model.city.City
import com.example.kotlinforecast.data.repository.SearchRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel(
    private val cityRepository: SearchRepository
) : ViewModel() {

    fun addCity(city: City) {
        CoroutineScope(Dispatchers.IO).launch {
            cityRepository.insert(city)
        }
    }

    fun deleteCity(city: City) {
        CoroutineScope(Dispatchers.IO).launch {
            cityRepository.delete(city)
        }
    }

    fun getAllCities() = cityRepository.getAllCities()
}