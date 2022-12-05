package com.example.myapplication.handler

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplicationkotlin.R
import java.lang.ref.WeakReference


class MyHandlerActivity : AppCompatActivity() {
    private lateinit var tvName: TextView
    val WHAT = 100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_handler)
        tvName = findViewById(R.id.tv_name)
        Thread {
            mHandler.sendMessageDelayed(Message.obtain().apply {
                what = WHAT
                obj = "hello world"
            }, 2000)
        }.start()
    }

    private val mHandler = MyHandler(WeakReference(this))

    private class MyHandler(val wrActivity: WeakReference<MyHandlerActivity>) : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            wrActivity.get()?.run {
                when (msg.what) {
                    WHAT -> tvName.text = msg.obj as String
                }
            }
        }
    }
}



