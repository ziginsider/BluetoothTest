package com.example.aliaksei_kisel.bluetoothtest

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothHeadset
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.example.aliaksei_kisel.bluetoothtest.utils.loge
import com.example.aliaksei_kisel.bluetoothtest.utils.logi
import com.example.aliaksei_kisel.bluetoothtest.utils.toast
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.util.*

class MainActivity : AppCompatActivity(), CallbackProxy, CallbackReceiver {

    var bluetoothAdapter: BluetoothAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonStartConnect.setOnClickListener {
            outputTextView.append("Starting connect...\n")
            startConnect()
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
            outputTextView.append("No bluetooth device :(\n")
            buttonStartConnect.isEnabled = false
        }
    }

    private fun startConnect() {
        if (bluetoothAdapter?.isEnabled!!) {
            onBluetoothConnected()
            return
        }

        if (bluetoothAdapter?.enable()!!) {
            BluetoothBroadcastReceiver.register(this, this)
        } else {
            loge(TAG, "[ Unable to enable Bluetooth ]")
        }
    }

    override fun onHeadSetProxyReceived(proxy: BluetoothHeadset) {
        val connect = getConnectMethod()
        val device = findBondedDeviceByName(bluetoothAdapter, DEVICE_NAME)

        if (connect == null || device == null) {
            logi(TAG, "[ No connect method or no device found ]")
            return
        }

        outputTextView.append("Connected device with name = ${device.name}" +
                " and address = ${device.address} ")

        try {
            connect.isAccessible = true
            connect.invoke(proxy, device)
        } catch (e: InvocationTargetException) {
            loge(TAG, "Unable to invoke connect(BluetoothDevice) method on proxy.")
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            loge(TAG, "Illegal Access!")
            e.printStackTrace()
        }
    }

    private fun findBondedDeviceByName(adapter: BluetoothAdapter?, deviceName: String)
            : BluetoothDevice? {
        for (device in getBoundedDevices(adapter)) {
            logi(TAG, "[ device_name = ${device.name}]")
            if (deviceName == device.name) {
                logi(TAG,
                        "[ Found device with name = ${device.name} and address = ${device.address} ]")
                return device
            }
        }
        logi(TAG, "Unable to find device with name = $deviceName")
        return null
    }

    private fun getBoundedDevices(adapter: BluetoothAdapter?): Set<BluetoothDevice> {
        var results = adapter?.bondedDevices
        if (results == null) {
            results = HashSet<BluetoothDevice>()
        }
        return results
    }

    private fun getConnectMethod(): Method? {
        try {
            return BluetoothHeadset::class.java.getDeclaredMethod("connect",
                    BluetoothDevice::class.java)
        } catch (e: NoSuchMethodException) {
            loge(TAG, "Unable to find connect(BluetoothDevice) method in BluetoothA2dp proxy.")
            e.printStackTrace()
            return null
        }
    }

    override fun onBluetoothConnected() {
        bluetoothAdapter?.let {
            BluetoothConnectionRequester(this).request(this, it)
        }
    }

    override fun onBluetoothError() {
        loge(TAG, "There was an error enabling the Bluetooth Adapter.")
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

        const val TAG = "BluetoothTest"
        const val REQUEST_PERMISSION_BLUETOOTH = 33
        const val REQUEST_PERMISSION_BLUETOOTH_ADMIN = 34
        const val NAME = "BluetoothTest"
        //val APP_UUID = UUID.fromString(NAME)
        const val DEVICE_NAME = "SBH20"
    }
}
