package com.lionerez.carouselanimation.animations

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation

internal class CarouselAnimationYScaleAnimation(context: Context?, attrs: AttributeSet?, view: View) : Animation(context, attrs) {
    //region Members
    private val mView: View = view
    private val mViewStartingYScale = view.scaleY
    private val mFinalYScale: Float = 0.5f
    private val mYScaleDifference: Float = mViewStartingYScale - mFinalYScale
    //endregion

    //region Implementations
    override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
        mView.scaleY = calculateNewXScale(interpolatedTime)
    }
    //endregion

    //region Private Methods
    private fun calculateNewXScale(interpolatedTime: Float): Float {
        return mViewStartingYScale - mYScaleDifference * interpolatedTime
    }
    //endregion
}