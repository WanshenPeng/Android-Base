package com.example.myapplication.pie

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View

class PieChartView(context: Context, attributes: AttributeSet) : View(context, attributes) {
    var paint: Paint = Paint().apply {
        flags = Paint.ANTI_ALIAS_FLAG // 设置抗锯齿，会更消耗资源
        style = Paint.Style.FILL // FILL表示只绘制内容，不绘制轮廓；STROKE表示只绘制轮廓，不绘制内容
        color = Color.WHITE
    }
    lateinit var pieDataList: List<PieData>
    var mStarAngle: Float = 0f // 初始角度，默认为0

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (pieDataList.isNullOrEmpty()) {
            return
        }
        var currentAngle = mStarAngle
        for (pieData in pieDataList) {
            paint.color = pieData.color
            canvas?.drawArc(
                0f, 0f, width.toFloat(), height.toFloat(),
                currentAngle, pieData.angle, true, paint
            )
            currentAngle += pieData.angle
        }
    }

    fun setData(dataList: List<PieData>) {
        if (dataList.isNullOrEmpty()) {
            return
        }
        pieDataList = dataList
        // 触发onDraw()
        invalidate()
    }
}
