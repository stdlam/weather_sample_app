package com.android.nldlam.sample.weatherapp.ui.searchweatherscreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.nldlam.sample.weatherapp.R
import com.android.nldlam.sample.weatherapp.data.model.WeatherInfo
import com.android.nldlam.sample.weatherapp.utils.toMonDayMonthYear
import java.util.*
import kotlin.math.roundToInt

class WeatherAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mData = mutableListOf<WeatherInfo>()

    fun setData(data: MutableList<WeatherInfo>) {
        mData.clear()
        mData.addAll(data)
        notifyDataSetChanged()
    }

    fun clearData() {
        mData.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return WeatherDetailViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_weather_result, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (itemCount > position && position != RecyclerView.NO_POSITION) {
            (holder as WeatherDetailViewHolder).build(mData[position])
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    inner class WeatherDetailViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvDate: TextView = view.findViewById(R.id.tvDate)
        private val tvTemp: TextView = view.findViewById(R.id.tvTemp)
        private val tvPressure: TextView = view.findViewById(R.id.tvPressure)
        private val tvHumidity: TextView = view.findViewById(R.id.tvHumidity)
        private val tvDescription: TextView = view.findViewById(R.id.tvDescription)
        private val context = view.context

        fun build(weather: WeatherInfo) {
            tvDate.text = String.format(context.getString(R.string.result_date), Date(weather.day * 1000L).toMonDayMonthYear())
            tvTemp.text = String.format(context.getString(R.string.result_avg_temp), weather.temp.roundToInt())
            tvPressure.text = String.format(context.getString(R.string.result_pressure), weather.pressure)
            "${String.format(context.getString(R.string.result_humidity), weather.humidity)}%".also { tvHumidity.text = it }
            tvDescription.text = String.format(context.getString(R.string.result_description), weather.description)
        }
    }
}