package com.example.myapplicationkotlin.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.myapplicationkotlin.MainActivity
import com.example.myapplicationkotlin.R
import kotlin.random.Random

/**
 * Author: Wanshenpeng
 * Date: 2022/12/6 10:03
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * Wanshenpeng 2022/12/6 1.0 首次创建
 */
class MyForegroundService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // If the notification supports a direct reply action, use
        // PendingIntent.FLAG_MUTABLE instead.

        val intent = Intent(this, MainActivity::class.java).apply {
            this.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val channelId = "1234"
        val notification: Notification = Notification.Builder(this, channelId)
            .setContentTitle(getText(R.string.notification_title))
            .setContentText(getText(R.string.notification_message))
            .setSmallIcon(R.drawable.test)
            .setContentIntent(pendingIntent)
            .setTicker(getText(R.string.ticker_text))
            .build()

        // Notification ID cannot be 0.
        val id = Random.nextInt(0, Int.MAX_VALUE)
        startForeground(id, notification)

        return START_STICKY
    }
}