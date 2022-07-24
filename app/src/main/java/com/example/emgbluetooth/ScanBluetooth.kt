package com.example.emgbluetooth

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.companion.AssociationRequest
import android.companion.BluetoothDeviceFilter
import android.companion.CompanionDeviceManager
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.ParcelUuid
import androidx.annotation.RequiresApi
import java.util.*
import java.util.regex.Pattern

// https://developer.android.com/guide/topics/connectivity/companion-device-pairing#kotlin

private const val SELECT_DEVICE_REQUEST_CODE = 0

@RequiresApi(Build.VERSION_CODES.O)
class ScanBluetooth : AppCompatActivity() {

    private val deviceManager: CompanionDeviceManager by lazy {
        getSystemService(Context.COMPANION_DEVICE_SERVICE) as CompanionDeviceManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_bluetooth)

                // To skip filters based on names and supported feature flags (UUIDs),
                // omit calls to setNamePattern() and addServiceUuid()
                // respectively, as shown in the following  Bluetooth example.

                /* val deviceFilter: BluetoothDeviceFilter = BluetoothDeviceFilter.Builder()
                    .setNamePattern(Pattern.compile("My device"))
                    .addServiceUuid(ParcelUuid(UUID(0x123abcL, -1L)), null)
                    .build() */

                val deviceFilter: BluetoothDeviceFilter = BluetoothDeviceFilter.Builder()
                    .build()

                // The argument provided in setSingleDevice() determines whether a single
                // device name or a list of them appears.
                val pairingRequest: AssociationRequest = AssociationRequest.Builder()
                    .addDeviceFilter(deviceFilter)
                    .setSingleDevice(false)
                    .build()

                // When the app tries to pair with a Bluetooth device, show the
                // corresponding dialog box to the user.
                deviceManager.associate(pairingRequest,
                    object : CompanionDeviceManager.Callback() {

                        override fun onDeviceFound(chooserLauncher: IntentSender) {
                            startIntentSenderForResult(chooserLauncher,
                                SELECT_DEVICE_REQUEST_CODE, null, 0, 0, 0)
                        }

                        override fun onFailure(error: CharSequence?) {
                            // Handle the failure.
                        }
                    }, null)
            }

            @SuppressLint("MissingPermission")
            override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
                when (requestCode) {
                    SELECT_DEVICE_REQUEST_CODE -> when(resultCode) {
                        Activity.RESULT_OK -> {
                            // The user chose to pair the app with a Bluetooth device.
                            val deviceToPair: BluetoothDevice? =
                                data?.getParcelableExtra(CompanionDeviceManager.EXTRA_DEVICE)
                            deviceToPair?.let { device ->
                                device.createBond()
                                // Maintain continuous interaction with a paired device.
                            }
                        }
                    }
                    else -> super.onActivityResult(requestCode, resultCode, data)
                }
            }
        }