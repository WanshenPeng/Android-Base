package com.example.myapplicationkotlin.view2

import androidx.databinding.Observable
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel

/**
 * Author: Wanshenpeng
 * Date: 2022/12/29 19:06
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * Wanshenpeng 2022/12/29 1.0 首次创建
 */
class View2ViewModel:ViewModel() {
    val title = "View2Activity"
    val rightText = "rightText"

    val show = ObservableBoolean(false)

    val errorMessage = "this is errorMessage"
    val text = ObservableField("123456")
}