package com.example.kotlinforecast.internal

import androidx.room.TypeConverter
import com.example.kotlinforecast.data.model.currentWeather.Weather
import com.example.kotlinforecast.data.model.forecastWeatherCity.ForecastCity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    val gson = Gson()
    val gson2 = Gson()
    val gson3 = Gson()
    val gson4 = Gson()

    @TypeConverter
    fun arrayListToJson(value: List<Weather>?): String? {
        return if (value == null) null else gson.toJson(value)
    }

    @TypeConverter
    fun jsonToArrayList(value: String?): List<Weather>? {
        return if (value == null) null else gson.fromJson(value, object : TypeToken<List<Weather>?>() {}.type)
    }

    @TypeConverter
    fun arrayListToJson2(value: List<com.example.kotlinforecast.data.model.forecastWeather.List>?): String? {
        return if (value == null) null else gson2.toJson(value)
    }

    @TypeConverter
    fun jsonToArrayList2(value: String?): List<com.example.kotlinforecast.data.model.forecastWeather.List>? {
        return if (value == null) null else gson2.fromJson(value, object :
            TypeToken<List<com.example.kotlinforecast.data.model.forecastWeather.List>?>() {}.type)
    }

    @TypeConverter
    fun arrayListToJson3(value: List<com.example.kotlinforecast.data.model.currentWeatherCity.Weather>?): String? {
        return if (value == null) null else gson3.toJson(value)
    }

    @TypeConverter
    fun jsonToArrayList3(value: String?): List<com.example.kotlinforecast.data.model.currentWeatherCity.Weather>? {
        return if (value == null) null else gson3.fromJson(value, object :
            TypeToken<List<com.example.kotlinforecast.data.model.currentWeatherCity.Weather>?>() {}.type)
    }

    @TypeConverter
    fun arrayListToJson4(value: List<com.example.kotlinforecast.data.model.forecastWeatherCity.List>?): String? {
        return if (value == null) null else gson4.toJson(value)
    }

    @TypeConverter
    fun jsonToArrayList4(value: String?): List<com.example.kotlinforecast.data.model.forecastWeatherCity.List>? {
        return if (value == null) null else gson4.fromJson(value, object :
        TypeToken<List<com.example.kotlinforecast.data.model.forecastWeatherCity.List>?>() {}.type)
    }
}