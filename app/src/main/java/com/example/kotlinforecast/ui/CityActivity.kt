package com.example.kotlinforecast.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kotlinforecast.R
import com.example.kotlinforecast.adapter.ForecastCityAdapter
import com.example.kotlinforecast.data.model.forecastWeatherCity.List
import com.example.kotlinforecast.databinding.ActivityCityBinding
import com.example.kotlinforecast.ui.base.Scoped
import kotlinx.android.synthetic.main.activity_city.*
import kotlinx.coroutines.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import java.math.RoundingMode


class CityActivity : Scoped(), KodeinAware {
    override val kodein by closestKodein()

    private val cityViewModelFactory: CityViewModelFactory by instance()

    private lateinit var cityViewModel: CityViewModel
    private lateinit var viewAdapter: ForecastCityAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var binding: ActivityCityBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_city)
        binding.lifecycleOwner = this

        cityViewModel = ViewModelProviders.of(this, cityViewModelFactory).get(CityViewModel::class.java)
        binding.cityViewModel = cityViewModel

        viewAdapter = ForecastCityAdapter(this)
        viewManager = LinearLayoutManager(this)

        forecast_city_recycler_view.apply {
            setHasFixedSize(true)
            adapter = viewAdapter
            layoutManager = viewManager
        }

        location_button.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        search_city_button.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        val bundle : Bundle? = intent.extras
        val message = bundle?.getString("CITY")
        var str: String? = intent.getStringExtra("CITY")
        if (message != null) {
            cityViewModel.city.set(message)
            CoroutineScope(Dispatchers.Main).launch {
                delay(200)
                bindUI()
                getCityForecast()
            }
        } else {
            updateCity()
        }

        saveCity()
    }

     private suspend fun bindUI() = launch {
        val cityWeather = cityViewModel.currentWeatherByCity.await()
        cityWeather.observe(this@CityActivity, Observer {
            if (it == null) return@Observer

            val description = it.weather?.get(0)?.description
            updateBackground(description.toString())

            val temp = it.main.temp
            val rounded = temp.toBigDecimal().setScale(1, RoundingMode.UP).toDouble()

            val tempMax = it.main.tempMax
            val rounded2 = tempMax.toBigDecimal().setScale(1, RoundingMode.UP).toDouble()

            val tempMin = it.main.tempMin
            val rounded3 = tempMin.toBigDecimal().setScale(1, RoundingMode.UP).toDouble()

            val humidity = it.main.humidity

            val visibility = it.visibility

            val cloudiness = it.clouds.all

            Glide.with(this@CityActivity)
                .load("http://openweathermap.org/img/w/${it.weather?.get(0)?.icon}.png")
                .into(icon_city_image_view)

            Glide.with(this@CityActivity)
                .load("http://openweathermap.org/img/w/${it.weather?.get(0)?.icon}.png")
                .into(details_city_image_view)

            val wind = it.wind.speed

            val pressure = it.main.pressure

            updateData(
                rounded, rounded2, rounded3, humidity, visibility, cloudiness, description.toString(),
                wind, pressure
            )
        })
    }

    private fun updateData(temp: Double, tempMax: Double, tempMin: Double, humidity: Int,
                           visibility: Int, cloudiness: Int, description: String, wind: Double, pressure: Double) {
        condition_city_text_view.text = description.capitalize()
        temp_city_text_view.text = temp.toInt().toString().plus(" ℃")
        max_temp_city_text_view.text = tempMax.toInt().toString().plus(" ℃")
        min_temp_city_text_view.text = tempMin.toInt().toString().plus(" ℃")
        humidity_city_text_view.text = humidity.toString().plus("%")
        visibility_city_text_view.text = visibility.toString().plus("m")
        cloudiness_city_text_view.text = cloudiness.toString().plus("%")
        wind_speed_city_text_view.text = wind.toString().plus(" m/s")
        pressure_city_text_view.text = pressure.toInt().toString().plus(" hPa")
    }

    private suspend fun getCityForecast() = launch {
        val forecastList = cityViewModel.forecastByCity.await()
        forecastList.observe(this@CityActivity, Observer {
            if (it == null) return@Observer
            val list = it.list
            viewAdapter.updateForecast(list as ArrayList<List>)
        })

    }

    private fun updateBackground(description: String) {
        when (description) {
            in "clear sky" -> city_activity.setBackgroundColor(Color.parseColor("#0091EA"))
            in "few clouds" ->  city_activity.setBackgroundColor(Color.parseColor("#2962FF"))
            in "shower rain" -> city_activity.setBackgroundColor(Color.parseColor("#304FFE"))
            in "scattered clouds" -> city_activity.setBackgroundColor(Color.parseColor("#356DCC"))
            in "overcast clouds" -> city_activity.setBackgroundColor(Color.parseColor("#8C90AD"))
            in "broken clouds" -> city_activity.setBackgroundColor(Color.parseColor("#5B64A8"))
            in "thunderstorm" -> city_activity.setBackgroundColor(Color.parseColor("#292D4B"))
        }
    }

    private fun saveCity() {
        if (city_city_text_view != null) {
            val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
            val editor = sharedPref.edit()
            editor
                .putString("NAME", cityViewModel.city.get())
                .apply()
        }
    }

    private fun updateCity() {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        sharedPref.apply {
            val city = getString("NAME", "")
            cityViewModel.city.set(city)

            if (cityViewModel.city.get() != null) {
                CoroutineScope(Dispatchers.Main).launch {
                    delay(200)
                     bindUI()
                    getCityForecast()
                }

            }
        }
    }
}