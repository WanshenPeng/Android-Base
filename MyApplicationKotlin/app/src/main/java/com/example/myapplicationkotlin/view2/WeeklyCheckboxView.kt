package com.example.myapplicationkotlin.view2

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.example.myapplicationkotlin.R
import kotlinx.android.synthetic.main.weekly_checkbox_view.view.*

/**
 * Author: Wanshenpeng
 * Date: 2022/12/29 21:44
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * Wanshenpeng 2022/12/29 1.0 首次创建
 */
class WeeklyCheckboxView(context: Context, attributeSet: AttributeSet) :
    LinearLayout(context, attributeSet) {
    init {
        LayoutInflater.from(context).inflate(R.layout.weekly_checkbox_view, this)

        tv_mo.setOnClickListener {
            it.isSelected = !it.isSelected
        }
        tv_tu.setOnClickListener {
            it.isSelected = !it.isSelected
        }
        tv_th.setOnClickListener {
            it.isSelected = !it.isSelected
        }
        tv_we.setOnClickListener {
            it.isSelected = !it.isSelected
        }
        tv_fr.setOnClickListener {
            it.isSelected = !it.isSelected
        }
        tv_sa.setOnClickListener {
            it.isSelected = !it.isSelected
        }
        tv_su.setOnClickListener {
            it.isSelected = !it.isSelected
        }
    }

    fun getSelectedDays():MutableList<Int>{
        val result = mutableListOf<Int>()
        if (tv_mo.isSelected) result.add(1)
        if (tv_tu.isSelected) result.add(2)
        if (tv_we.isSelected) result.add(3)
        if (tv_th.isSelected) result.add(4)
        if (tv_fr.isSelected) result.add(5)
        if (tv_sa.isSelected) result.add(6)
        if (tv_su.isSelected) result.add(7)
        return result
    }
}