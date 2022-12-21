package com.example.myapplicationkotlin.view2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.myapplicationkotlin.R
import com.example.myapplicationkotlin.SystemBarHelper
import kotlinx.android.synthetic.main.activity_view2.*

class View2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view2)
        SystemBarHelper.immersiveStatusBar(window, 0.0.toFloat())
        SystemBarHelper.setStatusBarDarkMode(this, true)
        SystemBarHelper.setHeightAndPadding(this, ll_status)

        text_entrance_view.setOnClickListener {
            Toast.makeText(this, "text_entrance_view", Toast.LENGTH_SHORT).show()
        }
    }
}