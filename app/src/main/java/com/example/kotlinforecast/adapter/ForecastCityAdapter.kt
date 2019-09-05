package com.example.kotlinforecast.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinforecast.R
import com.example.kotlinforecast.data.model.forecastWeatherCity.List
import com.example.kotlinforecast.internal.glide.GlideApp
import kotlinx.android.synthetic.main.forecast_city_recyclerview_item.view.*
import org.threeten.bp.format.DateTimeFormatter

class ForecastCityAdapter(
    private val context: Context
): RecyclerView.Adapter<ForecastCityViewHolder>() {

    private var list: ArrayList<List> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastCityViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val contactRow = layoutInflater.inflate(R.layout.forecast_city_recyclerview_item, parent, false)
        return ForecastCityViewHolder(contactRow)
    }

    override fun getItemCount(): Int {
       return list.size
    }

    fun updateForecast(forecastList: ArrayList<List>) {
        this.list = forecastList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ForecastCityViewHolder, position: Int) {
        val time = holder.view.time_text_view_item_city
        val tempMax = holder.view.temp_max_text_view_item_city
        val tempMin = holder.view.temp_min_text_view_item_city
        val icon = holder.view.icon_image_view_item_city

        val date = list.get(position).dt.toLong()
        val format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")                  //Time and Date Formatter
        val timestampAsDateString = java.time.format.DateTimeFormatter.ISO_INSTANT
            .format(java.time.Instant.ofEpochSecond(date))
        val localDate = org.threeten.bp.LocalDate.parse(timestampAsDateString, format)
        val localTime = org.threeten.bp.LocalTime.parse(timestampAsDateString, format)

        time.text = localDate.dayOfWeek.toString().toLowerCase().capitalize().plus(" ").plus(localTime.hour.toString())
            .plus(":").plus(localTime.minute).plus("0")
        tempMax.text = list.get(position).main.temp_max.toInt().toString().plus(" ℃")
        tempMin.text = list.get(position).main.temp_min.toInt().toString().plus(" ℃")

        GlideApp.with(context)
            .load("http://openweathermap.org/img/w/${list.get(position).weather?.get(0)?.icon}.png")
            .into(icon)
    }

}
class ForecastCityViewHolder(val view: View): RecyclerView.ViewHolder(view)