package com.example.kotlinforecast.data.model.currentWeather

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.kotlinforecast.data.model.currentWeatherCity.Weather
import com.google.gson.annotations.SerializedName

const val CURRENT_WEATHER_ID = 0

@Entity(tableName = "current_weather")
data class CurrentWeather(
    val weather: List<Weather>? = ArrayList(),
    @SerializedName("base")
    val base: String,
    @Embedded(prefix = "clouds_")
    val clouds: Clouds,
    @SerializedName("cod")
    val cod: Int,
    @Embedded(prefix = "coord_")
    val coord: Coord,
    @SerializedName("dt")
    val dt: Int,
    @SerializedName("id")
    val id: Int,
    @Embedded(prefix = "main_")
    val main: Main,
    @SerializedName("name")
    val name: String,
    @Embedded(prefix = "sys_")
    val sys: Sys,
    @SerializedName("visibility")
    val visibility: Int,
    @Embedded(prefix = "wind_")
    val wind: Wind
) {
    @PrimaryKey(autoGenerate = false)
    var idKey: Int = CURRENT_WEATHER_ID
}