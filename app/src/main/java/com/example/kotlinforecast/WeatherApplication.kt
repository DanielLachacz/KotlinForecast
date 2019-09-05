package com.example.kotlinforecast

import android.app.Application
import android.content.Context
import com.example.kotlinforecast.data.db.WeatherDatabase
import com.example.kotlinforecast.data.network.*
import com.example.kotlinforecast.data.network.provider.LocationProvider
import com.example.kotlinforecast.data.network.provider.LocationProviderImpl
import com.example.kotlinforecast.data.repository.SearchRepository
import com.example.kotlinforecast.data.repository.SearchRepositoryImpl
import com.example.kotlinforecast.data.repository.WeatherRepository
import com.example.kotlinforecast.data.repository.WeatherRepositoryImpl
import com.example.kotlinforecast.ui.CityViewModelFactory
import com.example.kotlinforecast.ui.MainViewModelFactory
import com.example.kotlinforecast.ui.SearchViewModelFactory
import com.google.android.gms.location.LocationServices
import com.jakewharton.threetenabp.AndroidThreeTen
import org.kodein.di.android.x.androidXModule
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class WeatherApplication : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@WeatherApplication))

        bind() from singleton { WeatherDatabase(instance()) }
        bind() from singleton { instance<WeatherDatabase>().currentWeatherDao() }
        bind() from singleton { instance<WeatherDatabase>().forecastDao() }
        bind() from singleton { instance<WeatherDatabase>().currentWeatherCityDao() }
        bind() from singleton { instance<WeatherDatabase>().forecastWeatherCityDao() }
        bind() from singleton { instance<WeatherDatabase>().cityDao() }
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        bind() from singleton { ApiInterface(instance()) }
        bind<WeatherNetworkDataSource>() with singleton { WeatherNetworkDataSourceImpl(instance()) }
        bind() from provider { LocationServices.getFusedLocationProviderClient(instance<Context>()) }
        bind<LocationProvider>() with singleton { LocationProviderImpl(instance(), instance()) }
        bind<WeatherRepository>() with singleton { WeatherRepositoryImpl(instance(), instance(), instance(),
            instance(), instance(), instance()) }
        bind<SearchRepository>() with singleton { SearchRepositoryImpl(instance()) }
        bind() from provider { MainViewModelFactory(instance()) }
        bind() from provider { CityViewModelFactory(instance()) }
        bind() from provider { SearchViewModelFactory(instance()) }
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }
}
