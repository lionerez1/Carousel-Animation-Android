package com.lionerez.carouselanimation.animations

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation

internal class CarouselAnimationYTranslationAnimation(context: Context?, attrs: AttributeSet?, view: View, toY: Float) : Animation(context, attrs) {
    //region Members
    private val mView: View = view
    private val mViewStartingYTranslation = view.translationY
    private val mFinalYTranslation: Float = toY
    private val mYDifference: Float = mViewStartingYTranslation - mFinalYTranslation
    //endregion

    //region Implementations
    override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
        mView.translationY = calculateNewYScale(interpolatedTime)
    }
    //endregion

    //region Private Methods
    private fun calculateNewYScale(interpolatedTime: Float): Float {
        return mViewStartingYTranslation - mYDifference * interpolatedTime
    }
    //endregion
}