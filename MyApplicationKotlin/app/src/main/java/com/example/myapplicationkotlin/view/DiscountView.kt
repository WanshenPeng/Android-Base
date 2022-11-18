package com.example.myapplicationkotlin.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.example.myapplicationkotlin.R

class DiscountView(context: Context, attributeSet: AttributeSet) :
    LinearLayout(context, attributeSet) {
    var discountNumber = ""

    var mRoot: View
    var dataType: TextView
    var price: TextView

    init {
        mRoot = LayoutInflater.from(context).inflate(R.layout.discount_view_layout, this)
        price = mRoot.findViewById(R.id.price)
        dataType = mRoot.findViewById(R.id.product)
        //setWillNotDraw(false) // 绘制ViewGroup
    }

    val paintTag = Paint().apply {
        flags = Paint.ANTI_ALIAS_FLAG
        color = Color.RED
        style = Paint.Style.FILL
    }
    val paintText= Paint().apply {
        flags = Paint.ANTI_ALIAS_FLAG
        color = Color.WHITE
        style = Paint.Style.FILL
        textSize = 35f
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val height = mRoot.height
        val width = mRoot.width

        val path = Path().apply {
            moveTo(0f, height / 4f)
            lineTo(width / 4f, 0f)
            lineTo(width / 2f, 0f)
            lineTo(0f, height / 2f)
            close()
        }
        canvas?.drawPath(path, paintTag)

        //绘制折扣文字
        val hOffset =
            (Math.sqrt((Math.pow(height / 4.0, 2.0) + Math.pow(width / 4.0, 2.0))) / 2.0) - 20f
        val vOffset = (height * width * Math.sqrt((Math.pow(height.toDouble(), 2.0) + Math.pow(width.toDouble(), 2.0)))
                / ((Math.pow(height.toDouble(), 2.0) + Math.pow(width.toDouble(), 2.0))) / 6.0f) + 3f
        canvas?.drawTextOnPath(discountNumber, path,
            hOffset.toFloat(), vOffset.toFloat(), paintText)

    }


    fun setDiscountNumberSize(size: Float) {
        paintText.textSize = size
    }

    fun setTextProduct(product: String) {
        dataType.text = product
    }

    fun setTextPrice(price: String) {
        this.price.text = price
    }

    fun setTextSizeProduct(size: Float) {
        dataType.textSize = size
    }

    fun setTextSizePrice(size: Float) {
        price.textSize = size
    }
}
