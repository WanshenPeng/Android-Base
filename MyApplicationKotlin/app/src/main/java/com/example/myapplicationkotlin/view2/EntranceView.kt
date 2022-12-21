package com.example.myapplicationkotlin.view2

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.example.myapplicationkotlin.R
import kotlinx.android.synthetic.main.activity_view2.view.*
import kotlinx.android.synthetic.main.entrance_view.view.*

/**
 * Author: Wanshenpeng
 * Date: 2022/12/20 17:25
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * Wanshenpeng 2022/12/20 1.0 首次创建
 */
class EntranceView(context: Context, attributes: AttributeSet) :
    RelativeLayout(context, attributes) {
    init {
        LayoutInflater.from(context).inflate(R.layout.entrance_view, this)
        context.theme.obtainStyledAttributes(attributes, R.styleable.EntranceView, 0, 0).apply {
            getResourceId(R.styleable.EntranceView_ev_left_image_src, -1).let {
                if (it == -1) {
                    iv_left.visibility = GONE
                } else {
                    iv_left.visibility = VISIBLE
                    iv_left.setImageResource(it)
                }
            }
            tv_text.apply {
                text = getString(R.styleable.EntranceView_ev_text)
                setTextSize(
                    TypedValue.COMPLEX_UNIT_PX, getDimension(
                        R.styleable.EntranceView_ev_text_size,
                        context.resources.getDimensionPixelSize(R.dimen.sp_16).toFloat()
                    )
                )
                setTextColor(
                    getColor(
                        R.styleable.EntranceView_ev_text_color,
                        context.resources.getColor(R.color.color_333333)
                    )
                )
                typeface = Typeface.create(
                    getString(R.styleable.EntranceView_ev_text_font),
                    Typeface.NORMAL
                )
            }
            tv_right.apply {
                text = getString(R.styleable.EntranceView_ev_right_text)
                visibility = if (text.isNullOrEmpty()) GONE else VISIBLE
                setTextSize(
                    TypedValue.COMPLEX_UNIT_PX, getDimension(
                        R.styleable.EntranceView_ev_right_text_size,
                        context.resources.getDimensionPixelSize(R.dimen.sp_16).toFloat()
                    )
                )
                setTextColor(
                    getColor(
                        R.styleable.EntranceView_ev_right_text_color,
                        context.resources.getColor(R.color.color_999999)
                    )
                )
                typeface = Typeface.create(
                    getString(R.styleable.EntranceView_ev_right_text_font),
                    Typeface.NORMAL
                )
            }
            getResourceId(R.styleable.EntranceView_ev_point_src, -1).let {
                if (it == -1) {
                    point.visibility = GONE
                } else {
                    point.visibility = VISIBLE
                    point.setImageResource(it)
                }
            }
            getResourceId(R.styleable.EntranceView_ev_right_arrow_src, -1).let {
                if (it == -1) {
                    right_arrow.visibility = GONE
                } else {
                    right_arrow.visibility = VISIBLE
                    right_arrow.setImageResource(it)
                }
            }

            divider_line.visibility =
                if (getBoolean(R.styleable.EntranceView_ev_divider_line, false)) VISIBLE else GONE
            divider_line.setBackgroundColor(
                getColor(
                    R.styleable.EntranceView_ev_divider_line_color,
                    context.resources.getColor(R.color.color_ee)
                )
            )
        }
    }

    fun setLeftImageResource(image: Int) {
        iv_left.setImageResource(image)
    }

    fun setRightArrowImageResource(image: Int) {
        right_arrow.setImageResource(image)
    }

    fun setPointImageResource(image: Int) {
        point.setImageResource(image)
    }

    fun getText(): String {
        return tv_text.text.toString()
    }

    fun setText(text: String) {
        tv_text.text = text
    }

    fun getTextColor(): Int {
        return tv_text.currentTextColor
    }

    /**
     * @param color 颜色资源id
     */
    fun setTextColor(color: Int) {
        tv_text.setTextColor(context.resources.getColor(color))
    }

    fun getTextSize(): Float {
        return tv_text.textSize
    }

    /**
     * @param size 字体大小资源id
     */
    fun setTextSize(size: Int) {
        tv_text.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            context.resources.getDimension(size)
        )
        tv_text.textSize
    }

    fun getTextFont(): Typeface? {
        return tv_text.typeface
    }

    fun setTextFont(font: String) {
        tv_text.typeface = Typeface.create(font, Typeface.NORMAL)
    }

    fun getRightText(): String {
        return tv_right.text.toString()
    }

    fun setRightText(text: String) {
        tv_right.text = text
    }

    fun getRightTextColor(): Int {
        return tv_right.currentTextColor
    }

    /**
     * @param color 颜色资源id
     */
    fun setRightTextColor(color: Int) {
        tv_right.setTextColor(context.resources.getColor(color))
    }

    fun getRightTextSize(): Float {
        return tv_right.textSize
    }

    /**
     * @param size 字体大小资源id
     */
    fun setRightTextSize(size: Int) {
        tv_right.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            context.resources.getDimension(size)
        )
    }

    fun getRightTextFont(): Typeface? {
        return tv_right.typeface
    }

    fun setRightTextFont(font: String) {
        tv_right.typeface = Typeface.create(font, Typeface.NORMAL)
    }

    fun setDividerLineColor(color: Int){
        divider_line.setBackgroundColor(context.resources.getColor(color))
    }

}