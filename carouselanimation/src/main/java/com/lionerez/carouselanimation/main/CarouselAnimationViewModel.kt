package com.lionerez.carouselanimation.main

class CarouselAnimationViewModel(numberOfViews: Int, size: Int) {
    //region Members
    private val mNumberOfViews: Int = numberOfViews
    private val mSize: Int = size
    //endregion

    //region Getters
    fun getNumberOfViews(): Int {
        return mNumberOfViews
    }

    fun getSize(): Int {
        return mSize
    }
    //endregion
}