package com.example.kotlinforecast.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinforecast.R
import com.example.kotlinforecast.data.model.city.City
import kotlinx.android.synthetic.main.city_reyclerview_item.view.*

class SearchAdapter(
    private val context: Context,
    private var list: List<City> = ArrayList(),
    val mOnCityListener: OnCityListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var city = String

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val contactRow = layoutInflater.inflate(R.layout.city_reyclerview_item, parent, false)
        return SearchViewHolder(contactRow)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val city = holder.itemView.city_city_text_view_item

        city.text = list[position].name
    }

    fun updateList(list: ArrayList<City>) {
        this.list = list
        notifyDataSetChanged()
    }

    fun removeItem(viewHolder: RecyclerView.ViewHolder) {
        notifyItemRemoved(viewHolder.adapterPosition)
    }

    fun getCityPosition(position: Int): City {
        return list[position]
    }

    interface OnCityListener {
        fun onCityClick(position: Int)
    }

   inner class SearchViewHolder(view: View): RecyclerView.ViewHolder(view) {

        init {
            view.setOnClickListener {
                mOnCityListener.onCityClick(adapterPosition)
            }
        }
    }

}
