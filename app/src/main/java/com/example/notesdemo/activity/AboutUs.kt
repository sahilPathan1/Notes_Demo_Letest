package com.example.notesdemo.activity

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.LinearInterpolator
import androidx.databinding.DataBindingUtil
import com.example.notesdemo.R
import com.example.notesdemo.databinding.ActivityAboutUsBinding

class AboutUs : AppCompatActivity() {
    private lateinit var binding: ActivityAboutUsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_about_us)

        binding.apply {
            val animLeftToTop =
                ObjectAnimator.ofFloat(container, "translationX", 0f, -container.width.toFloat())
            val animTopToRight =
                ObjectAnimator.ofFloat(container, "translationY", 0f, -container.height.toFloat())
            val animRightToBottom =
                ObjectAnimator.ofFloat(container, "translationX", -container.width.toFloat(), 0f)

            // Set the animation duration
            animLeftToTop.duration = 1000
            animTopToRight.duration = 1000
            animRightToBottom.duration = 1000

            // Set the interpolator for smooth movement
            animLeftToTop.interpolator = LinearInterpolator()
            animTopToRight.interpolator = LinearInterpolator()
            animRightToBottom.interpolator = LinearInterpolator()

            // Create an AnimatorSet to play the animations in sequence
            val animatorSet = AnimatorSet()
            animatorSet.playSequentially(animLeftToTop, animTopToRight, animRightToBottom)

            // Set the repeat count to infinite for continuous looping
            ObjectAnimator.INFINITE

            // Start the animation
            animatorSet.start()
        }
    }
}