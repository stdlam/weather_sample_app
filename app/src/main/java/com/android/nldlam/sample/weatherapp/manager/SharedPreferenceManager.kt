package com.android.nldlam.sample.weatherapp.manager

import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceManager(context: Context) {
    companion object {
        private const val PREFERENCE_DATA = "PREFERENCE_DATA"
        private const val KEY_LAST_SEARCH_CITY = "KEY_LAST_SEARCH_CITY"

        @Volatile
        private var mInstance: SharedPreferenceManager? = null
        fun getInstance(context: Context): SharedPreferenceManager {
            mInstance?.let {
                return it
            }
            return synchronized(this) {
                var i2 = mInstance
                if (i2 != null) {
                    i2
                } else {
                    i2 = SharedPreferenceManager(context)
                    mInstance = i2
                    i2
                }
            }
        }
    }

    private val mDataSharedPreferences: SharedPreferences
    init {
        mDataSharedPreferences = context.getSharedPreferences(PREFERENCE_DATA, Context.MODE_PRIVATE)
    }

    private fun setString(sharedPreferences: SharedPreferences, key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    private fun getString(sharedPreferences: SharedPreferences, key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    fun saveLastSearch(city: String) {
        setString(mDataSharedPreferences, KEY_LAST_SEARCH_CITY, city)
    }

    fun getLastSearch(): String {
        return getString(mDataSharedPreferences, KEY_LAST_SEARCH_CITY, "")
    }

}