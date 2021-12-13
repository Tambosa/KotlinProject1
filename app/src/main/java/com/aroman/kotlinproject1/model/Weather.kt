package com.aroman.kotlinproject1.model

data class Weather(
    val city: String = "Moscow",
    val temperature: Int = 5,
    val latitude: Float = 55.7f,
    val longitude: Float = 37.6f
)