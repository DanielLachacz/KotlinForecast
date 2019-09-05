package com.example.kotlinforecast.data.model.forecastWeatherCity

import androidx.room.Embedded
import com.google.gson.annotations.SerializedName
import kotlin.collections.List

data class List (

	@SerializedName("dt")
	val dt : Int,
	@Embedded(prefix = "main_")
	val main : Main,
	val weather : List<Weather>? = ArrayList(),
//	@SerializedName("clouds") val clouds : Clouds,
//	@SerializedName("wind") val wind : Wind,
//	@SerializedName("sys") val sys : Sys,
	@SerializedName("dt_txt")
	val dt_txt : String
)