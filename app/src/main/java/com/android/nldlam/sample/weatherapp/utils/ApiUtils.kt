package com.android.nldlam.sample.weatherapp.utils

import com.android.nldlam.sample.weatherapp.WeatherApp
import com.android.nldlam.sample.weatherapp.data.source.remote.RetrofitClient
import com.android.nldlam.sample.weatherapp.enums.Constant
import com.android.nldlam.sample.weatherapp.service.ApiService

object ApiUtils {
    fun getApiServiceInstance(): ApiService? {
        return RetrofitClient.getRetrofitClient(WeatherApp.instance, Constant.OPEN_WEATHER_MAP_BASE_API)?.create(ApiService::class.java)
    }

    // just for testing
    fun getApiServiceInstanceForTesting(): ApiService? {
        return RetrofitClient.getRetrofitClientForTesting(Constant.OPEN_WEATHER_MAP_BASE_API)?.create(ApiService::class.java)
    }
}