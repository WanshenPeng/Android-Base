package com.example.myapplicationkotlin.view2

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.marginStart
import com.example.myapplicationkotlin.R
import kotlinx.android.synthetic.main.entrance_view.view.*
import kotlinx.android.synthetic.main.entrance_view.view.divider_line

/**
 * Author: Wanshenpeng
 * Date: 2022/12/20 17:25
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * Wanshenpeng 2022/12/20 1.0 首次创建
 */
class EntranceView(context: Context, attributes: AttributeSet) :
    ConstraintLayout(context, attributes) {
    init {
        LayoutInflater.from(context).inflate(R.layout.entrance_view, this)
        context.theme.obtainStyledAttributes(attributes, R.styleable.EntranceView, 0, 0).apply {
            getResourceId(R.styleable.EntranceView_evLeftImageSrc, -1).let {
                if (it == -1) {
                    iv_left.visibility = GONE
                } else {
                    iv_left.visibility = VISIBLE
                    iv_left.setImageResource(it)
                }
            }
            tv_text.apply {
                text = getString(R.styleable.EntranceView_evText)
                setTextSize(
                    TypedValue.COMPLEX_UNIT_PX, getDimension(
                        R.styleable.EntranceView_evTextSize,
                        context.resources.getDimensionPixelSize(R.dimen.sp_16).toFloat()
                    )
                )
                setTextColor(
                    getColor(
                        R.styleable.EntranceView_evTextColor,
                        context.resources.getColor(R.color.color_333333)
                    )
                )
//                typeface = Typeface.create(
//                    getString(R.styleable.EntranceView_evTextFont),
//                    Typeface.NORMAL
//                )
                getString(R.styleable.EntranceView_evTextFont)?.let {
                    typeface = if (it.contains("res/font/")) {
                        ResourcesCompat.getFont(
                            context,
                            getResourceId(R.styleable.EntranceView_evTextFont, R.font.font_regular)
                        )
                    } else {
                        Typeface.create(
                            getString(R.styleable.EntranceView_evTextFont),
                            Typeface.NORMAL
                        )
                    }
                }
            }
            tv_right.apply {
                text = getString(R.styleable.EntranceView_evRightText)
                visibility = if (text.isNullOrEmpty()) GONE else VISIBLE
                setTextSize(
                    TypedValue.COMPLEX_UNIT_PX, getDimension(
                        R.styleable.EntranceView_evRightTextSize,
                        context.resources.getDimensionPixelSize(R.dimen.sp_16).toFloat()
                    )
                )
                setTextColor(
                    getColor(
                        R.styleable.EntranceView_evRightTextColor,
                        context.resources.getColor(R.color.color_999999)
                    )
                )
//                typeface = Typeface.create(
//                    getString(R.styleable.EntranceView_evRightTextFont),
//                    Typeface.NORMAL
//                )
                getString(R.styleable.EntranceView_evTextFont)?.let {
                    typeface = if (it.contains("res/font/")) {
                        ResourcesCompat.getFont(
                            context,
                            getResourceId(
                                R.styleable.EntranceView_evRightTextFont,
                                R.font.font_regular
                            )
                        )
                    } else {
                        Typeface.create(
                            getString(R.styleable.EntranceView_evRightTextFont),
                            Typeface.NORMAL
                        )
                    }
                }
            }
            getResourceId(R.styleable.EntranceView_evPointSrc, -1).let {
                if (it == -1) {
                    point.visibility = GONE
                } else {
                    point.visibility = VISIBLE
                    point.setImageResource(it)
                }
            }
            getResourceId(R.styleable.EntranceView_evRightArrowSrc, -1).let {
                if (it == -1) {
                    right_arrow.visibility = GONE
                } else {
                    right_arrow.visibility = VISIBLE
                    right_arrow.setImageResource(it)
                }
            }

            divider_line.apply {
                visibility =
                    if (getBoolean(R.styleable.EntranceView_evDividerLine, false)) VISIBLE else GONE
                setBackgroundColor(
                    getColor(
                        R.styleable.EntranceView_evDividerLineColor,
                        context.resources.getColor(R.color.color_ee)
                    )
                )
                val dividerLinePaddingHorizontal = getDimension(
                    R.styleable.EntranceView_evDividerLinePaddingHorizontal,
                    0f
                ).toInt()
                val params = layoutParams as ConstraintLayout.LayoutParams
                params.setMargins(dividerLinePaddingHorizontal, 0, dividerLinePaddingHorizontal, 0)
            }

            val contentPaddingHorizontal =
                getDimension(R.styleable.EntranceView_evContentPaddingHorizontal, 0f).toInt()
//            val params = cl_content.layoutParams as ConstraintLayout.LayoutParams
//            params.setMargins(contentPaddingHorizontal, 0, contentPaddingHorizontal, 0)
            cl_content.setPadding(contentPaddingHorizontal, 0, contentPaddingHorizontal, 0)
        }
    }

    fun setEvLeftImageSrc(image: Int) {
        iv_left.setImageResource(image)
    }

    fun setEvRightArrowSrc(image: Int) {
        right_arrow.setImageResource(image)
    }

    fun setEvPointSrc(image: Int) {
        point.setImageResource(image)
    }

    fun getEvText(): String {
        return tv_text.text.toString()
    }

    fun setEvText(text: String) {
        tv_text.text = text
        tv_right.visibility = VISIBLE
    }

    fun getEvTextColor(): Int {
        return tv_text.currentTextColor
    }

    /**
     * @param color 颜色资源id
     */
    fun setEvTextColor(color: Int) {
        tv_text.setTextColor(context.resources.getColor(color))
    }

    fun getEvTextSize(): Float {
        return tv_text.textSize
    }

    /**
     * @param size 字体大小资源id
     */
    fun setEvTextSize(size: Int) {
        tv_text.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            context.resources.getDimension(size)
        )
        tv_text.textSize
    }

    fun getEvTextFont(): Typeface? {
        return tv_text.typeface
    }

    fun setEvTextFont(font: String) {
        tv_text.typeface = Typeface.create(font, Typeface.NORMAL)
    }

    fun setEvTextFont(font: Int) {
        tv_text.typeface = context.resources.getFont(font)
    }

    fun getEvRightText(): String {
        return tv_right.text.toString()
    }

    fun setEvRightText(text: String) {
        tv_right.text = text
        tv_right.visibility = VISIBLE
    }

    fun getEvRightTextColor(): Int {
        return tv_right.currentTextColor
    }

    /**
     * @param color 颜色资源id
     */
    fun setEvRightTextColor(color: Int) {
        tv_right.setTextColor(context.resources.getColor(color))
    }

    fun getEvRightTextSize(): Float {
        return tv_right.textSize
    }

    /**
     * @param size 字体大小资源id
     */
    fun setEvRightTextSize(size: Int) {
        tv_right.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            context.resources.getDimension(size)
        )
    }

    fun getEvRightTextFont(): Typeface? {
        return tv_right.typeface
    }

    fun setEvRightTextFont(font: String) {
        tv_right.typeface = Typeface.create(font, Typeface.NORMAL)
    }

    fun setEvRightTextFont(font: Int){
        tv_right.typeface = context.resources.getFont(font)
    }

    fun setEvDividerLine(visibility: Boolean) {
        divider_line.visibility = if (visibility) VISIBLE else GONE
    }

    fun setEvDividerLineColor(color: Int) {
        divider_line.setBackgroundColor(context.resources.getColor(color))
    }

}