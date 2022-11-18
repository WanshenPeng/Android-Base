package com.example.myapplicationkotlin.view

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.myapplicationkotlin.view.DiscountView
import com.example.myapplication.pie.PieChartView
import com.example.myapplication.pie.PieData
import com.example.myapplicationkotlin.R

class MyViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_view)

        val values = listOf(20, 30, 40, 50)
        val total = values.sumOf { it }
        Log.i("asd", total.toString())
        val pieDataList = listOf<PieData>(
            PieData(values[0]/total.toFloat() * 360, Color.RED),
            PieData(values[1]/total.toFloat() * 360, Color.GREEN),
            PieData(values[2]/total.toFloat() * 360, Color.BLUE),
            PieData(values[3]/total.toFloat() * 360, Color.GRAY)
        )
        val pieChartView = findViewById<PieChartView>(R.id.pie_chart_view)
        pieChartView.setData(pieDataList)


        val discountView = findViewById<DiscountView>(R.id.discount_view)
        discountView.discountNumber = "8折"
        discountView.setTextProduct("商品")
        discountView.setTextPrice("￥123")
    }
}