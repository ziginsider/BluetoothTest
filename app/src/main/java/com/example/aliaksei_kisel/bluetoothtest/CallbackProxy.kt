package com.example.aliaksei_kisel.bluetoothtest

import android.bluetooth.BluetoothHeadset

interface CallbackProxy {

    fun onHeadSetProxyReceived(proxy: BluetoothHeadset)
}