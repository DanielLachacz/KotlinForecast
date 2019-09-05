package com.example.kotlinforecast.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.kotlinforecast.data.model.currentWeather.CurrentWeather
import com.example.kotlinforecast.data.model.currentWeatherCity.CurrentWeatherCity
import com.example.kotlinforecast.data.model.forecastWeather.Forecast
import com.example.kotlinforecast.data.model.forecastWeatherCity.ForecastCity
import com.example.kotlinforecast.internal.NoConnectivityException

class WeatherNetworkDataSourceImpl(private val apiInterface: ApiInterface) : WeatherNetworkDataSource {

    private val _downloadedCurrentWeather = MutableLiveData<CurrentWeather>()
    override val downloadedCurrentWeather: LiveData<CurrentWeather>
        get() = _downloadedCurrentWeather

    override suspend fun fetchCurrentWeather(lat: String, lon: String, units: String) {
       try {
           val fetchedCurrentWeather = apiInterface
               .getCurrentWeatherAsync(lat, lon, units)
               .await()
           _downloadedCurrentWeather.postValue(fetchedCurrentWeather)
       }
       catch (e: NoConnectivityException) {
           Log.e("CONNECTIVITY", "No Internet Connection", e)
       }
    }

    private val _downloadedForecast = MutableLiveData<Forecast>()
    override val downloadedForecast: LiveData<Forecast>
        get() = _downloadedForecast

    override suspend fun fetchForecast(lat: String, lon: String, units: String) {
        try {
            val fetchedForecast = apiInterface
                .getForecastAsync(lat, lon, units)
                .await()
            _downloadedForecast.postValue(fetchedForecast)
        }
        catch (e: NoConnectivityException) {
            Log.e("CONNECTIVITY", "No Internet Connection", e)
        }
    }

    private val _downloadedCurrentWeatherByCity = MutableLiveData<CurrentWeatherCity>()
    override val downloadedCurrentWeatherByCity: LiveData<CurrentWeatherCity>
        get() = _downloadedCurrentWeatherByCity

    override suspend fun fetchCurrentWeatherByCity(q: String, units: String) {
        try {
            val fetchedCurrentWeatherByCity = apiInterface
                .getCurrentWeatherByCityAsync(q, units)
                .await()
            _downloadedCurrentWeatherByCity.postValue(fetchedCurrentWeatherByCity)
        }
        catch (e: NoConnectivityException) {
            Log.e("CONNECTIVITY", "No Internet Connection", e)
        }
    }

    private val _downloadedForecastByCity = MutableLiveData<ForecastCity>()
    override val downloadedForecastByCity: LiveData<ForecastCity>
    get() = _downloadedForecastByCity

    override suspend fun fetchForecastByCity(q: String, units: String) {
        try {
            val fetchedForecastByCity = apiInterface
                .getForecastByCityAsync(q, units)
                .await()
            _downloadedForecastByCity.postValue(fetchedForecastByCity)
        }
        catch (e: NoConnectivityException) {
            Log.e("CONNECTIVITY", "No Internet Connection", e)
        }
    }

}