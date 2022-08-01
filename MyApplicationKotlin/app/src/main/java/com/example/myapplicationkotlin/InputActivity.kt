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
        val time = LocalTime.now().toString()
        Log.i(TAG, "InputActivity create $time")
    }

    override fun onStart() {
        super.onStart()
        val time = LocalTime.now().toString()
        Log.i(TAG, "InputActivity start $time")
    }

    override fun onResume() {
        super.onResume()
        val time = LocalTime.now().toString()
        Log.i(TAG, "InputActivity resume $time")
    }

    override fun onPause() {
        super.onPause()
        val time = LocalTime.now().toString()
        Log.i(TAG, "InputActivity pause $time")
    }

    override fun onStop() {
        super.onStop()
        val time = LocalTime.now().toString()
        Log.i(TAG, "InputActivity stop $time")
    }

    override fun onDestroy() {
        super.onDestroy()
        val time = LocalTime.now().toString()
        Log.i(TAG, "InputActivity destroy $time")
    }


    fun confirm(view: View) {
        val name = findViewById<EditText>(R.id.edt_name).text.toString()
        val age = findViewById<EditText>(R.id.edt_age).text.toString()
        val person = Person(name, age)

        val intent = Intent().putExtra(PERSON, person as Serializable)
        // val intent = Intent().putExtra(NAME, name).putExtra(AGE, age)

        val time = LocalTime.now().toString()
        Log.i(TAG, "finish InputActivity $time")
        setResult(RESULT_OK, intent)
        finish()
    }
}