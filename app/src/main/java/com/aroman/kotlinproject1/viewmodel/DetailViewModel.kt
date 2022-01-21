package com.aroman.kotlinproject1.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aroman.kotlinproject1.model.*
import com.aroman.kotlinproject1.view.App

class DetailViewModel : ViewModel() {

    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()
    private val localRepository: LocalRepository = LocalRepositoryImpl(App.getHistoryDAO())

    fun getData(): LiveData<AppState> = liveDataToObserve

    fun saveHistory(weather: Weather) {
        localRepository.saveEntity(weather)
    }
}