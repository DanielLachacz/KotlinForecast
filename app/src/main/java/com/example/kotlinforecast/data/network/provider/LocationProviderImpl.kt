package com.example.kotlinforecast.data.network.provider

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.kotlinforecast.data.model.currentWeather.CurrentWeather
import com.example.kotlinforecast.internal.LocationPermissionNotGrantedException
import com.example.kotlinforecast.internal.asDeferred
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.Deferred
import java.util.jar.Manifest
import kotlin.math.abs

class LocationProviderImpl(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    context: Context
) : LocationProvider {


    private val appContext = context.applicationContext

    override suspend fun getCoordinates(): Triple<String, String, String> {
        val deviceLocation = getLastDeviceLocation().await()

       return Triple(deviceLocation?.latitude.toString(),deviceLocation?.longitude.toString(),"metric")
    }

    @SuppressLint("MissingPermission")
    private fun getLastDeviceLocation(): Deferred<Location?> {
        return if (hasLocationPermission())
            fusedLocationProviderClient.lastLocation.asDeferred()
        else
            throw LocationPermissionNotGrantedException()
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(appContext,
            android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }
}