package com.aroman.kotlinproject1.model

object RepositoryImpl : Repository {
    private val listeners: MutableList<Repository.OnLoadListener> = mutableListOf()
    private var weather: Weather? = null

    override fun getWeatherFromServer(): Weather? = weather

    override fun getWeatherFromLocalStorageRus(): List<Weather> = getRussianCities()

    override fun getWeatherFromLocalStorageWorld(): List<Weather> = getWorldCities()

    override fun weatherLoaded(weather: Weather?) {
        this.weather = weather
        listeners.forEach { it.onLoaded() }
    }

    override fun addLoadListener(listener: Repository.OnLoadListener) {
        listeners.add(listener)
    }

    override fun removeLoadListener(listener: Repository.OnLoadListener) {
        listeners.remove(listener)
    }
}