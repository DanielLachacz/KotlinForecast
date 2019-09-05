package com.example.kotlinforecast.data.repository

import androidx.annotation.WorkerThread
import com.example.kotlinforecast.data.db.CityDao
import com.example.kotlinforecast.data.model.city.City

class SearchRepositoryImpl(
    private val cityDao: CityDao) : SearchRepository {

    init {
        cityDao.getAllCities()
    }

    @WorkerThread
    override suspend fun insert(city: City) {
        cityDao.insertCity(city)
    }

    @WorkerThread
    override suspend fun delete(city: City) {
        cityDao.deleteCity(city)
    }

    override fun getAllCities() = cityDao.getAllCities()

}