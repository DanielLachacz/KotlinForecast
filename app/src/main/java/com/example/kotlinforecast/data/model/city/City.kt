package com.example.kotlinforecast.data.model.city

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "city")
data class City (

    @PrimaryKey
    val name: String
)