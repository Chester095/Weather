package com.geekbrains.weather.model.weather

import android.util.Log
import com.geekbrains.weather.model.weather.Repository.*

object RepositoryImpl : Repository {

    private const val TAG = "!!! RepositoryImpl "
    private val listeners: MutableList<OnLoadListener> = mutableListOf()
    private var weather: Weather? = null

    override fun getWeatherFromServer(): Weather? = weather

    override fun getWeatherFromLocalStorageRus(): List<Weather> = getRussianCities()

    override fun getWeatherFromLocalStorageWorld(): List<Weather> = getWorldCities()

    override fun weatherLoaded(weather: Weather?) {
        RepositoryImpl.weather = weather
        // уведомляем все Listener, что погода получена
        listeners.forEach {
            it.onLoaded()
            Log.d(TAG, "weatherLoaded  listenerS = $listeners")
        }
    }

    override fun addLoadedListener(listener: OnLoadListener) {
        Log.d(TAG, "addLoadedListener listener = $listener")
        listeners.add(listener)
    }

    override fun removeLoadedListener(listener: OnLoadListener) {
        Log.d(TAG, "removeLoadedListener listener = $listener")
        listeners.remove(listener)
    }
}