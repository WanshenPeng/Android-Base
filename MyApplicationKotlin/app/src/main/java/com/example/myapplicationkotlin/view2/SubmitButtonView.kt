package com.example.myapplicationkotlin.view2

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import com.example.myapplicationkotlin.R
import kotlinx.android.synthetic.main.submit_buttom_view.view.*

/**
 * Author: Wanshenpeng
 * Date: 2022/12/29 17:53
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * Wanshenpeng 2022/12/29 1.0 首次创建
 */
class SubmitButtonView(context: Context, attributeSet: AttributeSet) :
    ConstraintLayout(context, attributeSet) {

    var isShowing = false

    init {
        LayoutInflater.from(context).inflate(R.layout.submit_buttom_view, this)
        context.theme.obtainStyledAttributes(attributeSet, R.styleable.SubmitButtonView, 0, 0)
            .apply {
                getBoolean(R.styleable.SubmitButtonView_sbvShowLoading, false).let {
                    if (it) {
                        iv_loading.startAnimation(
                            AnimationUtils.loadAnimation(
                                context,
                                R.anim.rotation
                            )
                        )
                        iv_loading.visibility = VISIBLE
                    } else {
                        iv_loading.clearAnimation()
                        iv_loading.visibility = GONE
                    }
                }
                tv_submit.apply {
                    text = getString(R.styleable.SubmitButtonView_sbvText)
                    setTextColor(
                        getColor(
                            R.styleable.SubmitButtonView_sbvTextColor,
                            context.resources.getColor(R.color.color_333333)
                        )
                    )
//                    typeface = Typeface.create(
//                        getString(R.styleable.SubmitButtonView_sbvTextFont),
//                        Typeface.NORMAL
//                    )
                    getString(R.styleable.SubmitButtonView_sbvTextFont)?.let {
                        typeface = if (it.contains("res/font/")) {
                            ResourcesCompat.getFont(
                                context,
                                getResourceId(
                                    R.styleable.SubmitButtonView_sbvTextFont,
                                    R.font.font_regular
                                )
                            )
                        } else {
                            Typeface.create(
                                getString(R.styleable.SubmitButtonView_sbvTextFont),
                                Typeface.NORMAL
                            )
                        }
                    }
                    setTextSize(
                        TypedValue.COMPLEX_UNIT_PX, getDimension(
                            R.styleable.SubmitButtonView_sbvTextSize,
                            context.resources.getDimensionPixelSize(R.dimen.sp_16).toFloat()
                        )
                    )
                }
                submit_button_view.apply {
                    setBackgroundResource(
                        getResourceId(
                            R.styleable.SubmitButtonView_sbvBackground,
                            R.drawable.btn_accent_gray_selector
                        )
                    )
                    isEnabled = getBoolean(R.styleable.SubmitButtonView_sbvEnabled, true)
                }
            }
    }

    fun setButtonOnClickListener(listener: OnClickListener) {
        submit_button_view.setOnClickListener(listener)
    }

    fun showLoading(loadingAnimation: Int? = null) {
        if (!isShowing) {
            if (loadingAnimation == null) {
                iv_loading.startAnimation(AnimationUtils.loadAnimation(context, R.anim.rotation))
            } else {
                iv_loading.startAnimation(AnimationUtils.loadAnimation(context, loadingAnimation))
            }
        }
        iv_loading.visibility = VISIBLE
        tv_submit.visibility = GONE
        isShowing = true
    }

    fun stopLoading() {
        iv_loading.clearAnimation()
        iv_loading.visibility = GONE
        tv_submit.visibility = VISIBLE
        isShowing = false
    }

    fun setSbvEnabled(enabled: Boolean) {
        submit_button_view.isEnabled = enabled
    }

    fun getSbvEnabled(): Boolean {
        return submit_button_view.isEnabled
    }

    fun setSbvText(string: String) {
        tv_submit.text = string
    }

    fun getSbvText(): String {
        return tv_submit.text.toString()
    }

    /**
     * @param size 字体大小资源id
     */
    fun setSbvTextSize(size: Int) {
        tv_submit.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.resources.getDimension(size))
    }

    fun getSbvTextSize(): Float {
        return tv_submit.textSize
    }

    /**
     * @param color 颜色资源id
     */
    fun setSbvTextColor(color: Int) {
        tv_submit.setTextColor(context.resources.getColor(color))
    }

    fun getSbvTextColor(): Int {
        return tv_submit.currentTextColor
    }

    fun setSbvTextFont(font: String) {
        tv_submit.typeface = Typeface.create(font, Typeface.NORMAL)
    }

    fun setSbvTextFont(font: Int){
        tv_submit.typeface = context.resources.getFont(font)
    }

    fun getSbvTextFont(): Typeface? {
        return tv_submit.typeface
    }
}