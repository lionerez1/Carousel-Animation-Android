package com.lionerez.carouselanimation.animations

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation

internal class CarouselAnimationXRotateAnimation(context: Context?, attrs: AttributeSet?, view: View, finalXRotate: Float) : Animation(context, attrs) {
    //region Members
    private val mView: View = view
    private val mViewStartingXRotation: Float = view.rotationX
    private val finalXRotation: Float = finalXRotate - mViewStartingXRotation
    //endregion

    //region Implementations
    override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
        mView.rotationX = calculateNewXRotation(interpolatedTime)
    }
    //endregion

    //region Private Methods
    private fun calculateNewXRotation(interpolatedTime: Float): Float {
        val newRotationValue: Float = finalXRotation * interpolatedTime
        return mViewStartingXRotation + newRotationValue
    }
    //endregion
}