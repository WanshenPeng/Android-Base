package com.example.myapplicationkotlin.view2

import android.content.Context
import android.graphics.Typeface
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import androidx.core.widget.doOnTextChanged
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.example.myapplicationkotlin.R
import kotlinx.android.synthetic.main.input_text_view.view.*

/**
 * Author: Wanshenpeng
 * Date: 2022/12/22 9:47
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * Wanshenpeng 2022/12/22 1.0 首次创建
 */
class InputTextView(context: Context, attributeSet: AttributeSet) :
    RelativeLayout(context, attributeSet) {
    private var onInputChangeListener: InverseBindingListener? = null
    var etInput = ""
        set(value) {
            val oldValue = field
            if (value == oldValue) {
                return
            }
            field = value
            onInputChangeListener?.onChange()
            tv_input.setText(value)
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.input_text_view, this)
        context.theme.obtainStyledAttributes(attributeSet, R.styleable.InputTextView, 0, 0).apply {
            getResourceId(R.styleable.InputTextView_itvLeftImageSrc, -1).let {
                if (it == -1) {
                    tv_input.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
                } else {
                    tv_input.setCompoundDrawablesWithIntrinsicBounds(
                        context.resources.getDrawable(it),
                        null,
                        null,
                        null
                    )
                }
            }

            getResourceId(R.styleable.InputTextView_itvRightImageSrc, -1).let {
                if (it == -1) {
                    cb_right.visibility = GONE
                } else {
                    cb_right.visibility = VISIBLE
                    cb_right.background = context.resources.getDrawable(it)
                    tv_input.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                    tv_input.transformationMethod = PasswordTransformationMethod.getInstance()
                    tv_input.setSelection(tv_input.text.length)
                }
            }
            cb_right.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    tv_input.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    tv_input.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    tv_input.setSelection(tv_input.text.length)
                } else {
                    tv_input.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                    tv_input.transformationMethod = PasswordTransformationMethod.getInstance()
                    tv_input.setSelection(tv_input.text.length)
                }
            }


            tv_input.apply {
                hint = getString(R.styleable.InputTextView_itvHintText)
                setHintTextColor(
                    getColor(
                        R.styleable.InputTextView_itvHintTextColor,
                        context.resources.getColor(R.color.color_bbbbbb)
                    )
                )
                setText(getString(R.styleable.InputTextView_itvText))
                setTextColor(
                    getColor(
                        R.styleable.InputTextView_itvHintTextColor,
                        context.resources.getColor(R.color.color_333333)
                    )
                )
                typeface = Typeface.create(
                    getString(R.styleable.InputTextView_itvTextFont),
                    Typeface.NORMAL
                )
                setTextSize(
                    TypedValue.COMPLEX_UNIT_PX, getDimension(
                        R.styleable.InputTextView_itvTextSize,
                        context.resources.getDimensionPixelSize(R.dimen.sp_16).toFloat()
                    )
                )
                doOnTextChanged { text, start, count, after ->
                    etInput = text.toString()
                }
            }

            tv_error_message.apply {
                text = getString(R.styleable.InputTextView_itvErrorMessage)
                setTextColor(
                    getColor(
                        R.styleable.InputTextView_itvErrorMessageTextColor,
                        context.resources.getColor(R.color.color_fa584d)
                    )
                )
                setTextSize(
                    TypedValue.COMPLEX_UNIT_PX, getDimension(
                        R.styleable.InputTextView_itvErrorMessageTextSize,
                        context.resources.getDimensionPixelSize(R.dimen.sp_14).toFloat()
                    )
                )

                visibility = getInt(R.styleable.InputTextView_itvErrorMessageVisibility, 2)
            }
        }
    }

//    fun setItvLeftImageSrc(image: Int) {
//        iv_left.setImageResource(image)
//    }
//
//    fun setItvRightImageSrc(image: Int) {
//        iv_right.setImageResource(image)
//    }

    fun getItvHintText(): String {
        return tv_input.hint.toString()
    }

    fun setItvHintText(hint: String) {
        tv_input.hint = hint
    }

    fun getItvHintTextColor(): Int {
        return tv_input.currentHintTextColor
    }

    fun setItvHintTextColor(color: Int) {
        tv_input.setHintTextColor(context.resources.getColor(color))
    }
//
//    fun getItvText(): String {
//        return tv_input.text.toString()
//    }
//
//    fun setItvText(text: String) {
//        tv_input.setText(text)
//    }

    fun getItvTextColor(): Int {
        return tv_input.currentTextColor
    }

    /**
     * @param color 颜色资源id
     */
    fun setItvTextColor(color: Int) {
        tv_input.setTextColor(context.resources.getColor(color))
    }

    fun getItvTextSize(): Float {
        return tv_input.textSize
    }

    /**
     * @param size 字体大小资源id
     */
    fun setItvTextSize(size: Int) {
        tv_input.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            context.resources.getDimension(size)
        )
        tv_input.textSize
    }

    fun getItvTextFont(): Typeface? {
        return tv_input.typeface
    }

    fun setItvTextFont(font: String) {
        tv_input.typeface = Typeface.create(font, Typeface.NORMAL)
    }

    fun getItvErrorMessage(): String {
        return tv_error_message.text.toString()
    }

    fun setItvErrorMessage(errorMessage: String) {
        tv_error_message.text = errorMessage
    }

    fun getItvErrorMessageTextColor(): Int {
        return tv_error_message.currentTextColor
    }

    /**
     * @param color 颜色资源id
     */
    fun setItvErrorMessageTextColor(color: Int) {
        tv_error_message.setTextColor(context.resources.getColor(color))
    }

    fun getItvErrorMessageTextSize(): Float {
        return tv_error_message.textSize
    }

    /**
     * @param size 字体大小资源id
     */
    fun setItvErrorMessageTextSize(size: Int) {
        tv_error_message.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            context.resources.getDimension(size)
        )
        tv_error_message.textSize
    }

    fun getItvErrorMessageTextFont(): Typeface? {
        return tv_error_message.typeface
    }

    fun setItvErrorMessageTextFont(font: String) {
        tv_error_message.typeface = Typeface.create(font, Typeface.NORMAL)
    }

    fun getItvErrorMessageVisibility(): Int {
        return tv_error_message.visibility
    }

    fun setItvErrorMessageVisibility(visibility: Int) {
        tv_error_message.visibility = visibility
    }

    companion object {
        @BindingAdapter("itvText")
        @JvmStatic
        fun InputTextView.setItvText(value: String) {
            etInput = value
        }

        @InverseBindingAdapter(attribute = "itvText", event = "itvTextAttrChanged")
        @JvmStatic
        fun getItvText(view: InputTextView): String {
            return view.etInput
        }

        @BindingAdapter(value = ["itvTextAttrChanged"], requireAll = false)
        @JvmStatic
        fun InputTextView.itvTextChange(textAttrChanged: InverseBindingListener) {
            this.onInputChangeListener = textAttrChanged
        }
    }
}