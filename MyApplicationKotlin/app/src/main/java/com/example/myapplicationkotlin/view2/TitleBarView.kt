package com.example.myapplicationkotlin.view2

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.example.myapplicationkotlin.R
import com.example.myapplicationkotlin.SystemBarHelper
import kotlinx.android.synthetic.main.title_bar_view.view.*

/**
 * Author: Wanshenpeng
 * Date: 2022/12/19 9:58
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * Wanshenpeng 2022/12/19 1.0 首次创建
 */
class TitleBarView(context: Context, attributes: AttributeSet) : LinearLayout(context, attributes) {
    init {
        LayoutInflater.from(context).inflate(R.layout.title_bar_view, this)
        context.theme.obtainStyledAttributes(attributes, R.styleable.TitleBarView, 0, 0).apply {
            // 设置左图标资源
            getResourceId(R.styleable.TitleBarView_left_image_src, -1).let {
                if (it == -1) {
                    iv_left.visibility = GONE
                } else {
                    iv_left.setImageResource(it)
                }
            }
            // 设置右图标资源
            getResourceId(R.styleable.TitleBarView_right_image_src, -1).let {
                if (it == -1) {
                    iv_right.visibility = GONE
                } else {
                    iv_right.setImageResource(it)
                }
            }
            // 设置标题
            tv_title.apply {
                text = getString(R.styleable.TitleBarView_title_text)
                visibility = if (text.isNullOrEmpty()) GONE else VISIBLE
                setTextSize(
                    TypedValue.COMPLEX_UNIT_PX, getDimension(
                        R.styleable.TitleBarView_title_text_size,
                        context.resources.getDimensionPixelSize(R.dimen.sp_18).toFloat()
                    )
                )
                setTextColor(
                    getColor(
                        R.styleable.TitleBarView_title_text_color,
                        context.resources.getColor(R.color.color_333333)
                    )
                )
                typeface = Typeface.create(
                    getString(R.styleable.TitleBarView_title_text_font),
                    Typeface.NORMAL
                )
            }
            // 设置右文字
            tv_right.apply {
                text = getString(R.styleable.TitleBarView_right_text)
                visibility = if (text.isNullOrEmpty()) GONE else VISIBLE
                setTextColor(
                    getColor(
                        R.styleable.TitleBarView_right_text_color,
                        context.resources.getColor(R.color.color_00c389)
                    )
                )
                setTextSize(
                    TypedValue.COMPLEX_UNIT_PX, getDimension(
                        R.styleable.TitleBarView_right_text_size,
                        context.resources.getDimensionPixelSize(R.dimen.sp_16).toFloat()
                    )
                )
                typeface = Typeface.create(
                    getString(R.styleable.TitleBarView_right_text_font),
                    Typeface.NORMAL
                )
            }
        }
    }


    fun setLeftImageResource(image: Int) {
        iv_left.setImageResource(image)
    }

    fun setLeftImageOnClickListener(onClickListener: OnClickListener) {
        iv_left.setOnClickListener(onClickListener)
    }

    fun setRightImageResource(image: Int) {
        iv_right.setImageResource(image)
    }

    fun setRightImageOnClickListener(onClickListener: OnClickListener) {
        iv_right.setOnClickListener(onClickListener)
    }

    fun setTitle(title: String) {
        tv_title.text = title
    }

    fun getTitle(): String {
        return tv_title.text.toString()
    }

    fun setTitleTextSize(size: Int) {
        tv_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.resources.getDimension(size))
    }

    fun getTitleTextSize(): Float {
        return tv_title.textSize
    }

    fun setTitleTextColor(color: Int) {
        tv_title.setTextColor(color)
    }

    fun getTitleTextColor(): Int {
        return tv_title.currentTextColor
    }

    fun setTitleTextFont(font: String) {
        tv_title.typeface = Typeface.create(font, Typeface.NORMAL)
    }

    fun getTitleTextFont(): Typeface? {
        return tv_title.typeface
    }

    fun setRightText(rightText: String) {
        tv_right.text = rightText
        tv_right.visibility = VISIBLE
    }

    fun getRightText(): String {
        return tv_right.text.toString()
    }

    fun setRightTextSize(size: Int) {
        tv_right.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.resources.getDimension(size))
    }

    fun getRightTextSize(): Float {
        return tv_right.textSize
    }

    fun setRightTextColor(color: Int) {
        tv_right.setTextColor(color)
    }

    fun getRightTextColor(): Int {
        return tv_right.currentTextColor
    }

    fun setRightTextFont(font: String) {
        tv_right.typeface = Typeface.create(font, Typeface.NORMAL)
    }

    fun getRightTextFont(): Typeface? {
        return tv_right.typeface
    }


}