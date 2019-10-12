package com.lionerez.carouselanimation.animations

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation

internal class CarouselAnimationXScaleAnimation(context: Context?, attrs: AttributeSet?, view: View) : Animation(context, attrs) {
    //region Members
    private val mView: View = view
    private val mViewStartingXScale = view.scaleX
    private val mFinalXScale: Float = 0.9f
    private val mXScaleDifference: Float = mViewStartingXScale - mFinalXScale
    //endregion

    //region Implementations
    override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
        mView.scaleX = calculateNewXScale(interpolatedTime)
    }
    //endregion

    //region Private Methods
    private fun calculateNewXScale(interpolatedTime: Float): Float {
        return mViewStartingXScale - mXScaleDifference * interpolatedTime
    }
    //endregion
}