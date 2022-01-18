package com.aroman.kotlinproject1.model

import java.util.*

class LocalRepositoryImpl(private val dao: HistoryDAO) : LocalRepository {

    override fun getAllHistory(): List<Weather> {
        return dao.all().map { historyEntity ->
            Weather(
                city = City(historyEntity.city),
                temperature = historyEntity.temperature,
                condition = historyEntity.condition
            )
        }
    }

    override fun getHistoryByCity(word: String): List<Weather> {
        return dao.getHistoryByCity(word).map { historyEntity ->
            Weather(
                city = City(historyEntity.city),
                temperature = historyEntity.temperature,
                condition = historyEntity.condition
            )
        }
    }

    override fun saveEntity(weather: Weather) {
        dao.insert(
            HistoryEntity(
                0,
                weather.city.name,
                weather.temperature,
                weather.condition,
                timestamp = Date().time
            )
        )
    }
}