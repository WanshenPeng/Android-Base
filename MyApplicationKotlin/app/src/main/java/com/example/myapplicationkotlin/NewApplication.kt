package com.example.myapplicationkotlin

import android.app.Application
import android.util.Log


private const val TAG = "NewApplication"
class NewApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "NewApplication Start")
    }

}