package com.example.aliaksei_kisel.bluetoothtest

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothHeadset
import android.bluetooth.BluetoothProfile
import android.content.Context

class BluetoothConnectionRequester(private val callback: CallbackProxy)
    : BluetoothProfile.ServiceListener {

    override fun onServiceDisconnected(i: Int) {
        //TODO disconnected
    }

    override fun onServiceConnected(i: Int, bluetoothProfile: BluetoothProfile?) {
        callback.onHeadSetProxyReceived(bluetoothProfile as BluetoothHeadset)
    }

    fun request(context: Context, adapter: BluetoothAdapter) {
        adapter.getProfileProxy(context, this, BluetoothProfile.HEADSET)
    }
}
