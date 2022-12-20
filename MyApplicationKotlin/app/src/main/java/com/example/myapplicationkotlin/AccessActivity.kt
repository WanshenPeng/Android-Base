package com.example.myapplicationkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

private const val TAG = "AccessActivity"

class AccessActivity : AppCompatActivity() {
    private val itemList = ArrayList<AccessData>()
    private var n = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_access)

        val toolbar = findViewById<Toolbar>(R.id.access_toolbar)
        toolbar.findViewById<TextView>(R.id.toolbar_title).setText("Access")

        initItem()
        val layoutManager = LinearLayoutManager(this)
        val mRecyclerView: RecyclerView = findViewById(R.id.recycler_view)
        mRecyclerView.layoutManager = layoutManager
        val adapter = AccessAdapter(itemList)
        mRecyclerView.adapter = adapter

        val swipRefreshLayout : SwipeRefreshLayout = findViewById(R.id.swip_refresh_layout)
        swipRefreshLayout.setColorSchemeResources(R.color.color_00c389)
        swipRefreshLayout.setOnRefreshListener {
            Log.i(TAG, "recycleView refresh")
            updateOperation(mRecyclerView, adapter)
            swipRefreshLayout.isRefreshing = false
        }
    }

    private fun updateOperation(mRecyclerView: RecyclerView, adapter: AccessAdapter){
        this.n += 5
        for (i in this.n..(this.n + 4)) {
            itemList.add(0, AccessData("Title $i", "Content $i", "Time $i", R.drawable.icon_tem))
        }

        adapter.notifyItemRangeInserted(0, 5)
        //mRecyclerView.adapter = adapter
    }

    private fun initItem() {
        for (i in this.n..(this.n + 4)) {
            itemList.add(AccessData("Title $i", "Content $i", "Time $i", R.drawable.icon_tem))
        }
    }

}