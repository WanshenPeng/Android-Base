package com.example.myapplicationkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
private const val TAG = "BaseWidgetActivity"
class BaseWidgetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_widget)
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            Log.i(TAG, "pressed button")
        }
    }
}