package com.lionerez.carouselanimation.animations

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation

internal class CarouselAnimationScaleAnimation(context: Context?, attrs: AttributeSet?, view: View, toX: Float, toY: Float) : Animation(context, attrs) {
    //region Members
    private val mView: View = view
    private val mViewStartingXScale = view.scaleX
    private val mViewStartingYScale = view.scaleY
    private val mFinalXScale: Float = toX
    private val mFinalYScale: Float = toY
    private val mXScaleDifference: Float = mViewStartingXScale - mFinalXScale
    private val mYScaleDifference: Float = mViewStartingYScale - mFinalYScale
    //endregion

    //region Implementations
    override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
        mView.scaleX = calculateNewXScale(interpolatedTime)
        mView.scaleY = calculateNewYScale(interpolatedTime)
    }
    //endregion

    //region Private Methods
    private fun calculateNewXScale(interpolatedTime: Float): Float {
        return mViewStartingXScale - mXScaleDifference * interpolatedTime
    }

    private fun calculateNewYScale(interpolatedTime: Float): Float {
        return mViewStartingYScale - mYScaleDifference * interpolatedTime
    }
    //endregion
}