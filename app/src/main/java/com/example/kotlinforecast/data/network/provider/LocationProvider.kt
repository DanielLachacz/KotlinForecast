package com.example.kotlinforecast.data.network.provider

import com.example.kotlinforecast.data.model.currentWeather.CurrentWeather

interface LocationProvider {

    suspend fun getCoordinates(): Triple<String, String, String>

}