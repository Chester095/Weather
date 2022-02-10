package com.geekbrains.weather.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.geekbrains.weather.model.LocalRepository
import com.geekbrains.weather.model.LocalRepositoryImpl
import com.geekbrains.weather.model.weather.Weather
import com.geekbrains.weather.view.App

class DetailViewModel : ViewModel() {
    private val localRepo: LocalRepository = LocalRepositoryImpl(App.getHistoryDao())

    fun saveHistory(weather: Weather) {
        Log.d("!!! DetailViewModel", " saveHistory $weather")
        localRepo.saveEntity(weather)
    }

}