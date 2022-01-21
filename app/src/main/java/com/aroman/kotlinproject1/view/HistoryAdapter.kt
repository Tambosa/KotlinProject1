package com.aroman.kotlinproject1.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aroman.kotlinproject1.R
import com.aroman.kotlinproject1.model.Weather

class HistoryAdapter(private var items: List<Weather>) :
    RecyclerView.Adapter<HistoryAdapter.HistoryItemViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryItemViewHolder {
        return HistoryItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.history_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: HistoryItemViewHolder, position: Int) {

        val weather = items[position]


        holder.itemView.apply {
            findViewById<TextView>(R.id.history_city_name).text = weather.city.name
            findViewById<TextView>(R.id.history_city_temperature).text =
                weather.temperature.toString()
            findViewById<TextView>(R.id.history_city_condition).text = weather.condition
        }


    }

    override fun getItemCount(): Int = items.size

    class HistoryItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }
}
