package com.aroman.kotlinproject1.view

import android.app.Application
import androidx.room.Room
import com.aroman.kotlinproject1.model.HistoryDAO
import com.aroman.kotlinproject1.model.HistoryDataBase

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }

    companion object {

        private var appInstance: App? = null
        private var db: HistoryDataBase? = null
        private const val DB_NAME = "History.db"

        fun getHistoryDAO(): HistoryDAO {

            if (db == null) {
                synchronized(HistoryDataBase::class.java) {
                    if (db == null) {
                        appInstance?.let { app ->
                            db = Room.databaseBuilder(
                                app.applicationContext,
                                HistoryDataBase::class.java,
                                DB_NAME
                            ).allowMainThreadQueries()
                                .build()
                        } ?: throw Exception("DB exception!!!")
                    }
                }
            }

            return db!!.historyDAO()
        }
    }
}