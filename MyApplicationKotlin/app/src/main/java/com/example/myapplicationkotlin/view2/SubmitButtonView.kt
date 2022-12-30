package com.example.myapplicationkotlin.view2

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
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
    FrameLayout(context, attributeSet) {

    var isShowing = false

    init {
        LayoutInflater.from(context).inflate(R.layout.submit_buttom_view, this)
        context.theme.obtainStyledAttributes(attributeSet, R.styleable.SubmitButtonView, 0, 0)
            .apply {
                getBoolean(R.styleable.SubmitButtonView_sbvShowLoading, false).let {
                    if (it){
                        iv_loading.startAnimation(AnimationUtils.loadAnimation(context, R.anim.rotation))
                    }else{
                        iv_loading.clearAnimation()
                    }
                }
                btn_submit.apply {
                    text = getString(R.styleable.SubmitButtonView_sbvText)
                    setTextColor(
                        getColor(
                            R.styleable.SubmitButtonView_sbvTextColor,
                            context.resources.getColor(R.color.color_333333)
                        )
                    )
                    typeface = Typeface.create(
                        getString(R.styleable.SubmitButtonView_sbvTextFont),
                        Typeface.NORMAL
                    )
                    setTextSize(
                        TypedValue.COMPLEX_UNIT_PX, getDimension(
                            R.styleable.SubmitButtonView_sbtTextSize,
                            context.resources.getDimensionPixelSize(R.dimen.sp_16).toFloat()
                        )
                    )
                }
            }
    }

    fun setButtonOnClickListener(listener: OnClickListener){
        btn_submit.setOnClickListener(listener)
    }

    fun showLoading(){
        iv_loading.startAnimation(AnimationUtils.loadAnimation(context, R.anim.rotation))
        iv_loading.visibility = VISIBLE
        btn_submit.visibility = GONE
        isShowing = true
    }
    fun stopLoading(){
        iv_loading.clearAnimation()
        iv_loading.visibility = GONE
        btn_submit.visibility = VISIBLE
        isShowing = false
    }

    fun setSbvText(string: String){
        btn_submit.text = string
    }
    fun getSbvText():String{
        return btn_submit.text.toString()
    }

    /**
     * @param size 字体大小资源id
     */
    fun setSbvTextSize(size: Int) {
        btn_submit.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.resources.getDimension(size))
    }

    fun getSbvTextSize(): Float {
        return btn_submit.textSize
    }

    /**
     * @param color 颜色资源id
     */
    fun setSbvTextColor(color: Int) {
        btn_submit.setTextColor(context.resources.getColor(color))
    }

    fun getSbvTextColor(): Int {
        return btn_submit.currentTextColor
    }

    fun setSbvTextFont(font: String) {
        btn_submit.typeface = Typeface.create(font, Typeface.NORMAL)
    }

    fun getSbvTextFont(): Typeface? {
        return btn_submit.typeface
    }
}