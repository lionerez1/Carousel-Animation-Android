package com.lionerez.carouselanimation.models

import com.lionerez.carouselanimation.extensions.getNextScale

internal class CarouselAnimationValues(width: Int, height: Int, size: Int) {
    //region Members
    private val mOriginalWidth: Int = width
    private val mOriginalHeight: Int = height
    val mViewAnimationValues: ArrayList<CarouselAnimationViewValues> = ArrayList()
    private val mNumberOfViews: Int = size
    val mHorizontalMargins: Int = (mOriginalHeight * 0.1 * mNumberOfViews).toInt()
    val mVerticalMargins: Int = (mOriginalWidth * 0.1).toInt()
    private val mTouchEventNextMinimumDistance: Int = 0
    val mTouchEventNextMaximumDistance: Int = 100
    private val mTouchEventPreviousMinimumDistance: Int = 1
    val mTouchEventPreviousMaximumDistance: Int = 200
    //endregion

    init {
        var lastCalculatedWidth: Int = mOriginalWidth
        var lastCalculatedHeight: Int = mOriginalHeight
        createFirstViewValues()
        for (i in 1 until mNumberOfViews) {
            val results: CarouselAnimationNewValuesResult = createNewViewValues(lastCalculatedWidth, lastCalculatedHeight)
            lastCalculatedHeight = results.getNewHeight()
            lastCalculatedWidth = results.getNewWidth()
        }
    }

    //region Public Methods
    fun getFirstViewValues(): CarouselAnimationViewValues {
        return mViewAnimationValues[0]
    }

    fun getLastViewValues(): CarouselAnimationViewValues {
        return mViewAnimationValues[mViewAnimationValues.size - 1]
    }

    fun isDistanceInNextMovementEventRange(distance: Int): Boolean {
        return distance in mTouchEventNextMinimumDistance..mTouchEventNextMaximumDistance
    }

    fun isDistanceInPreviousMovementEventRange(distance: Int): Boolean {
        return distance in mTouchEventPreviousMinimumDistance..mTouchEventPreviousMaximumDistance
    }
    //endregion

    //region Private Methods
    private fun createFirstViewValues() {
        addViewAnimationValues(1f,1f,0f)
    }

    private fun createNewViewValues(lastCalculatedWidth: Int, lastCalculatedHeight: Int): CarouselAnimationNewValuesResult {
        val newWidth = lastCalculatedWidth.getNextScale()
        val newHeight = lastCalculatedHeight.getNextScale()
        val scaleX: Float = newWidth.toFloat() / mOriginalWidth.toFloat()
        val scaleY: Float = newHeight.toFloat() / mOriginalHeight.toFloat()
        var yTranslation: Float = newHeight.toFloat() - mOriginalHeight.toFloat()
        yTranslation += yTranslation * 0.3f
        addViewAnimationValues(scaleX, scaleY, yTranslation)
        return CarouselAnimationNewValuesResult(newWidth, newHeight)
    }

    private fun addViewAnimationValues(scaleX: Float, scaleY: Float, yTranslation: Float) {
        val newModel = CarouselAnimationViewValues(scaleX, scaleY, yTranslation)
        mViewAnimationValues.add(newModel)
    }
    //endregion

}