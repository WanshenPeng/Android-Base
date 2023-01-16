package com.example.myapplicationkotlin.view2

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.example.myapplicationkotlin.R
import kotlinx.android.synthetic.main.switch_view.view.*

/**
 * Author: Wanshenpeng
 * Date: 2022/12/29 18:10
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * Wanshenpeng 2022/12/29 1.0 首次创建
 */
class SwitchView(context: Context, attributeSet: AttributeSet) :
    ConstraintLayout(context, attributeSet) {

    private var onCheckedChangeListener: InverseBindingListener? = null
    var isChecked = false
        set(value) {
            val oldValue = field
            if (value == oldValue) {
                return
            }
            field = value
            onCheckedChangeListener?.onChange()
            sb_switch.isChecked = value
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.switch_view, this)
        context.theme.obtainStyledAttributes(R.styleable.SwitchView).apply {
            getBoolean(R.styleable.SwitchView_android_checked, false).let {
                sb_switch.isChecked = it
            }

            divider_line.apply {
                visibility =
                    if (getBoolean(R.styleable.SwitchView_svDividerLine, false)) VISIBLE else GONE
                setBackgroundColor(
                    getColor(
                        R.styleable.SwitchView_svDividerLineColor,
                        context.resources.getColor(R.color.color_ee)
                    )
                )
                val dividerLinePaddingHorizontal =
                    getDimension(R.styleable.SwitchView_svDividerLinePaddingHorizontal, 0f).toInt()
                val params = layoutParams as ConstraintLayout.LayoutParams
                params.setMargins(dividerLinePaddingHorizontal, 0, dividerLinePaddingHorizontal, 0)
            }

            tv_switch.apply {
                text = getString(R.styleable.SwitchView_svText)
                setTextColor(
                    getColor(
                        R.styleable.SwitchView_svTextColor,
                        context.resources.getColor(R.color.color_00c389)
                    )
                )
                setTextSize(
                    TypedValue.COMPLEX_UNIT_PX, getDimension(
                        R.styleable.SwitchView_svTextSize,
                        context.resources.getDimensionPixelSize(R.dimen.sp_16).toFloat()
                    )
                )
//                typeface = Typeface.create(
//                    getString(R.styleable.SwitchView_svTextFont),
//                    Typeface.NORMAL
//                )
                getString(R.styleable.SwitchView_svTextFont)?.let {
                    typeface = if (it.contains("res/font/")) {
                        ResourcesCompat.getFont(
                            context,
                            getResourceId(R.styleable.SwitchView_svTextFont, R.font.font_regular)
                        )
                    } else {
                        Typeface.create(
                            getString(R.styleable.SwitchView_svTextFont),
                            Typeface.NORMAL
                        )
                    }
                }
            }

            val contentPaddingHorizontal =
                getDimension(R.styleable.SwitchView_svContentPaddingHorizontal, 0f).toInt()
            cl_content.setPadding(contentPaddingHorizontal, 0, contentPaddingHorizontal, 0)
        }
    }

    fun setSwitchOnClickListener(onClickListener: OnClickListener) {
        tv_switch.setOnClickListener(onClickListener)
    }

    fun getSvText(): String {
        return tv_switch.text.toString()
    }

    fun setSvText(text: String) {
        tv_switch.text = text
    }

    /**
     * @param size 字体大小资源id
     */
    fun setSvTextSize(size: Int) {
        tv_switch.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.resources.getDimension(size))
    }

    fun getSvTextSize(): Float {
        return tv_switch.textSize
    }

    /**
     * @param color 颜色资源id
     */
    fun setSvTextColor(color: Int) {
        tv_switch.setTextColor(context.resources.getColor(color))
    }

    fun getSvTextColor(): Int {
        return tv_switch.currentTextColor
    }

    fun setSvTextFont(font: String) {
        tv_switch.typeface = Typeface.create(font, Typeface.NORMAL)
    }

    fun setSvTextFont(font: Int){
        tv_switch.typeface = context.resources.getFont(font)
    }

    fun getSvTextFont(): Typeface? {
        return tv_switch.typeface
    }

    fun getSbvIsChecked(): Boolean {
        return sb_switch.isChecked
    }

    fun setSbvIsChecked(isChecked: Boolean) {
        sb_switch.isChecked = isChecked
    }

    fun setDividerLineVisibility(visibility: Int) {
        divider_line.visibility = visibility
    }

    fun setDividerLineColor(color: Int) {
        divider_line.setBackgroundColor(context.resources.getColor(color))
    }


    companion object {
        @BindingAdapter("svIsChecked")
        @JvmStatic
        fun SwitchView.setSvIsChecked(value: Boolean) {
            isChecked = value
        }

        @InverseBindingAdapter(attribute = "svIsChecked", event = "svIsCheckedAttrChanged")
        @JvmStatic
        fun getSvIsChecked(view: SwitchView): Boolean {
            return view.isChecked
        }

        @BindingAdapter(value = ["svIsCheckedAttrChanged"], requireAll = false)
        @JvmStatic
        fun SwitchView.svIsCheckedChange(textAttrChanged: InverseBindingListener) {
            this.onCheckedChangeListener = textAttrChanged
        }
    }
}