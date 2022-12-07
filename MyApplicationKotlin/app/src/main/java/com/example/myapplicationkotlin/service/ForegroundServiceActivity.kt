package com.example.myapplicationkotlin.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.app.NotificationCompat
import com.example.myapplicationkotlin.R
import kotlin.random.Random

class ForegroundServiceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_foreground_service)

        findViewById<Button>(R.id.start).setOnClickListener {
            startMyService()
        }
    }

    fun startMyService(){
        val intent = Intent(this, MyForegroundService::class.java)
        startForegroundService(intent)
    }
    fun createNotification() {
        val notificationManager = createNotificationChannel()

        val pendingIntent: PendingIntent =
            Intent(this, ForegroundServiceActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(this, 0, notificationIntent, 0)
            }

        val builder = NotificationCompat.Builder(this, "1234")
            .setSmallIcon(R.drawable.test)
            .setContentTitle("My notification")
            .setContentText("Much longer text that cannot fit one line...")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("Much longer text that cannot fit one line...aaaaaaaaaaabbbbbbbbb"))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true) // 点击通知后自动移除通知

        val id = Random.nextInt(0, Int.MAX_VALUE)
        notificationManager.notify(id, builder.build())
    }

    private fun createNotificationChannel(): NotificationManager {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library

        // Register the channel with the system
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "MyApplicationKotlin"
            val descriptionText = "description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channelId = "1234"
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }

            notificationManager.createNotificationChannel(channel)
        }
        return notificationManager
    }

}