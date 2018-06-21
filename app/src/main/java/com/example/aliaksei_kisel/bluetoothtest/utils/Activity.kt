package com.example.aliaksei_kisel.bluetoothtest.utils

import android.app.Activity
import android.widget.Toast

/**
 * Extension show Toast for Activity.
 */
fun Activity.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}