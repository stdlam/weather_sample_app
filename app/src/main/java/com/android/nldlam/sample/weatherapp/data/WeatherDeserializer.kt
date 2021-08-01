package com.android.nldlam.sample.weatherapp.data

import com.android.nldlam.sample.weatherapp.data.model.Weather
import com.android.nldlam.sample.weatherapp.data.model.WeatherInfo
import com.android.nldlam.sample.weatherapp.utils.toCelsius
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class WeatherDeserializer : JsonDeserializer<Weather> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Weather {
        val weather = Weather("", mutableListOf())
        val weatherInfo = mutableListOf<WeatherInfo>()
        json?.asJsonObject?.let { jsonObject ->
            val cityJsonObj = jsonObject.get("city").asJsonObject
            val city = cityJsonObj.get("name").asString
            weather.city = city
            val list = jsonObject.get("list").asJsonArray
            list.forEach { jsonElement ->
                val eleObject = jsonElement.asJsonObject
                val day = eleObject.get("dt").asInt
                val pressure = eleObject.get("pressure").asInt
                val humidity = eleObject.get("humidity").asInt

                val weatherObject = eleObject.getAsJsonArray("weather")[0].asJsonObject
                val description = weatherObject.get("description").asString

                val tempJsonObject = eleObject.getAsJsonObject("temp")
                val minTemp = tempJsonObject.get("min").asInt
                val maxTemp = tempJsonObject.get("max").asInt
                val avgTmp = ((minTemp + maxTemp) / 2).toDouble().toCelsius()
                weatherInfo.add(WeatherInfo(city = city, day = day, temp = avgTmp, pressure = pressure, humidity = humidity, description = description))
            }
            weather.weatherInfo = weatherInfo
        }
        return weather
    }

}