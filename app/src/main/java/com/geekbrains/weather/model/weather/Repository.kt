package com.geekbrains.weather.model.weather

interface Repository {
    fun getWeatherFromServer(): Weather?
    fun getWeatherFromLocalStorageRus(): List<Weather>
    fun getWeatherFromLocalStorageWorld(): List<Weather>

    fun weatherLoaded(weather: Weather?)
    fun addLoadedListener(listener: OnLoadListener)
    fun removeLoadedListener(listener: OnLoadListener)

    fun interface OnLoadListener {
        fun onLoaded()
    }
}