package com.example.emgbluetooth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class ControlActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_control)

        val deviceName: String? = intent.getStringExtra("EXTRA_NAME")
        val deviceAddress: String? = intent.getStringExtra("EXTRA_ADDRESS")

        val tvdeviceName = findViewById<TextView>(R.id.tv_devicename)
        val tvdeviceAddress = findViewById<TextView>(R.id.tv_deviceaddress)

        tvdeviceName.text = deviceName
        tvdeviceAddress.text = deviceAddress
    }
}