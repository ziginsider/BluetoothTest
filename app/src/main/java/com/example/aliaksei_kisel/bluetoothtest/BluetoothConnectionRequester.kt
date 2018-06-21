package com.example.aliaksei_kisel.bluetoothtest

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothProfile
import android.content.Context

class BluetoothConnectionRequester(private val callback: CallbackReceiver)
    : BluetoothProfile.ServiceListener {

    override fun onServiceDisconnected(i: Int) {

    }

    override fun onServiceConnected(i: Int, bluetoothProfile: BluetoothProfile?) {

    }

    fun request(context: Context, adapter: BluetoothAdapter) {
        adapter.getProfileProxy(context, this, BluetoothProfile.HEADSET)
    }


}