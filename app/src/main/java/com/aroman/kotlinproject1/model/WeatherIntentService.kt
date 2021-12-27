package com.aroman.kotlinproject1.model

import android.app.IntentService
import android.content.Intent


class WeatherIntentService : IntentService("WeatherIntentService") {


    override fun onHandleIntent(intent: Intent?) {
        intent?.getParcelableExtra<Weather>("WEATHER_EXTRA")?.let { weather ->
            WeatherLoader.load(weather.city, object : WeatherLoader.OnWeatherLoadListener {
                override fun onLoaded(weatherDTO: WeatherDTO) {
                    applicationContext.sendBroadcast(
                        Intent(
                            applicationContext,
                            WeatherReceiver::class.java
                        ).apply {
                            putExtra(
                                "WEATHER_EXTRA", Weather(
                                    temperature = weatherDTO.fact?.temp ?: 0,
                                    feelsLike = weatherDTO.fact?.feels_like ?: 0,
                                    condition = weatherDTO.fact?.condition ?: "",
                                )
                            )
                            action = WeatherReceiver.WEATHER_LOADED
                        })
                }

                override fun onFailed(throwable: Throwable) {
                    applicationContext.sendBroadcast(
                        Intent(
                            applicationContext,
                            WeatherReceiver::class.java
                        ).apply {
                            action = WeatherReceiver.WEATHER_FAILED
                        })
                }
            })
        }
    }
}