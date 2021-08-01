package com.android.nldlam.sample.weatherapp

import android.app.Application

class WeatherApp : Application() {
    companion object {
        lateinit var instance: WeatherApp
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}