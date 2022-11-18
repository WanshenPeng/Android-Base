package com.example.myapplicationkotlin.wifitest

import android.Manifest
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.NetworkInfo
import android.net.wifi.p2p.WifiP2pDeviceList
import android.net.wifi.p2p.WifiP2pManager
import android.util.Log
import androidx.core.app.ActivityCompat

class WifiBroadcastReceiver(
    val manager: WifiP2pManager,
    val channel: WifiP2pManager.Channel,
    val activity: WifiTestActivity
) : BroadcastReceiver() {
    val TAG = "WifiBroadcastReceiver"
    override fun onReceive(context: Context?, intent: Intent?) {
        val action: String? = intent?.action
        when (action) {
            // 检查WLAN P2P是否开启并受支持
            WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION -> {
                Log.i(TAG, "WIFI_P2P_STATE_CHANGED_ACTION")
                val state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1)
                when (state) {
                    WifiP2pManager.WIFI_P2P_STATE_ENABLED -> {
                        Log.i(TAG, "WIFI_P2P_STATE_ENABLED")
                    }
                    else -> {
                        Log.i(TAG, "WIFI_P2P_STATE_ENABLED not")
                    }
                }
            }

            // 发现进程成功并检测到对等设备，则系统会发送此广播
            WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION -> {
                Log.i(TAG, "WIFI_P2P_PEERS_CHANGED_ACTION")
                if (ActivityCompat.checkSelfPermission(
                        context!!,
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
                manager.requestPeers(channel, activity)
            }
            WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION -> {
                Log.i(TAG, "WIFI_P2P_CONNECTION_CHANGED_ACTION")
//                val networkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO, NetworkInfo::class.java)
//                if (networkInfo != null) {
//                    if (networkInfo.isConnected){
//                        manager.requestConnectionInfo(channel, activity)
//                    }
//                }
                manager.requestConnectionInfo(channel, activity)
            }

            WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION -> {
                Log.i(TAG, "WIFI_P2P_THIS_DEVICE_CHANGED_ACTION")
            }
        }
    }
}
