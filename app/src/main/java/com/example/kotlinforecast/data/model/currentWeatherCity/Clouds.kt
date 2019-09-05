package com.example.kotlinforecast.data.model.currentWeatherCity

import com.google.gson.annotations.SerializedName


data class Clouds (

	@SerializedName("all")
	val all : Int
)