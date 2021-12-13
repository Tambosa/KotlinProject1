package com.aroman.kotlinproject1.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aroman.kotlinproject1.model.Repository
import com.aroman.kotlinproject1.model.RepositoryImpl
import java.lang.Exception
import java.lang.Thread.sleep
import kotlin.random.Random

class MainViewModel : ViewModel() {

    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()
    private val repository: Repository = RepositoryImpl()

    fun getData(): LiveData<AppState> = liveDataToObserve

    fun getWeather() {
        liveDataToObserve.value = AppState.Loading

        Thread {
            sleep(2000)

            if (Random.nextBoolean()) {
                liveDataToObserve.postValue(AppState.Success(repository.getWeatherFromServer()))
            } else {
                liveDataToObserve.postValue(AppState.Error(Exception("No Internet")))
            }
        }.start()
    }


}