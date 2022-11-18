package com.example.myapplicationkotlin

import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso

private const val TAG = "BaseWidgetActivity"

class BaseWidgetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_widget)
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            Log.i(TAG, "pressed button")
        }

        val imageView: ImageView = findViewById(R.id.imageView)
//        Picasso.get().load("https://image.vesync.com/arize/device/icon_aes220_online.png")
//            .into(imageView);
        Glide.with(this).load("https://image.vesync.com/arize/device/icon_hub.png")
            .into(imageView);
    }
}