package com.example.myapplicationkotlin

import android.app.Application
import android.util.Log


private const val TAG = "NewApplication"

class NewApplication : Application() {

    companion object {
        var instances: Application? = null
        fun getInstance() = instances
    }


    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "NewApplication Start")
        instances = this
        // 监听应用内所有Activity生命周期，registActivityLife
        //registerActivityLifecycleCallbacks()
    }


}