package com.example.myapplication.lottie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.example.myapplicationkotlin.R

class LottieAnimationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lottie_animation)

        val animationView = findViewById<LottieAnimationView>(R.id.lottieAnimationView);
//        animationView.setAnimation("wizzard.json");//在assets目录下的动画json文件名。
//        animationView.loop(true);//设置动画循环播放
        animationView.repeatCount = LottieDrawable.INFINITE
//        animationView.imageAssetsFolder = "images/";//assets目录下的子目录，存放动画所需的图片
//        animationView.playAnimation();//播放动画
    }

    //lottie_loop：动画是否循环播放，默认不循环播放
    //lottie_autoPlay：动画是否自动播放，默认不自动播放。
    //lottie_imageAssetsFolder：动画所依赖的图片目录，在app/src/main/assets/目录下的子目录，该子目录存放所有图片
}