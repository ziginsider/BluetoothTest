package com.example.aliaksei_kisel.bluetoothtest

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.example.aliaksei_kisel.bluetoothtest.utils.toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var bluetoothAdapter: BluetoothAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonStartConnect.setOnClickListener {
            outputTextView.append("Starting connect...")
            startServer()
        }

        if (!checkPermission()) {
            requestPermission()
        } else {
            checkBluetooth()
        }
    }

    private fun checkBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter == null) {
            outputTextView.append("No bluetooth device :(")
            buttonStartConnect.isEnabled = false
        }
    }

    private fun startServer() {

    }

    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) ==
                PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.BLUETOOTH),
                REQUEST_PERMISSION_BLUETOOTH)
        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.BLUETOOTH_ADMIN),
                REQUEST_PERMISSION_BLUETOOTH_ADMIN)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_PERMISSION_BLUETOOTH -> {
                if (grantResults.isNotEmpty()
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    toast("Permission Bluetooth granted")
                    //TODO start work
                } else {
                    toast("Permission denied. Unfortunately the app won't work correctly")
                }
            }
            REQUEST_PERMISSION_BLUETOOTH_ADMIN -> {
                if (grantResults.isNotEmpty()
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    toast("Permission Bluetooth admin granted")
                    checkBluetooth()
                } else {
                    toast("Permission denied. Unfortunately the app won't work correctly")
                }
            }
        }
    }

    companion object {

        const val REQUEST_PERMISSION_BLUETOOTH = 33
        const val REQUEST_PERMISSION_BLUETOOTH_ADMIN = 34
    }
}
