package com.example.myapplicationkotlin.view2

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.example.myapplicationkotlin.R
import kotlinx.android.synthetic.main.title_bar_view.view.*

/**
 * Author: Wanshenpeng
 * Date: 2022/12/19 9:58
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * Wanshenpeng 2022/12/19 1.0 首次创建
 */
class TitleBarView(context: Context, attributes: AttributeSet) : ConstraintLayout(context, attributes) {
    init {
        LayoutInflater.from(context).inflate(R.layout.title_bar_view, this)
        context.theme.obtainStyledAttributes(attributes, R.styleable.TitleBarView, 0, 0).apply {
            // 设置左图标资源
            getResourceId(R.styleable.TitleBarView_tbvLeftImageSrc, -1).let {
                if (it == -1) {
                    iv_left.visibility = GONE
                } else {
                    iv_left.setImageResource(it)
                }
            }
            // 设置右图标资源
            getResourceId(R.styleable.TitleBarView_tbvRightImageSrc, -1).let {
                if (it == -1) {
                    iv_right.visibility = GONE
                } else {
                    iv_right.setImageResource(it)
                }
            }
            // 设置标题
            tv_title.apply {
                text = getString(R.styleable.TitleBarView_tbvTitleText)
                visibility = if (text.isNullOrEmpty()) GONE else VISIBLE
                setTextSize(
                    TypedValue.COMPLEX_UNIT_PX, getDimension(
                        R.styleable.TitleBarView_tbvTitleTextSize,
                        context.resources.getDimensionPixelSize(R.dimen.sp_18).toFloat()
                    )
                )
                setTextColor(
                    getColor(
                        R.styleable.TitleBarView_tbvTitleTextColor,
                        context.resources.getColor(R.color.color_333333)
                    )
                )
                typeface = Typeface.create(
                    getString(R.styleable.TitleBarView_tbvTitleTextFont),
                    Typeface.NORMAL
                )
            }
            // 设置右文字
            tv_right.apply {
                text = getString(R.styleable.TitleBarView_tbvRightText)
                visibility = if (text.isNullOrEmpty()) GONE else VISIBLE
                setTextColor(
                    getColor(
                        R.styleable.TitleBarView_tbvRightTextColor,
                        context.resources.getColor(R.color.color_00c389)
                    )
                )
                setTextSize(
                    TypedValue.COMPLEX_UNIT_PX, getDimension(
                        R.styleable.TitleBarView_tbvRightTextSize,
                        context.resources.getDimensionPixelSize(R.dimen.sp_16).toFloat()
                    )
                )
                typeface = Typeface.create(
                    getString(R.styleable.TitleBarView_tbvRightTextFont),
                    Typeface.NORMAL
                )
            }
        }
    }


    fun setTbvLeftImageSrc(image: Int) {
        iv_left.setImageResource(image)
    }

    fun setLeftImageOnClickListener(onClickListener: OnClickListener) {
        iv_left.setOnClickListener(onClickListener)
    }

    fun setTbvRightImageSrc(image: Int) {
        iv_right.setImageResource(image)
    }

    fun setRightImageOnClickListener(onClickListener: OnClickListener) {
        iv_right.setOnClickListener(onClickListener)
    }

    fun setTbvTitleText(title: String){
        tv_title.text = title
        tv_title.visibility = VISIBLE
    }

    fun getTbvTitleText(): String {
        return tv_title.text.toString()
    }

    /**
     * @param size 字体大小资源id
     */
    fun setTbvTitleTextSize(size: Int) {
        tv_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.resources.getDimension(size))
    }

    fun getTbvTitleTextSize(): Float {
        return tv_title.textSize
    }

    /**
     * @param color 颜色资源id
     */
    fun setTbvTitleTextColor(color: Int) {
        tv_title.setTextColor(context.resources.getColor(color))
    }

    fun getTbvTitleTextColor(): Int {
        return tv_title.currentTextColor
    }

    fun setTbvTitleTextFont(font: String) {
        tv_title.typeface = Typeface.create(font, Typeface.NORMAL)
    }

    fun getTbvTitleTextFont(): Typeface? {
        return tv_title.typeface
    }

    fun setTbvRightText(rightText: String) {
        tv_right.text = rightText
        tv_right.visibility = VISIBLE
    }

    fun getTbvRightText(): String {
        return tv_right.text.toString()
    }

    /**
     * @param size 字体大小资源id
     */
    fun setTbvRightTextSize(size: Int) {
        tv_right.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.resources.getDimension(size))
    }

    fun getTbvRightTextSize(): Float {
        return tv_right.textSize
    }

    /**
     * @param color 颜色资源id
     */
    fun setTbvRightTextColor(color: Int) {
        tv_right.setTextColor(context.resources.getColor(color))
    }

    fun getTbvRightTextColor(): Int {
        return tv_right.currentTextColor
    }

    fun setTbvRightTextFont(font: String) {
        tv_right.typeface = Typeface.create(font, Typeface.NORMAL)
    }

    fun getTbvRightTextFont(): Typeface? {
        return tv_right.typeface
    }

    fun setRightTextClickListener(clickListener: OnClickListener){
        tv_right.setOnClickListener(clickListener)
    }



}