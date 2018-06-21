package com.example.aliaksei_kisel.bluetoothtest

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.example.aliaksei_kisel.bluetoothtest.utils.loge

class BluetoothBroadcastReceiver(private val callback: Callback) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        if (BluetoothAdapter.ACTION_STATE_CHANGED != intent?.action) {
            loge(TAG, "Received irrelevant broadcast. Disregarding.")
            return
        }

        val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)

        when (state) {
            BluetoothAdapter.STATE_CONNECTED -> {
                unregisterReceiver(context, this)
                onBluetoothConnected()
            }
            BluetoothAdapter.ERROR -> {
                unregisterReceiver(context, this)
                onBluetoothError()
            }
        }
    }

    private fun onBluetoothError() {
        callback.onBluetoothError()
    }

    private fun onBluetoothConnected() {
        callback.onBluetoothConnected()
    }

    private fun unregisterReceiver(context: Context?, receiver: BluetoothBroadcastReceiver) {
        try {
            context?.unregisterReceiver(receiver)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            loge(TAG, "Tried to unregister BluetoothBroadcastReceiver that was not registered.")
        }
    }

    companion object {

        const val TAG = "BluetoothBroadcastReceiver"

        fun register(callback: Callback, context: Context) {
            context.registerReceiver(BluetoothBroadcastReceiver(callback), getFilter())
        }

        private fun getFilter() = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
    }

}