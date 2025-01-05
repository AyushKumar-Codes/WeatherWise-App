package com.android.Weather.Repository

import com.android.Weather.Server.ApiServices

class WeatherRepository (val api: ApiServices){

    fun getCurrentWeather(lat:Double ,lng:Double,unit:String) =
        api.getCurrenWeather(lat,lng,unit,"894950804100e0ea4ac8a936f090781e");

    fun getForecastWeather(lat:Double ,lng:Double,unit:String) =
        api.getForecastWeather(lat,lng,unit,"894950804100e0ea4ac8a936f090781e");

}