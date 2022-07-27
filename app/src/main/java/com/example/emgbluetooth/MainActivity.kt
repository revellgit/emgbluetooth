package com.example.emgbluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.ParcelUuid
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import java.util.*

class MainActivity : AppCompatActivity() {
    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        val scanbtn = findViewById<Button>(R.id.scanbtn)

        scanbtn.setOnClickListener {
            val intent = Intent(this, ScanBluetooth::class.java)
            startActivity(intent)
        }

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

        val togglebtn = findViewById<Button>(R.id.togglebtn)

        togglebtn.setOnClickListener {
            val REQUEST_ENABLE_BT = 111

            if (bluetoothAdapter?.isEnabled == false) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED
                ) /*{
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }*/
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
            } else {
                bluetoothAdapter?.disable()
                Toast.makeText(applicationContext,"Bluetooth Disabled",Toast.LENGTH_SHORT).show()
            }
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

            devices += deviceName + " " + deviceHardwareAddress.toString()

            // devices += deviceHardwareAddress.toString()
        }

        val arrayAdapter: ArrayAdapter<*>
        val users = arrayOf(
            "Virat Kohli", "Rohit Sharma", "Steve Smith",
            "Kane Williamson", "Ross Taylor"
        )

        // Access the listView from the xml file
        var mListView = findViewById<ListView>(R.id.devicelist)
        arrayAdapter = ArrayAdapter(this,
            android.R.layout.simple_list_item_1, devices)
        mListView.adapter = arrayAdapter

    }
}