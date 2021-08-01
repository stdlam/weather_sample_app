package com.android.nldlam.sample.weatherapp.utils

import android.widget.Button

fun Button.disableButton() {
    this.isEnabled = false
    this.isClickable = false
}

fun Button.enableButton() {
    this.isEnabled = true
    this.isClickable = true
}