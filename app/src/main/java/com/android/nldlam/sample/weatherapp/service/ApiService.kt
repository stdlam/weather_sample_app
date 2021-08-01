package com.android.nldlam.sample.weatherapp.service

import com.android.nldlam.sample.weatherapp.data.model.Weather
import com.android.nldlam.sample.weatherapp.enums.Constant
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    // Retrofit caching https://bapspatil.medium.com/caching-with-retrofit-store-responses-offline-71439ed32fda
    @GET("2.5/forecast/daily?appid=${Constant.API_APP_ID}")
    fun queryWeatherByCity(@Query("q") city: String, @Query("cnt") numOfDay: Int): Call<Weather>
}