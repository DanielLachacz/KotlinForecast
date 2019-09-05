package com.example.kotlinforecast.data.network

import com.example.kotlinforecast.data.model.currentWeather.CurrentWeather
import com.example.kotlinforecast.data.model.currentWeatherCity.CurrentWeatherCity
import com.example.kotlinforecast.data.model.forecastWeather.Forecast
import com.example.kotlinforecast.data.model.forecastWeatherCity.ForecastCity
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val API_KEY = "ab866fa1c88a2e6a34deb876093bedd7"

interface ApiInterface {

    @GET("weather")
    fun getCurrentWeatherAsync(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("units") units: String
    ): Deferred<CurrentWeather>

    @GET("weather")
    fun getCurrentWeatherByCityAsync(
        @Query("q") city: String,
        @Query("units") units: String
    ): Deferred<CurrentWeatherCity>

    @GET("forecast")
    fun getForecastAsync(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("units") units: String
    ): Deferred<Forecast>

    @GET("forecast")
    fun getForecastByCityAsync(
        @Query("q") city: String,
        @Query("units") units: String
    ): Deferred<ForecastCity>

    companion object {
        operator fun invoke(connectivityInterceptor: ConnectivityInterceptor): ApiInterface {
            val requestInterceptor = Interceptor { chain ->
                val url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("APPID", API_KEY)
                    .build()
                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()

                return@Interceptor chain.proceed(request)
            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .addInterceptor(connectivityInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("http://api.openweathermap.org/data/2.5/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiInterface::class.java)
        }
    }
}