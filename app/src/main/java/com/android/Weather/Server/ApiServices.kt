package com.android.Weather.Server

import com.android.Weather.Model.CityResponseApi
import com.android.Weather.Model.CurrentResponseApi
import com.android.Weather.Model.ForecastResponseApi
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices {

    @GET("data/2.5/weather")
    fun getCurrenWeather(
        @Query("lat") lat:Double,
        @Query("lon") lon:Double,
        @Query("units") units:String,
        @Query("appid") ApiKey:String,
    ): Call<CurrentResponseApi>

    @GET("data/2.5/forecast")
    fun getForecastWeather(
        @Query("lat") lat:Double,
        @Query("lon") lon:Double,
        @Query("units") units:String,
        @Query("appid") ApiKey:String,
    ): Call<ForecastResponseApi>

    @GET("geo/1.0/direct")
    fun getCitiesList(
        @Query("q") q:String,
        @Query("limit") limit:Int,
        @Query("appid") ApiKey: String
    ):Call<CityResponseApi>
}