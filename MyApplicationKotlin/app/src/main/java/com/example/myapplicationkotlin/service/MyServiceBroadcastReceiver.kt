package com.example.myapplicationkotlin.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

/**
 * Author: Wanshenpeng
 * Date: 2022/11/21 10:48
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * Wanshenpeng 2022/11/21 1.0 首次创建
 */
class MyServiceBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            "result_msg" -> {
                val result = intent.getStringExtra("result").toString()
                Log.i("MyServiceBroadcastReceiver", result)
                processResult.getProcessResult(result)
            }
        }
    }

    private lateinit var processResult: ProcessResult
    fun setProcessResult(processResult: ProcessResult) {
        this.processResult = processResult
    }

    interface ProcessResult {
        fun getProcessResult(result: String)
    }


}