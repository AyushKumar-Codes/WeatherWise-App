package com.android.Weather.ViewModel

import androidx.lifecycle.ViewModel
import com.android.Weather.Repository.WeatherRepository
import com.android.Weather.Server.ApiClient
import com.android.Weather.Server.ApiServices
import kotlin.math.ln

class WeatherViewModel (val repository: WeatherRepository): ViewModel(){
    constructor(): this(WeatherRepository(ApiClient().getClient().create(ApiServices::class.java)))

    fun loadCurrentWeather(lat: Double, lng:Double,unit:String)=
        repository.getCurrentWeather(lat, lng,unit)
    fun loadForecastWeather(lat: Double, lng:Double,unit:String)=
        repository.getForecastWeather(lat, lng,unit)
}