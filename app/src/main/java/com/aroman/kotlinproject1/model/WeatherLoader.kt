package com.aroman.kotlinproject1.model

import android.os.Handler
import android.os.Looper
import com.aroman.kotlinproject1.BuildConfig
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.URL
import java.util.concurrent.Executors
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

object WeatherLoader {

    fun load(city: City, listener: OnWeatherLoadListener) {

        val handler = Handler(Looper.myLooper() ?: Looper.getMainLooper())

        Executors.newSingleThreadExecutor().submit {
            var urlConnections: HttpsURLConnection? = null
            try {
                val uri =
                    URL("https://api.weather.yandex.ru/v2/forecast/?lat=${city.lat}&lon=${city.lon}")

                urlConnections = (uri.openConnection() as HttpsURLConnection).apply {
                    addRequestProperty("X-Yandex-API-Key", BuildConfig.WEATHER_API_KEY)
                    requestMethod = "GET"
                    readTimeout = 1000
                    connectTimeout = 1000
                }

                val reader = BufferedReader(InputStreamReader(urlConnections.inputStream))
                val result = reader.lines().collect(Collectors.joining("\n"))

                val weatherDTO = Gson().fromJson(result, WeatherDTO::class.java)

                handler.post {
                    listener.onLoaded(weatherDTO)
                }
            } catch (e: Exception) {
                handler.post {
                    listener.onFailed(e)
                }
            } finally {
                urlConnections?.disconnect()
            }
        }
    }

    interface OnWeatherLoadListener {
        fun onLoaded(weatherDTO: WeatherDTO)
        fun onFailed(throwable: Throwable)
    }
}