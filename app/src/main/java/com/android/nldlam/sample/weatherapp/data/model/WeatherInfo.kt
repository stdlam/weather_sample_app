package com.android.nldlam.sample.weatherapp.data.model

import android.os.Parcel
import android.os.Parcelable

data class WeatherInfo(
    val city: String,
    val day: Int,
    val temp: Double,
    val pressure: Int,
    val humidity: Int,
    val description: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(city)
        parcel.writeInt(day)
        parcel.writeDouble(temp)
        parcel.writeInt(pressure)
        parcel.writeInt(humidity)
        parcel.writeString(description)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WeatherInfo> {
        override fun createFromParcel(parcel: Parcel): WeatherInfo {
            return WeatherInfo(parcel)
        }

        override fun newArray(size: Int): Array<WeatherInfo?> {
            return arrayOfNulls(size)
        }
    }

}