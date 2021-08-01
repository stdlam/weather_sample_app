package com.android.nldlam.sample.weatherapp.data.repository

import com.android.nldlam.sample.weatherapp.data.model.Weather
import com.android.nldlam.sample.weatherapp.service.ApiService
import com.android.nldlam.sample.weatherapp.utils.ApiUtils
import retrofit2.Call

class WeatherRepository {
    companion object {
        @Volatile
        private var instance: WeatherRepository? = null
        fun getInstance(): WeatherRepository {
            instance?.let {
                return it
            }
            return synchronized(this) {
                var i2 = instance
                if (i2 != null) {
                    i2
                } else {
                    i2 = WeatherRepository()
                    instance = i2
                    i2
                }
            }
        }
    }

    private val apiService: ApiService? = ApiUtils.getApiServiceInstance()

    fun queryWeatherByCity(city: String, numOfDays: Int): Call<Weather>? {
        apiService?.let {
            return it.queryWeatherByCity(city, numOfDays)
        }
        return null
    }
}