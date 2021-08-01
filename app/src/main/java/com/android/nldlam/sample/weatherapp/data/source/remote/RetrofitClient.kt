package com.android.nldlam.sample.weatherapp.data.source.remote

import android.content.Context
import com.android.nldlam.sample.weatherapp.data.WeatherDeserializer
import com.android.nldlam.sample.weatherapp.data.model.Weather
import com.android.nldlam.sample.weatherapp.utils.hasNetworkAvailable
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private var retrofit: Retrofit? = null
    private const val TIME_ONLINE_CACHE = 5     // fetch 5s ago if has internet
    private const val TIME_OFFLINE_CACHE = 86400    // fetch 1 day ago if no internet

    @Synchronized
    fun getRetrofitClient(context: Context, baseUrl: String): Retrofit? {
        if (retrofit == null) {
            val cacheSize = (5 * 1024 * 1024).toLong()      // 5 MB
            val weatherCache = Cache(context.cacheDir, cacheSize)
            val okHttpClient = OkHttpClient.Builder()
                .cache(weatherCache)
                .addInterceptor { chain ->
                    var request = chain.request()
                    request = if (context.hasNetworkAvailable()) {
                        request.newBuilder().header("Cache-Control",
                            "public, max-age=$TIME_ONLINE_CACHE"
                        ).build()
                    } else {
                        request.newBuilder().header("Cache-Control",
                            "public, only-if-cached, max-stale=$TIME_OFFLINE_CACHE"
                        ).build()
                    }
                    chain.proceed(request)
                }
                .build()

            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(
                    GsonBuilder()
                        .setLenient()
                        .registerTypeAdapter(object : TypeToken<Weather>() {}.type, WeatherDeserializer())
                        .create())
                )
                .client(okHttpClient)
                .build()
        }
        return retrofit
    }

    fun getRetrofitClientForTesting(baseUrl: String): Retrofit? {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(
                    GsonBuilder()
                        .setLenient()
                        .registerTypeAdapter(object : TypeToken<Weather>() {}.type, WeatherDeserializer())
                        .create())
                )
                .build()
        }
        return retrofit
    }
}