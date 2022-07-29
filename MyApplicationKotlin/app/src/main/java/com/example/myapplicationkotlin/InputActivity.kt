package com.example.myapplicationkotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText

private const val TAG = "InputActivity"
class InputActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input)
        Log.i(TAG, "InputActivity create")
    }

    fun confirm(view: View){
        val name = findViewById<EditText>(R.id.edt_name).text.toString()
        val age = findViewById<EditText>(R.id.edt_age).text.toString()
        val intent = Intent().putExtra(NAME, name).putExtra(AGE, age)
        Log.i(TAG, "finish InputActivity")
        setResult(RESULT_OK, intent)
        finish()
    }
}