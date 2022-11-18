package com.example.myapplicationkotlin.service

import android.app.IntentService
import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message

/**
 * Author: Wanshenpeng
 * Date: 2022/11/18 16:35
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * Wanshenpeng 2022/11/18 1.0 首次创建
 */
class MyService : Service() {
    private inner class ServiceHandler(looper: Looper):Handler(looper){
        override fun handleMessage(msg: Message) {
            try {
                Thread.sleep(5000)
            }catch (e:InterruptedException){
                Thread.currentThread().interrupt()
            }
            stopSelf(msg.arg1)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}