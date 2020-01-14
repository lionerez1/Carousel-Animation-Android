package com.lionerez.carouselanimation.main

class CarouselAnimationViewModel(numberOfViews: Int, size: Int) {
    //region Members
    var mNumberOfViews: Int = numberOfViews
    val mTotalSize: Int = size
    //endregion

    init {
        if (mTotalSize < numberOfViews) {
            mNumberOfViews = mTotalSize
        }
    }
}