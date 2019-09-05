package com.example.kotlinforecast.data.model.forecastWeather

import androidx.room.Embedded
import com.google.gson.annotations.SerializedName

data class City (

	@SerializedName("id")
	val id : Int,
	@SerializedName("name")
	val name : String,
	@Embedded(prefix = "coord_")
	val coord : Coord,
	@SerializedName("country")
	val country : String
)