package com.android.nldlam.sample.weatherapp.data.model

import android.os.Parcel
import android.os.Parcelable

data class Weather(
    var city: String,
    var weatherInfo: MutableList<WeatherInfo>
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        arrayListOf<WeatherInfo>().apply {
            parcel.readArrayList(WeatherInfo::class.java.classLoader)
        }
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(city)
        parcel.writeList(weatherInfo)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Weather> {
        override fun createFromParcel(parcel: Parcel): Weather {
            return Weather(parcel)
        }

        override fun newArray(size: Int): Array<Weather?> {
            return arrayOfNulls(size)
        }
    }
}