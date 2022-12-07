package com.example.myapplicationkotlin.service

import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.myapplicationkotlin.R

class MyServiceActivity : AppCompatActivity() {
    val myServiceBroadcastReceiver = MyServiceBroadcastReceiver()
    val filter = IntentFilter().apply {
        addAction("result_msg")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_service)

        findViewById<Button>(R.id.start).setOnClickListener {
            val number = findViewById<EditText>(R.id.number).text.toString()
            startMyService(number)
        }

        findViewById<Button>(R.id.stop).setOnClickListener {
            stopMyService()
        }

        myServiceBroadcastReceiver.setProcessResult(object :MyServiceBroadcastReceiver.ProcessResult{
            override fun getProcessResult(result: String) {
                findViewById<TextView>(R.id.result).text = result
            }
        })

        registerReceiver(myServiceBroadcastReceiver, filter)


        findViewById<Button>(R.id.foreground).setOnClickListener {
            val intent = Intent(this, ForegroundServiceActivity::class.java)
            startActivity(intent)
        }
    }

    private fun startMyService(number: String) {
        val intent = Intent(this, MyService::class.java)
        intent.putExtra("number", number)
        startService(intent)
    }

    private fun stopMyService() {
        val intent = Intent(this, MyService::class.java)
        stopService(intent)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        Log.i("MyServiceActivity", "onPause")
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(myServiceBroadcastReceiver)
        stopMyService()
        Log.i("MyServiceActivity", "onDestroy")
    }
}