package com.example.myapplicationkotlin.nested

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.myapplicationkotlin.R

class NestedScrollView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nested_scroll_view)

        val recycler1 = findViewById<RecyclerView>(R.id.recycler_view1)
        val recycler2 = findViewById<RecyclerView>(R.id.recycler_view2)
        val layoutManager = LinearLayoutManager(this)
        val layoutManager2 = LinearLayoutManager(this)
        recycler1.layoutManager = layoutManager
        recycler2 .layoutManager = layoutManager2
        val adapter1 = Adapter1(R.layout.item1)
        val adapter2 = Adapter2(R.layout.item2)
        recycler1.adapter = adapter1
        recycler2.adapter = adapter2
        val list1 = mutableListOf<String>("1", "2", "3", "4","5","6","7","8","9","10","1", "2", "3", "4","5","6","7","8","9","10")
        val list2 = mutableListOf<String>("a", "b", "c","d","e","f","g","h","i","j","k","a", "b", "c","d","e","f","g","h","i","j","k")
        adapter1.addData(list1)
        adapter2.addData(list2)
        recycler1.isNestedScrollingEnabled = false
        recycler2.isNestedScrollingEnabled = false

        val swipeRefreshLayout : SwipeRefreshLayout = findViewById(R.id.swipe_layout)
        swipeRefreshLayout.setColorSchemeResources(R.color.color_00c389)
        swipeRefreshLayout.setOnRefreshListener {
            Log.i("TAG", "recycleView refresh")
            list1.clear()
            list2.clear()
            list1.addAll(listOf("a", "b", "c","d","e","f","g","h","i","j","k","a", "b", "c","d","e","f","g","h","i","j","k"))
            list2.addAll(listOf("1", "2", "3", "4","5","6","7","8","9","10","1", "2", "3", "4","5","6","7","8","9","10"))
            adapter1.notifyDataSetChanged()
            adapter2.notifyDataSetChanged()

            swipeRefreshLayout.isRefreshing = false
        }
    }
}