package com.example.kotlinforecast.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.kotlinforecast.data.model.city.City

@Dao
interface CityDao {

    @Insert
    suspend fun insertCity(city: City)

    @Delete
    suspend fun deleteCity(city: City)

    @Query("SELECT * FROM city")
    fun getAllCities(): LiveData<List<City>>
}