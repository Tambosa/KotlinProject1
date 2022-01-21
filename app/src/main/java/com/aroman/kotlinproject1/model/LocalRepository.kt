package com.aroman.kotlinproject1.model

interface LocalRepository {

    fun getAllHistory(): List<Weather>

    fun getHistoryByCity(word: String): List<Weather>

    fun saveEntity(weather: Weather)
}