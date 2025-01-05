package com.android.Weather.ViewModel

import androidx.lifecycle.ViewModel
import com.android.Weather.Repository.CityRepository
import com.android.Weather.Server.ApiClient
import com.android.Weather.Server.ApiServices

class CityViewModel(val repository: CityRepository): ViewModel() {
    constructor():this(CityRepository(ApiClient().getClient().create(ApiServices::class.java)))
    fun loadCity(q:String,limit:Int)=repository.getCities(q,limit)
}