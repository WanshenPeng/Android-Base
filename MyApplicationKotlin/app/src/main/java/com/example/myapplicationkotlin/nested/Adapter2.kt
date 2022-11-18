package com.example.myapplicationkotlin.nested

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.myapplicationkotlin.R

class Adapter2(layoutResId: Int): BaseQuickAdapter<String, BaseViewHolder>(layoutResId) {
    override fun convert(holder: BaseViewHolder, item: String) {
        holder.setText(R.id.location_name_txt2, item)
    }
}