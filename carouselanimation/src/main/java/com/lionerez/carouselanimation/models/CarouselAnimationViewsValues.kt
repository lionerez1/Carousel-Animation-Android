package com.lionerez.carouselanimation.models

import com.lionerez.carouselanimation.extensions.getNextScale

class CarouselAnimationViewsValues(width: Int, height: Int, size: Int) {
    //region Members
    private val mOriginalWidth: Int = width
    private val mOriginalHeight: Int = height
    private val mViewAnimationValues: ArrayList<CarouselAnimationViewValues> = ArrayList()
    private val mSize: Int = size
    private val mHorizontalMargins: Int
    private val mVerticalMargins: Int
    //endregion

    init {
        mVerticalMargins = (mOriginalHeight * 0.1 * mSize).toInt()
        mHorizontalMargins = (mOriginalWidth * 0.1).toInt()
        var lastCalculatedWidth: Int = mOriginalWidth
        var lastCalculatedHeight: Int = mOriginalHeight
        addViewAnimationValues(1f, 1f, 0f)
        for (i in 1 until mSize) {
            val results: CarouselAnimationNewValuesResult = createNewViewValues(lastCalculatedWidth, lastCalculatedHeight)
            lastCalculatedHeight = results.getNewHeight()
            lastCalculatedWidth = results.getNewWidth()
        }
    }

    //region Public Methods
    fun getAnimationViewValuesByPosition(index: Int): CarouselAnimationViewValues {
        return mViewAnimationValues[index]
    }

    fun getFirstViewValues(): CarouselAnimationViewValues {
        return mViewAnimationValues[0]
    }

    fun getLastViewValues(): CarouselAnimationViewValues {
        val itemIndex: Int = mViewAnimationValues.size - 1
        return mViewAnimationValues[itemIndex]
    }

    fun getHorizontalMargins(): Int {
        return mHorizontalMargins
    }

    fun getVerticalMargins(): Int {
        return mVerticalMargins
    }
    //endregion

    //region Private Methods
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