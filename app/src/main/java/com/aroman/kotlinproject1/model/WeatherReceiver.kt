package com.aroman.kotlinproject1.model

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class WeatherReceiver : BroadcastReceiver() {

    companion object {
        const val WEATHER_LOADED = "WEATHER_LOADED"
        const val WEATHER_FAILED = "WEATHER_FAILED"
    }

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            WEATHER_LOADED -> RepositoryImpl.weatherLoaded(intent.getParcelableExtra("WEATHER_EXTRA"))
            WEATHER_FAILED -> RepositoryImpl.weatherLoaded(null)
        }
    }
}