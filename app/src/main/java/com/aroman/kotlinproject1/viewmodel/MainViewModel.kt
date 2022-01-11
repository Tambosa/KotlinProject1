package com.aroman.kotlinproject1.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aroman.kotlinproject1.model.Repository
import com.aroman.kotlinproject1.model.RepositoryImpl

class MainViewModel : ViewModel() {

    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()
    private val repository: Repository = RepositoryImpl

    fun getData(): LiveData<AppState> = liveDataToObserve

    fun getWeatherLocalRus() = getWeatherLocal(true)

    fun getWeatherLocalWorld() = getWeatherLocal(false)

    //fun getWeatherYaApi() = TODO

    private fun getWeatherLocal(isRussian: Boolean = true) {
        liveDataToObserve.value = AppState.Loading

        Thread {
            val weather =
                if (isRussian) repository.getWeatherFromLocalStorageRus()
                else repository.getWeatherFromLocalStorageWorld()
            liveDataToObserve.postValue(AppState.Success(weather))
        }.start()
    }
}