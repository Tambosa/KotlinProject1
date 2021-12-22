package com.aroman.kotlinproject1.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aroman.kotlinproject1.R
import com.aroman.kotlinproject1.model.Weather

class MainAdapter : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    private var weather: List<Weather> = listOf()
    var listener: OnItemClick? = null

    fun setWeather(data: List<Weather>) {
        weather = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.main_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(weather[position])
    }

    override fun getItemCount(): Int = weather.size

    inner class MainViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(weather: Weather) {
            itemView.apply {
                findViewById<TextView>(R.id.main_city_name_text_view).text = weather.city.name
                setOnClickListener {
                    if (listener != null) {
                        listener?.onClick(weather)
                    }
                }
            }
        }
    }

    fun interface OnItemClick {
        fun onClick(weather: Weather)
    }
}
