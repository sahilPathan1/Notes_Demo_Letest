package com.example.notesdemo.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.airbnb.lottie.LottieDrawable
import com.example.notesdemo.activity.MainActivity
import com.example.notesdemo.R
import com.example.notesdemo.databinding.ActivitySplashBinding

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        binding.apply {
            Handler(this@SplashScreenActivity.mainLooper).postDelayed({

                //This block will be executed after ANIMATION_TIME milliseconds.

                //After ANIMATION_TIME we will start the MainActivity
                startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))

                //To remove this activity from back stack so that
                // this activity will not show when user closes MainActivity
                finish()

            }, 3900)

            animationView.setAnimation(R.raw.sp)
            animationViewWelcome.setAnimation(R.raw.welcomee)
            animationView.repeatCount = LottieDrawable.INFINITE
            animationView.playAnimation()

        }
    }
}