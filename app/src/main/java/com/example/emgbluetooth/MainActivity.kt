package com.example.emgbluetooth

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // TUTORIALS :
        // https://developer.android.com/guide/topics/connectivity/bluetooth/setup
        // Android Bluetooth Connectivity Tutorial - Playlist
        // https://www.youtube.com/playlist?list=PLFh8wpMiEi8_I3ujcYY3-OaaYyLudI_qi

        // Create an instance of the bluetooth adapter
        val bluetoothManager: BluetoothManager = getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.getAdapter()

        // Check if bluetooth is available on the device
        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
        }

        // Enable Bluetooth on the device
        // Ask the user for permission if not enabled
        val REQUEST_ENABLE_BT = 111

        if (bluetoothAdapter?.isEnabled == false) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        }

        // Find and Connect devices
        // https://developer.android.com/guide/topics/connectivity/bluetooth/find-bluetooth-devices

        // Show paired devices

        var devices: Array<String> = arrayOf()
        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
        pairedDevices?.forEach { device ->
            val deviceName = device.name
            val deviceHardwareAddress = device.address // MAC address
            val deviceUUID = device.uuids
            devices += deviceName
        }

        val arrayAdapter: ArrayAdapter<*>
        val users = arrayOf(
            "Virat Kohli", "Rohit Sharma", "Steve Smith",
            "Kane Williamson", "Ross Taylor"
        )

        // access the listView from xml file
        var mListView = findViewById<ListView>(R.id.devicelist)
        arrayAdapter = ArrayAdapter(this,
            android.R.layout.simple_list_item_1, devices)
        mListView.adapter = arrayAdapter



    }
}