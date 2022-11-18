package com.example.myapplicationkotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import java.io.Serializable
import java.time.LocalTime

private const val TAG = "InputActivity"

class InputActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input)
        Log.i(TAG, "InputActivity create ")
    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG, "InputActivity start ")
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "InputActivity resume ")
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "InputActivity pause ")
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "InputActivity stop ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "InputActivity destroy ")
    }
    
    fun confirm(view: View) {
        val name = findViewById<EditText>(R.id.edt_name).text.toString()
        val age = findViewById<EditText>(R.id.edt_age).text.toString()
        val person = Person(name, age)

        val intent = Intent().putExtra(PERSON, person as Serializable)
        // val intent = Intent().putExtra(NAME, name).putExtra(AGE, age)

        Log.i(TAG, "finish InputActivity ")
        setResult(RESULT_OK, intent)
        finish()
    }
}