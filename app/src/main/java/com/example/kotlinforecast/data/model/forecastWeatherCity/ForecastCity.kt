package com.example.kotlinforecast.data.model.forecastWeatherCity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

const val FORECAST_CITY_ID = 0

@Entity(tableName = "forecast_city")
data class ForecastCity (

	@SerializedName("cod")
	val cod : Int,
	@SerializedName("message")
	val message : Double,
	@SerializedName("cnt")
	val cnt : Int,
	val list : kotlin.collections.List<List>? = ArrayList(),
	@Embedded(prefix = "city_")
	val city : City
) {
	@PrimaryKey(autoGenerate = false)
	var idKey: Int = FORECAST_CITY_ID
}