package com.aroman.kotlinproject1.model

data class WeatherDTO (
    val now: Long?,
    val fact: FactDTO?
)

data class FactDTO(
    val obs_time: Long?,
    val temp: Int?,
    val feels_like: Int?,
    val icon: String?,
    val condition: String?,
    val wind_speed: Double?,
    val wind_dir: String?,
    val pressure_mm: Int?,
    val pressure_pa: Int?,
    val humidity: Int?,
    val daytime: String?,
    val polar: Boolean?,
    val season: String?,
    val wind_gust: Double?,
)

