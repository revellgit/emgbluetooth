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
import android.util.Log
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        setContentView(R.layout.activity_main)

        // Button click listener to start scan for devices activity

        val scanbtn = findViewById<Button>(R.id.scanbtn)

        scanbtn.setOnClickListener {
            val intent = Intent(this, ScanBluetooth::class.java)
            startActivity(intent)
        }

        // Create an instance of the bluetooth adapter

        val bluetoothManager: BluetoothManager = getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter

        // Check if bluetooth is available on the device

        if (bluetoothAdapter == null) {
            Toast.makeText(applicationContext,"Bluetooth not supported",Toast.LENGTH_SHORT).show()
        }

        // Enable/Disable Bluetooth on the device with button press

        val togglebtn = findViewById<Button>(R.id.togglebtn)

        togglebtn.setOnClickListener {
            val REQUEST_ENABLE_BT = 111

            if (bluetoothAdapter?.isEnabled == false) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED
                )
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
            } else {
                bluetoothAdapter?.disable()
                Toast.makeText(applicationContext,"Bluetooth Disabled",Toast.LENGTH_SHORT).show()
            }
        }

        // Show paired devices & Allow one to be chosen from the list

        // Kotlin 101: How to communicate to a Bluetooth device Part 1
        // https://www.youtube.com/watch?v=Oz4CBHrxMMs

        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
        var list: ArrayList<BluetoothDevice> = ArrayList()
        var displayList: ArrayList<String> = ArrayList()

        pairedDevices?.forEach { device ->
            list.add(device)
            displayList.add("${device.name} ${device.address}")
            Log.i("device", "" + device)
        }

        // Populate the list of paired devices in the view
        // set click listener for list items and open a new activity
        // with device properties as extras

        var mListView = findViewById<ListView>(R.id.devicelist)

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, displayList)
        mListView.adapter = adapter

        mListView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val device: BluetoothDevice = list[position]
            val name: String = device.name
            val address: String = device.address
            val uuid = device.uuids

            val intent = Intent(this, ControlActivity::class.java)
            intent.putExtra("EXTRA_NAME", name)
            intent.putExtra("EXTRA_ADDRESS", address)
            intent.putExtra("EXTRA_UUID", uuid)
            startActivity(intent)
        }
    }
}