package com.aroman.kotlinproject1.model

interface Repository {
    fun getWeatherFromServer(): Weather?
    fun getWeatherFromLocalStorageRus(): List<Weather>
    fun getWeatherFromLocalStorageWorld(): List<Weather>

    fun weatherLoaded(weather: Weather?)
    fun addLoadListener(listener: OnLoadListener)
    fun removeLoadListener(listener: OnLoadListener)

    fun interface OnLoadListener {
        fun onLoaded()
    }
}