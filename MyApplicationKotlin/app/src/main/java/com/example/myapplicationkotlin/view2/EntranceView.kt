package com.example.myapplicationkotlin.view2

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.example.myapplicationkotlin.R
import kotlinx.android.synthetic.main.entrance_view.view.*

/**
 * Author: Wanshenpeng
 * Date: 2022/12/20 17:25
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * Wanshenpeng 2022/12/20 1.0 首次创建
 */
class EntranceView(context: Context, attributes: AttributeSet) : LinearLayout(context, attributes) {
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

            val left = getDimensionPixelSize(
                R.styleable.EntranceView_ev_padding_left,
                getDimensionPixelSize(R.styleable.EntranceView_ev_padding_horizontal, 0)
            )
            val right = getDimensionPixelSize(
                R.styleable.EntranceView_ev_padding_right,
                getDimensionPixelSize(R.styleable.EntranceView_ev_padding_horizontal, 0)
            )
            ll_entrance.setPadding(left, 0, right, 0)
            if (getBoolean(R.styleable.EntranceView_ev_divider_line, false)){
                ll_entrance.background = context.resources.getDrawable(R.drawable.bg_divider_line2)
            }
        }
    }
}