package com.example.kotlinforecast.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kotlinforecast.R
import com.example.kotlinforecast.adapter.ForecastAdapter
import com.example.kotlinforecast.data.model.forecastWeather.List
import com.example.kotlinforecast.ui.base.Scoped
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import java.math.RoundingMode
import kotlin.collections.ArrayList

private const val MY_PERMISSION_ACCESS_COARSE_LOCATION = 1

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MainActivity : Scoped(), KodeinAware {
    override val kodein by closestKodein()

    private val mainViewModelFactory: MainViewModelFactory by instance()

    private lateinit var mainViewModel: MainViewModel
    private lateinit var viewAdapter: ForecastAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        mainViewModel = ViewModelProviders.of(this, mainViewModelFactory).get(MainViewModel::class.java)

        viewAdapter = ForecastAdapter(this)
        viewManager = LinearLayoutManager(this)

        forecast_recycler_view.apply {
            setHasFixedSize(true)
            adapter = viewAdapter
            layoutManager = viewManager
        }

        requestLocationPermission()
        if (hasLocationPermission()) {
            bindUI()
        }

        search_button.setOnClickListener{
            val intent = Intent(this, CityActivity::class.java)
            startActivity(intent)
        }
    }

    private fun bindUI() = launch {
        val currentWeather = mainViewModel.currentWeatherByCoordinates.await()
       currentWeather.observe(this@MainActivity, Observer {
           if (it == null) return@Observer

           getForecast()

           val name = it.name

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

           Glide.with(this@MainActivity)
               .load("http://openweathermap.org/img/w/${it.weather?.get(0)?.icon}.png")
               .into(icon_image_view)

           Glide.with(this@MainActivity)
               .load("http://openweathermap.org/img/w/${it.weather?.get(0)?.icon}.png")
               .into(details_image_view)

           val wind = it.wind.speed

           val pressure = it.main.pressure

           updateData(name, rounded, rounded2, rounded3, humidity, visibility, cloudiness, description.toString(),
               wind, pressure)
       })

    }

    private fun updateData(name: String, temp: Double, tempMax: Double, tempMin: Double, humidity: Int, visibility: Int,
                           cloudiness: Int, description: String, wind: Double, pressure: Double) {
        city_text_view.text = name
        temp_text_view.text = temp.toInt().toString().plus(" ℃")
        max_temp_text_view.text = tempMax.toInt().toString().plus(" ℃")
        min_temp_text_view.text = tempMin.toInt().toString().plus(" ℃")
        humidity_text_view.text = humidity.toString().plus("%")
        visibility_text_view.text = visibility.toString().plus("m")
        cloudiness_text_view.text = cloudiness.toString().plus("%")
        condition_text_view.text = description.capitalize()
        wind_speed_text_view.text = wind.toString().plus(" m/s")
        pressure_text_view.text = pressure.toInt().toString().plus(" hPa")
    }

    private fun getForecast() = launch {
        val forecastList = mainViewModel.forecastByCoordinates.await()
        forecastList.observe(this@MainActivity, Observer {
            if (it == null) return@Observer
            val list = it.list
            viewAdapter.updateForecast(list as ArrayList<List>)
        })

    }

    private fun updateBackground(description: String) {
        when (description) {
            in "clear sky" -> main_activity.setBackgroundColor(Color.parseColor("#0091EA"))
            //in "clear sky" -> main_activity.background = ContextCompat.getDrawable(this, R.drawable.gradient)
            in "few clouds" ->  main_activity.setBackgroundColor(Color.parseColor("#2962FF"))
            in "shower rain" -> main_activity.setBackgroundColor(Color.parseColor("#304FFE"))
            in "scattered clouds" -> main_activity.setBackgroundColor(Color.parseColor("#356DCC"))
            in "overcast clouds" -> main_activity.setBackgroundColor(Color.parseColor("#8C90AD"))
            in "broken clouds" -> main_activity.setBackgroundColor(Color.parseColor("#5B64A8"))

        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
            MY_PERMISSION_ACCESS_COARSE_LOCATION
        )
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == MY_PERMISSION_ACCESS_COARSE_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            bindUI()
            else
                Toast.makeText(this, "Permission Denied. You can only check weather only manually", Toast.LENGTH_LONG).show()
        }
    }

}
