package com.lionerez.carouselanimation.models

internal class CarouselAnimationViewValues(scaleX: Float, scaleY: Float, yTranslation: Float) {
    private val mScaleX: Float = scaleX
    private val mScaleY: Float = scaleY
    private val mYTranslation: Float = yTranslation

    //region Getters
    fun getScaleX(): Float {
        return mScaleX
    }

    fun getScaleY(): Float {
        return mScaleY
    }

    fun getYTranslation(): Float {
        return mYTranslation
    }
    //endregion
}