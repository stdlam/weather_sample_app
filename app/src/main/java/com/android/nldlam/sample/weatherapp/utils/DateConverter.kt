package com.android.nldlam.sample.weatherapp.utils

import java.text.SimpleDateFormat
import java.util.*

fun Date.toMonDayMonthYear(): String {
    val formatter = SimpleDateFormat("EEE, d MMM yyyy")
    return formatter.format(this)
}