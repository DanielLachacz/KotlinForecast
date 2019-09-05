package com.example.kotlinforecast.data.repository

import androidx.lifecycle.LiveData
import com.example.kotlinforecast.data.db.CurrentWeatherCityDao
import com.example.kotlinforecast.data.db.CurrentWeatherDao
import com.example.kotlinforecast.data.db.ForecastCityDao
import com.example.kotlinforecast.data.db.ForecastDao
import com.example.kotlinforecast.data.model.currentWeather.CurrentWeather
import com.example.kotlinforecast.data.model.currentWeatherCity.CurrentWeatherCity
import com.example.kotlinforecast.data.model.forecastWeather.Forecast
import com.example.kotlinforecast.data.model.forecastWeatherCity.ForecastCity
import com.example.kotlinforecast.data.network.WeatherNetworkDataSource
import com.example.kotlinforecast.data.network.provider.LocationProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WeatherRepositoryImpl(
    private val currentWeatherDao: CurrentWeatherDao,
    private val forecastDao: ForecastDao,
    private val currentWeatherCityDao: CurrentWeatherCityDao,
    private val forecastCityDao: ForecastCityDao,
    private val weatherNetworkDataSource: WeatherNetworkDataSource,
    private val locationProvider: LocationProvider
) : WeatherRepository {


    init {
        weatherNetworkDataSource.apply {
            downloadedCurrentWeather.observeForever { newCurrentWeather ->
                persistFetchedCurrentWeather(newCurrentWeather)
            }
            downloadedForecast.observeForever { newForecast ->
                persistFetchedForecast(newForecast)
            }
            downloadedCurrentWeatherByCity.observeForever { newCurrentWeatherByCity ->
                persistFetchedCurrentWeatherByCity(newCurrentWeatherByCity)
            }
            downloadedForecastByCity.observeForever {newForecastByCity ->
                persistFetchedForecastByCity(newForecastByCity)
            }
        }
    }

    override suspend fun getCurrentWeather(): LiveData<CurrentWeather> {
        return withContext(Dispatchers.IO) {
            initCurrentWeatherData()
            return@withContext currentWeatherDao.getCurrentWeather()
        }
    }

    override suspend fun getForecast(): LiveData<Forecast> {
        return withContext(Dispatchers.IO) {
            initForecastData()
            return@withContext forecastDao.getForecast()
        }
    }

    override suspend fun getCurrentWeatherByCity(q: String, units: String): LiveData<CurrentWeatherCity> {
        return withContext(Dispatchers.IO) {
            initCurrentWeatherCityData(q, units)
            return@withContext currentWeatherCityDao.getCurrentWeatherCity()
        }
    }

    override suspend fun getForecastByCity(q: String, units: String): LiveData<ForecastCity> {
       return withContext(Dispatchers.IO) {
           initForecastCityData(q, units)
           return@withContext forecastCityDao.getForecastCity()
       }
    }

    private fun persistFetchedCurrentWeather(fetchedCurrentWeather: CurrentWeather) {
        GlobalScope.launch(Dispatchers.IO) {
            currentWeatherDao.update(fetchedCurrentWeather)
        }
    }

    private fun persistFetchedForecast(fetchedForecast: Forecast) {
        GlobalScope.launch(Dispatchers.IO) {
            forecastDao.update(fetchedForecast)
        }
    }

    private fun persistFetchedCurrentWeatherByCity(fetchedCurrentWeatherCity: CurrentWeatherCity) {
        GlobalScope.launch(Dispatchers.IO) {
            currentWeatherCityDao.update(fetchedCurrentWeatherCity)
        }
    }

    private fun persistFetchedForecastByCity(fetchedForecastCity: ForecastCity) {
        GlobalScope.launch(Dispatchers.IO) {
            forecastCityDao.update(fetchedForecastCity)
        }
    }

    private suspend fun initCurrentWeatherData() {
        if (isFetchCurrentNeeded(org.threeten.bp.ZonedDateTime.now().minusHours(1)))
            fetchCurrentWeather()
    }

    private suspend fun initForecastData() {
        if (isFetchForecastNeeded(org.threeten.bp.ZonedDateTime.now().minusHours(1)))
            fetchForecast()
    }

    private suspend fun initCurrentWeatherCityData(q: String, units: String) {
        if (isFetchCurrentByCityNeeded(org.threeten.bp.ZonedDateTime.now().minusHours(1)))
          fetchCurrentWeatherByCity(q, units)
    }

    private suspend fun initForecastCityData(q: String, units: String) {
        if (isFetchForecastByCityNeeded(org.threeten.bp.ZonedDateTime.now().minusHours(1)))
            fetchForecastByCity(q, units)
    }

    private suspend fun fetchCurrentWeather() {
        val coords = locationProvider.getCoordinates()
        val lat = coords.first
        val lon = coords.second
        val units = coords.third
        weatherNetworkDataSource.fetchCurrentWeather(
            lat, lon, units
        )
    }

    private suspend fun fetchForecast() {
        val coords = locationProvider.getCoordinates()
        val lat = coords.first
        val lon = coords.second
        val units = coords.third
        weatherNetworkDataSource.fetchForecast(
            lat, lon, units
        )
    }

    private suspend fun fetchCurrentWeatherByCity(q: String, units: String) {
        weatherNetworkDataSource.fetchCurrentWeatherByCity(q, units)
    }

    private suspend fun fetchForecastByCity(q: String, units: String) {
        weatherNetworkDataSource.fetchForecastByCity(q, units)
    }

    private fun isFetchCurrentNeeded(lastFetchTime: org.threeten.bp.ZonedDateTime): Boolean {
        val thirtyMinutesAgo = org.threeten.bp.ZonedDateTime.now().minusMinutes(30)
        return lastFetchTime.isBefore(thirtyMinutesAgo)
    }

    private fun isFetchForecastNeeded(lastFetchTime: org.threeten.bp.ZonedDateTime): Boolean {
        val thirtyMinutesAgo = org.threeten.bp.ZonedDateTime.now().minusMinutes(30)
        return lastFetchTime.isBefore(thirtyMinutesAgo)
    }

    private fun isFetchCurrentByCityNeeded(lastFetchTime: org.threeten.bp.ZonedDateTime): Boolean {
        val thirtyMinutesAgo = org.threeten.bp.ZonedDateTime.now().minusMinutes(60)
        return lastFetchTime.isBefore(thirtyMinutesAgo)
    }

    private fun isFetchForecastByCityNeeded(lastFetchTime: org.threeten.bp.ZonedDateTime): Boolean {
        val thirtyMinutesAgo = org.threeten.bp.ZonedDateTime.now().minusMinutes(60)
        return lastFetchTime.isBefore(thirtyMinutesAgo)
    }
}