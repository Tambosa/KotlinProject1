package com.aroman.kotlinproject1.model

import android.util.Log
import com.aroman.kotlinproject1.BuildConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.Exception
import java.net.URL
import java.util.concurrent.TimeUnit
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

object WeatherLoader {

    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .callTimeout(1000, TimeUnit.MILLISECONDS)
            .connectTimeout(1000, TimeUnit.MILLISECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            })
            .build()
    }

    private val weatherApi: WeatherApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.weather.yandex.ru/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()
            .create(WeatherApi::class.java)
    }

    fun load(city: City, listener: OnWeatherLoadListener) {
        var urlConnections: HttpsURLConnection? = null

        try {
            val uri =
                URL("https://api.weather.yandex.ru/v2/informers?lat=${city.lat}&lon=${city.lon}")

            urlConnections = (uri.openConnection() as HttpsURLConnection).apply {
                addRequestProperty("X-Yandex-API-Key", BuildConfig.WEATHER_API_KEY)
                requestMethod = "GET"
                readTimeout = 1000
                connectTimeout = 1000
            }

            val reader = BufferedReader(InputStreamReader(urlConnections.inputStream))
            val result = reader.lines().collect(Collectors.joining("\n"))

            val weatherDTO = Gson().fromJson(result, WeatherDTO::class.java)

            Log.d("WEATHERLOADED", "load: $weatherDTO")

            listener.onLoaded(weatherDTO)

        } catch (e: Exception) {
            listener.onFailed(e)
        } finally {
            urlConnections?.disconnect()
        }
    }

    fun loadOkHttp(city: City, listener: OnWeatherLoadListener) {

        val request: Request = Request.Builder()
            .get()
            .addHeader("X-Yandex-API-Key", BuildConfig.WEATHER_API_KEY)
            .url("https://api.weather.yandex.ru/v2/informers?lat=${city.lat}&lon=${city.lon}")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                listener.onFailed(e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val weatherDTO =
                        Gson().fromJson(response.body?.string(), WeatherDTO::class.java)
                    listener.onLoaded(weatherDTO)
                } else {
                    val e = Exception("Response exception ${response.message}")
                    listener.onFailed(e)
                    Log.d("DEBUGLOG", e.toString())
                }
            }
        })
    }

    fun loadRetrofit(city: City, listener: OnWeatherLoadListener) {

        weatherApi.getWeather(BuildConfig.WEATHER_API_KEY, city.lat, city.lon)
            .enqueue(object : retrofit2.Callback<WeatherDTO> {
                override fun onResponse(
                    call: retrofit2.Call<WeatherDTO>,
                    response: retrofit2.Response<WeatherDTO>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { listener.onLoaded(it) }
                    } else {
                        val e = Exception("Response exception ${response.message()}")
                        listener.onFailed(e)
                        Log.d("DEBUGLOG", e.toString())
                    }
                }

                override fun onFailure(call: retrofit2.Call<WeatherDTO>, t: Throwable) {
                    listener.onFailed(t)
                }
            })

    }

    interface OnWeatherLoadListener {
        fun onLoaded(weatherDTO: WeatherDTO)
        fun onFailed(throwable: Throwable)
    }
}