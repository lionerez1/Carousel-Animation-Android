package com.lionerez.carouselanimation.models

internal class CarouselAnimationNewValuesResult(newWidth: Int, newHeight: Int) {
    private val mNewWidth: Int = newWidth
    private val mNewHeight: Int = newHeight

    fun getNewWidth(): Int {
        return mNewWidth
    }

    fun getNewHeight(): Int {
        return mNewHeight
    }
}