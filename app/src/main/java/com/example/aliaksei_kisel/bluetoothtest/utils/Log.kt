package com.example.aliaksei_kisel.bluetoothtest.utils

import android.util.Log
import com.example.aliaksei_kisel.bluetoothtest.BuildConfig

/**
 * Extension fun for logging info
 */
fun logi(className: String, message: String) {
    if (BuildConfig.DEBUG) Log.i(className, message)
}

/**
 * Extension fun for logging errors
 */
fun loge(className: String, message: String) {
    Log.e(className, message)
}

/**
 * Extension fun for logging verbose
 */
fun logv(className: String, message: String) {
    Log.v(className, message)
}

