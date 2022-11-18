package com.example.myapplicationkotlin.wifitest

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.aware.WifiAwareManager
import android.net.wifi.p2p.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.myapplicationkotlin.R
import java.net.InetAddress
import java.time.LocalTime

class WifiTestActivity : AppCompatActivity(), WifiP2pManager.PeerListListener,
    WifiP2pManager.ConnectionInfoListener {
    private val TAG = "WifiTestActivity"
    val manager: WifiP2pManager? by lazy(LazyThreadSafetyMode.NONE) {
        getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager?
    }
    var mChannel: WifiP2pManager.Channel? = null
    var receiver: BroadcastReceiver? = null
    val intentFilter = IntentFilter().apply {
        addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)
    }

    lateinit var device: WifiP2pDevice
    val config = WifiP2pConfig()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wifi_test)
        Log.i(TAG, "onCreate")
        mChannel = manager?.initialize(this, mainLooper, null)
        mChannel?.also { channel ->
            receiver = manager?.let { WifiBroadcastReceiver(it, channel, this) }
        }

        findViewById<Button>(R.id.discover).setOnClickListener {
            discoverPeers()
        }
        findViewById<Button>(R.id.connect).setOnClickListener {
            Log.i(TAG, "start connect")
            connect()
        }
        findViewById<Button>(R.id.cancel).setOnClickListener {
            Log.i(TAG, "start removeGroup")
            removeGroup()
        }
        findViewById<Button>(R.id.show_connection_info).setOnClickListener {
            showConnectionInfo()
        }
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume")
        receiver.also { receiver ->
            registerReceiver(receiver, intentFilter)
        }
        discoverPeers()
    }

    private fun discoverPeers() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
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
        manager?.discoverPeers(mChannel, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                Log.i(TAG, "discoverPeers success")
            }

            override fun onFailure(p0: Int) {
                Log.i(TAG, "discoverPeers failure")
            }
        })
    }

    override fun onPeersAvailable(p0: WifiP2pDeviceList?) {
        if (p0?.deviceList != null) {
            Log.i(TAG, "find ${p0.deviceList.size} device")
            for (i in p0.deviceList) {
                Log.i(TAG, "${i.deviceName} : ${i.deviceAddress}")
                if (i.deviceName == "OnePlus 7 Pro" || i.deviceName == "Motorola_5c62") {
                    device = i
                }
            }
        }
    }

    private fun connect() {
        config.deviceAddress = device.deviceAddress
        mChannel?.also { channel ->
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
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
            manager?.connect(channel, config, object : WifiP2pManager.ActionListener {
                override fun onSuccess() {
                    Toast.makeText(
                        this@WifiTestActivity,
                        "connect ${device.deviceName} success",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.i(TAG, "connect ${device.deviceName} success")
                }

                override fun onFailure(p0: Int) {
                    Toast.makeText(this@WifiTestActivity, "connect failure", Toast.LENGTH_SHORT)
                        .show()
                    Log.i(TAG, "connect failure")
                }
            })
        }
    }

    private fun removeGroup() {
        mChannel?.also { channel ->

            manager?.removeGroup(channel, object : WifiP2pManager.ActionListener {
                override fun onSuccess() {
                    Toast.makeText(this@WifiTestActivity, "cancel success", Toast.LENGTH_SHORT)
                        .show()
                    Log.i(TAG, "cancel success")
                }

                override fun onFailure(p0: Int) {
                    Toast.makeText(this@WifiTestActivity, "cancel failure", Toast.LENGTH_SHORT)
                        .show()
                    Log.i(TAG, "cancel failure $p0")
                }

            })
        }
    }

    fun showConnectionInfo() {
        mChannel?.also { channel ->
            manager?.requestConnectionInfo(channel, this)
        }
    }

    override fun onConnectionInfoAvailable(p0: WifiP2pInfo?) {
//        val groupOwnerAddress = p0?.groupOwnerAddress
//        Log.i(TAG, "groupOwnerAddress: ${groupOwnerAddress.toString()}")
        Log.i(TAG, "onConnectionInfoAvailable")
        Log.i(TAG, p0.toString())
    }

    override fun onPause() {
        super.onPause()
        receiver.also { receiver ->
            unregisterReceiver(receiver)
        }
        manager?.stopPeerDiscovery(mChannel, null);
    }


    override fun onStart() {
        super.onStart()
        Log.i(TAG, "onStart")
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy")
    }


}