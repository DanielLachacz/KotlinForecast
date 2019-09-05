package com.example.kotlinforecast.data.model.forecastWeather

import androidx.room.Embedded
import com.google.gson.annotations.SerializedName
import kotlin.collections.List


data class List(

	@SerializedName("dt")
	val dt : String,
	@Embedded(prefix = "main_")
	val main : Main,
	val weather : List<Weather>? = ArrayList(),
	@Embedded(prefix = "clouds_")
	val clouds : Clouds,
	@Embedded(prefix = "wind_")
	val wind : Wind,
	@Embedded(prefix = "sys_")
	val sys : Sys,
	@SerializedName("dt_txt")
	val dt_txt : String
)