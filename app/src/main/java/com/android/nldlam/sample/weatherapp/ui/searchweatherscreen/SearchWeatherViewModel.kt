package com.android.nldlam.sample.weatherapp.ui.searchweatherscreen

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.nldlam.sample.weatherapp.R
import com.android.nldlam.sample.weatherapp.WeatherApp
import com.android.nldlam.sample.weatherapp.data.model.Weather
import com.android.nldlam.sample.weatherapp.data.repository.WeatherRepository
import com.android.nldlam.sample.weatherapp.utils.hasNetworkAvailable
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class SearchWeatherViewModel : ViewModel() {
    companion object {
        val TAG = SearchWeatherViewModel::class.simpleName
        private const val FAKE_LOADING_INTERVAL = 2000L
        private const val QUERY_TIMEOUT = 5000L
    }

    private val mUIStateLiveData: MutableLiveData<UIState> = MutableLiveData()
    private val mWeatherRepository = WeatherRepository.getInstance()
    private val mHandler = Handler(Looper.getMainLooper())
    private val mApp = WeatherApp.instance

    fun queryWeather(city: String, numOfDays: Int) {
        emitData(UIState(isLoading = true))
        mHandler.postDelayed({
            executeQuery(city, numOfDays)
        }, FAKE_LOADING_INTERVAL)
    }

    fun getUIState() = mUIStateLiveData

    private fun executeQuery(city: String, numOfDays: Int) {
        Log.d(TAG, "executeQuery - city=$city")
        viewModelScope.launch {
            try {
                withTimeout(QUERY_TIMEOUT) {
                    withContext(Dispatchers.IO) {
                        mWeatherRepository.queryWeatherByCity(city, numOfDays)?.let {
                            it.enqueue(object : Callback<Weather> {
                                override fun onResponse(call: Call<Weather>, response: Response<Weather>) {
                                    response.body()?.let { weather ->
                                        Log.d(TAG, "queryWeather - weatherResult size=${weather.weatherInfo.size}")
                                        emitData(UIState(isLoading = false, response = weather))
                                    } ?: kotlin.run {
                                        Log.e(TAG, "queryWeather - NO RESULT!!!")
                                        val message = if (!mApp.hasNetworkAvailable()) mApp.getString(R.string.no_internet) else mApp.getString(R.string.data_not_found)
                                        emitData(UIState(isLoading = false, errorMessage = message))
                                    }
                                }

                                override fun onFailure(call: Call<Weather>, t: Throwable) {
                                    Log.e(TAG, "queryWeather - onFailure - message=${t.message}")
                                    val message = if (!mApp.hasNetworkAvailable()) mApp.getString(R.string.no_internet) else mApp.getString(R.string.something_wrong)
                                    emitData(UIState(isLoading = false, errorMessage = message))
                                }

                            })
                        } ?: kotlin.run {
                            Log.e(TAG, "queryWeather - SERVICE NULL!!!")
                            emitData(UIState(isLoading = false, errorMessage = mApp.getString(R.string.no_service)))
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "queryWeather - TIMEOUT!!!")
                emitData(UIState(isLoading = false, errorMessage = mApp.getString(R.string.timeout)))
            }
        }
    }

    private fun emitData(uiState: UIState) {
        mUIStateLiveData.postValue(uiState)
    }

    data class UIState(var isLoading: Boolean? = null,
                       var response: Weather? = null,
                       var errorMessage: String? = null)
}