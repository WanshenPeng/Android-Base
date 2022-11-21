package com.example.myapplicationkotlin.service

import android.app.IntentService
import android.app.Service
import android.content.Intent
import android.os.*
import android.os.Process
import android.util.Log
import android.widget.Toast

/**
 * Author: Wanshenpeng
 * Date: 2022/11/18 16:35
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * Wanshenpeng 2022/11/18 1.0 首次创建
 */
class MyService : Service() {
    private var serviceLooper: Looper? = null
    private var serviceHandler: ServiceHandler? = null
    var process = 0

    private inner class ServiceHandler(looper: Looper) : Handler(looper) {
        override fun handleMessage(msg: Message) {
            try {
                Log.i("service", "start")
                val number = msg.data.getString("number")
                while (process < number!!.toInt()) {
                    Log.i("service", "start process $process")
                    Thread.sleep(1500) //1.5s处理一次
                    Log.i("service", "end process $process")
                    process += 1
                }
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
            }
            Log.i("service", "stop")
            stopSelf(msg.arg1)
        }
    }

    override fun onCreate() {
        HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND).apply {
            start()

            // Get the HandlerThread's Looper and use it for our Handler
            serviceLooper = looper
            serviceHandler = ServiceHandler(looper)
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show()

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        serviceHandler?.obtainMessage()?.also { msg ->
            msg.arg1 = startId
            msg.data = Bundle().apply { putString("number", intent.getStringExtra("number")) }
            serviceHandler?.sendMessage(msg)
        }

        // If we get killed, after returning from here, restart
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show()
        val intent = Intent().apply {
            action = "result_msg"
            putExtra("result", process.toString())
        }
        sendBroadcast(intent)
        super.onDestroy()
    }
}