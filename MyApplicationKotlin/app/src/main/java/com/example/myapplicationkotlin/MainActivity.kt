package com.example.myapplicationkotlin

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Display
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import java.time.LocalDateTime
import java.time.LocalTime

const val EXTRA_MESSAGE = "com.example.myapplicationkotlin.MESSAGE"
const val NAME = "com.example.myapplicationkotlin.NAME"
const val AGE = "com.example.myapplicationkotlin.AGE"
const val PERSON = "com.example.myapplicationkotlin.PERSON"
const val GAME_STATE_KEY = "game.state.key"
var gameState: String? = null

private const val TAG = "MainActivity"


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        gameState = savedInstanceState?.getString(GAME_STATE_KEY)
        val time = LocalTime.now().toString()
        Log.i(TAG, "mainActivity create $time")

    }

    override fun onStart() {
        super.onStart()
        val time = LocalTime.now().toString()
        Log.i(TAG, "mainActivity start $time")
    }

    override fun onResume() {
        super.onResume()
        val time = LocalTime.now().toString()
        Log.i(TAG, "mainActivity resume $time")
    }

    override fun onPause() {
        super.onPause()
        val time = LocalTime.now().toString()
        Log.i(TAG, "mainActivity pause $time")
    }

    override fun onStop() {
        super.onStop()
        val time = LocalTime.now().toString()
        Log.i(TAG, "mainActivity stop $time")
    }

    override fun onDestroy() {
        super.onDestroy()
        val time = LocalTime.now().toString()
        Log.i(TAG, "mainActivity destroy $time")
    }

    fun sendMessage(view: View) {
        val editText = findViewById<EditText>(R.id.editTextTextPersonName)
        val message = editText.text.toString()
        val intent = Intent(this, DisplayMessageActivity::class.java).apply {
            putExtra(EXTRA_MESSAGE, message)
        }
        startActivity(intent)
    }

    fun intoInputActivity(view: View) {
        val intent = Intent(this, InputActivity::class.java)
//        startActivityForResult(intent, 1) //该方法已被弃用
        val time = LocalTime.now().toString()
        Log.i(TAG, "start inputActivity from mainActivity $time")
        resultLauncher.launch(intent)
    }

    val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                val person = data?.extras?.get(PERSON) as Person
                val name = person.name
                val age = person.age
//                val name = data?.getStringExtra(NAME)
//                val age = data?.getStringExtra(AGE)
                val nameTextView = findViewById<TextView>(R.id.name).setText(name)
                val ageTextView = findViewById<TextView>(R.id.age).setText(age)
            }
        }

    override fun onSaveInstanceState(outState: Bundle) {
        val name = findViewById<TextView>(R.id.name).text.toString()
        val age = findViewById<TextView>(R.id.age).text.toString()
        outState.run {
            putString(GAME_STATE_KEY, gameState)
            putString(NAME, name)
            putString(AGE, age)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        findViewById<TextView>(R.id.name).setText(savedInstanceState.getString(NAME))
        findViewById<TextView>(R.id.age).setText(savedInstanceState.getString(AGE))

        // super.onRestoreInstanceState(savedInstanceState)
    }

}
