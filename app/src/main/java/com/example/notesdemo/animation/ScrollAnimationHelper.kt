package com.example.notesdemo.animation

import android.animation.ObjectAnimator
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ScrollAnimationHelper(private val container: View) : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        val visibilityThreshold = 0.1 // Adjust this threshold as needed

        // Check if the container is visible on the screen
        val containerVisible = recyclerView.computeVerticalScrollOffset() <= container.bottom

        // Calculate the visibility percentage
        val visibilityPercentage = (container.bottom - recyclerView.computeVerticalScrollOffset()) / container.height.toFloat()

        // If the container is visible and passes the visibility threshold, animate it
        if (containerVisible && visibilityPercentage >= visibilityThreshold) {
            val alpha = (1 - visibilityPercentage) // Reverse the visibility percentage for alpha

            // Animate the container's alpha
            ObjectAnimator.ofFloat(container, "alpha", container.alpha, alpha).apply {
                duration = 200 // Adjust the animation duration as needed
                start()
            }
        }
    }
}
