package com.android.nldlam.sample.weatherapp.ui.searchweatherscreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.nldlam.sample.weatherapp.R
import com.android.nldlam.sample.weatherapp.data.model.Weather
import com.android.nldlam.sample.weatherapp.manager.SharedPreferenceManager
import com.android.nldlam.sample.weatherapp.utils.disableButton
import com.android.nldlam.sample.weatherapp.utils.enableButton
import kotlinx.android.synthetic.main.activity_search_weather.*

class SearchWeatherActivity : AppCompatActivity() {
    companion object {
        private const val AVAILABLE_SEARCH_LENGTH = 3
        private const val NUM_OF_DAYS = 7
        private const val KEY_WEATHER = "KEY_WEATHER"

        private val TAG = SearchWeatherActivity::class.java.simpleName
    }

    private lateinit var mViewModel: SearchWeatherViewModel
    private var mInputText = ""
    private var mAdapter = WeatherAdapter()
    private var mWeatherCache: Weather = Weather(city = mInputText, weatherInfo = mutableListOf())
    private lateinit var mSharedPreferenceManager: SharedPreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_weather)

        mViewModel = ViewModelProvider(this).get(SearchWeatherViewModel::class.java)
        mSharedPreferenceManager = SharedPreferenceManager.getInstance(this)
        savedInstanceState?.let {
            if (it.containsKey(KEY_WEATHER)) {
                mWeatherCache = it.getParcelable(KEY_WEATHER)!!
                mInputText = mWeatherCache.city
            }
        } ?: kotlin.run {
            mInputText = mSharedPreferenceManager.getLastSearch()
            if (mInputText.length > AVAILABLE_SEARCH_LENGTH) {
                // fetch cached data
                // online or offline
                mViewModel.queryWeather(mInputText, NUM_OF_DAYS)
            }
        }

        Log.d(TAG, "onCreate - last search=$mInputText, cached data size=${mWeatherCache.weatherInfo.size}")
        initViews()
        observeData()
    }

    private fun observeData() {
        mViewModel.getUIState().observe(this, {
            it?.errorMessage?.let { message ->
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }
            it?.isLoading?.let { loading ->
                sflLoading.visibility = if (loading) {
                    btGet.disableButton()
                    sflLoading.startShimmerAnimation()
                    View.VISIBLE
                } else {
                    btGet.enableButton()
                    sflLoading.stopShimmerAnimation()
                    View.GONE
                }
            }
            it?.response?.let { data ->
                mInputText = data.city
                updateEditText()
                Log.d(TAG, "observeData - observed city=$mInputText")
                mWeatherCache = data
                mAdapter.setData(data.weatherInfo)
            }
        })
    }

    private fun initViews() {
        btGet.setOnClickListener {
            handleSearchClick()
        }

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // no need implement
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // no need implement
            }

            override fun afterTextChanged(s: Editable?) {
                mInputText = s.toString()
            }

        })
        updateEditText()

        rvResult.apply {
            layoutManager = LinearLayoutManager(this@SearchWeatherActivity, LinearLayoutManager.VERTICAL, false)
            adapter = mAdapter
        }

        if (mWeatherCache.weatherInfo.size > 0) {
            mAdapter.setData(mWeatherCache.weatherInfo)
        }
    }

    private fun handleSearchClick() {
        mWeatherCache.city = mInputText
        mSharedPreferenceManager.saveLastSearch(mInputText)
        if (mInputText.length >= AVAILABLE_SEARCH_LENGTH) {
            // clear old data
            mAdapter.clearData()
            mWeatherCache.weatherInfo.clear()
            mViewModel.queryWeather(mInputText, NUM_OF_DAYS)
        } else {
            Toast.makeText(this, getString(R.string.invalid_input), Toast.LENGTH_LONG).show()
        }
    }

    private fun updateEditText() {
        etSearch.text = Editable.Factory.getInstance().newEditable(mInputText)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(KEY_WEATHER, mWeatherCache)
        super.onSaveInstanceState(outState)
    }
}