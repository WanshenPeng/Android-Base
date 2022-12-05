package com.example.myapplication.handler

import android.os.Bundle
import android.os.AsyncTask
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplicationkotlin.R
import java.lang.ref.WeakReference


class AsyncTaskActivity : AppCompatActivity() {
    lateinit var textView: TextView
    lateinit var btnDoAsync: Button
    lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_async_task)

        textView = findViewById(R.id.textView)
        btnDoAsync = findViewById(R.id.btnDoAsync)
        progressBar = findViewById(R.id.progressBar)

        btnDoAsync.setOnClickListener {
            val task = MyAsyncTask(this)
            task.execute(10)
        }
    }

    companion object {
        class MyAsyncTask internal constructor(context: AsyncTaskActivity) :
            AsyncTask<Int, String, String?>() {

            private var resp: String? = null
            private val activityReference: WeakReference<AsyncTaskActivity> = WeakReference(context)

            override fun onPreExecute() {
                Log.i("AsyncTask", "onPreExecute")
                val activity = activityReference.get()
                if (activity == null || activity.isFinishing) return
                activity.progressBar.visibility = View.VISIBLE
            }

            override fun doInBackground(vararg params: Int?): String? {
                Log.i("AsyncTask", "doInBackground")
                publishProgress("Sleeping Started") // Calls onProgressUpdate()
                try {
                    val time = params[0]?.times(1000)
                    time?.toLong()?.let { Thread.sleep(it / 2) }
                    publishProgress("Half Time") // Calls onProgressUpdate()
                    time?.toLong()?.let { Thread.sleep(it / 2) }
                    publishProgress("Sleeping Over") // Calls onProgressUpdate()
                    resp = "Android was sleeping for " + params[0] + " seconds"
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                    resp = e.message
                } catch (e: Exception) {
                    e.printStackTrace()
                    resp = e.message
                }

                return resp
            }


            override fun onPostExecute(result: String?) {
                Log.i("AsyncTask", "onPostExecute")
                val activity = activityReference.get()
                if (activity == null || activity.isFinishing) return
                activity.progressBar.visibility = View.GONE
                activity.textView.text = result.let { it }
            }

            override fun onProgressUpdate(vararg text: String?) {
                Log.i("AsyncTask", "onProgressUpdate")
                val activity = activityReference.get()
                if (activity == null || activity.isFinishing) return

                Toast.makeText(activity, text.firstOrNull(), Toast.LENGTH_SHORT).show()

            }
        }
    }
}