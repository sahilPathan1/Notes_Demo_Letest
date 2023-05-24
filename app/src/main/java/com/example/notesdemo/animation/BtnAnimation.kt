package com.example.notesdemo.animation

import android.view.View
import androidx.recyclerview.widget.RecyclerView

class BtnAnimation(private val button: View) : RecyclerView.OnScrollListener() {

    private val scrollThreshold = 20 // Adjust this threshold as needed
    private var isButtonVisible = true

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        val scrolledY = recyclerView.computeVerticalScrollOffset()

        if (scrolledY > scrollThreshold && isButtonVisible) {
            hideButton()
        } else if (scrolledY <= scrollThreshold && !isButtonVisible) {
            showButton()
        }
    }

    private fun hideButton() {
        button.animate().apply {
            translationY(button.height.toFloat())
            alpha(0f)
            duration = 200 // Adjust the animation duration as needed
            start()
        }
        isButtonVisible = false
    }

    private fun showButton() {
        button.animate().apply {
            translationY(0f)
            alpha(1f)
            duration = 200 // Adjust the animation duration as needed
            start()
        }
        isButtonVisible = true
    }
}
