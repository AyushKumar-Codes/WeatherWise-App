package com.android.Weather.Repository

import com.android.Weather.Server.ApiServices

class CityRepository(val api: ApiServices) {
    fun getCities(q:String,limit:Int)=
        api.getCitiesList(q,limit,"894950804100e0ea4ac8a936f090781e")
}