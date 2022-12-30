package com.example.myapplicationkotlin.view2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.myapplicationkotlin.R
import com.example.myapplicationkotlin.SystemBarHelper
import com.example.myapplicationkotlin.databinding.ActivityView2Binding
import kotlinx.android.synthetic.main.activity_view2.*

class View2Activity : AppCompatActivity() {
    private lateinit var databinding : ActivityView2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_view2)
        databinding = DataBindingUtil.setContentView(this,R.layout.activity_view2)
        SystemBarHelper.immersiveStatusBar(window, 0.0.toFloat())
        SystemBarHelper.setStatusBarDarkMode(this, true)
        SystemBarHelper.setHeightAndPadding(this, ll_status)

        text_entrance_view.setOnClickListener {
            Toast.makeText(this, "text_entrance_view", Toast.LENGTH_SHORT).show()
        }
        text_entrance_view.setEvTextSize(R.dimen.sp_10)

        title_bar.setLeftImageOnClickListener{
            Toast.makeText(this, "title_bar", Toast.LENGTH_SHORT).show()
        }
        title_bar.setRightImageOnClickListener{
            Toast.makeText(this, "title_bar", Toast.LENGTH_SHORT).show()
        }



        val viewModel = ViewModelProvider(this).get(View2ViewModel::class.java)
        databinding.viewModel = viewModel

        btn_submit2.setButtonOnClickListener {
            Toast.makeText(this, viewModel.text.get(), Toast.LENGTH_SHORT).show()
            btn_submit2.showLoading()
        }

        test.setOnClickListener{
            Toast.makeText(this, viewModel.text.get(), Toast.LENGTH_SHORT).show()
            viewModel.show.set(true)
            btn_submit2.stopLoading()
        }
    }
}