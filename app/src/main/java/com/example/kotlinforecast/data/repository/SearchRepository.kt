package com.example.kotlinforecast.data.repository

import androidx.lifecycle.LiveData
import com.example.kotlinforecast.data.model.city.City

interface SearchRepository {

    suspend fun insert(city: City)

    suspend fun delete(city: City)

    fun getAllCities(): LiveData<List<City>>
}