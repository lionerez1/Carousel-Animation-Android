package com.lionerez.carouselanimation.adapter

import com.lionerez.carouselanimation.models.CarouselAnimationViewValues
import com.lionerez.carouselanimation.wrappers.animated_view.CarouselAnimationItemViewWrapper

class CarouselAnimationViewAdapter(size: Int, contract: CarouselAnimationViewAdapterContract) {
    //region Members
    private val mContract: CarouselAnimationViewAdapterContract = contract
    private val mViews: ArrayList<CarouselAnimationItemViewWrapper> = ArrayList()
    private val mSize: Int = size
    //endregion

    init {
        createViews()
    }

    //region Public Methods
    fun getFirstWrapper(): CarouselAnimationItemViewWrapper {
        return mViews[0]
    }

    fun getLastWrapper(): CarouselAnimationItemViewWrapper {
        return mViews[mViews.size - 1]
    }

    fun reOrderViewsAfterNextCompleted() {
        val lastItemIndex: Int = mViews.size - 1
        var tempView: CarouselAnimationItemViewWrapper = mViews[0]
        for (i in 0 until mViews.size - 1) {
            val nextViewIndex: Int = i + 1
            val nextView: CarouselAnimationItemViewWrapper = mViews[nextViewIndex]
            val newViewValues: CarouselAnimationViewValues = mContract.getViewValuesByPosition(i)
            mViews[i] = nextView
            mViews[i].setViewAnimationValues(newViewValues)
            if (nextViewIndex == lastItemIndex) {
                val nextViewNewValues: CarouselAnimationViewValues = mContract.getViewValuesByPosition(nextViewIndex)
                mViews[nextViewIndex] = tempView
                mViews[nextViewIndex].setViewAnimationValues(nextViewNewValues)
            }
        }
    }

    fun reOrderViewsAfterPreviousCompleted() {
        val tempView: CarouselAnimationItemViewWrapper = mViews[mViews.size - 1]
        for (i in mViews.size - 1 downTo 1) {
            val nextViewIndex: Int = i - 1
            val nextView: CarouselAnimationItemViewWrapper = mViews[nextViewIndex]
            val newViewValues: CarouselAnimationViewValues = mContract.getViewValuesByPosition(i)
            mViews[i] = nextView
            mViews[i].setViewAnimationValues(newViewValues)
            if (nextViewIndex == 0) {
                val nextViewNewValues: CarouselAnimationViewValues = mContract.getViewValuesByPosition(nextViewIndex)
                mViews[nextViewIndex] = tempView
                mViews[nextViewIndex].setViewAnimationValues(nextViewNewValues)
            }
        }
    }

    fun getWrapperByPosition(index: Int): CarouselAnimationItemViewWrapper {
        return mViews[index]
    }

    fun getWrappers(): ArrayList<CarouselAnimationItemViewWrapper> {
        return mViews
    }
    //endregion

    //region Private Methods
    private fun createViews() {
        for (i in 0 until mSize) {
            val view: CarouselAnimationItemViewWrapper = mContract.createView(i)
            mViews.add(view)
            reverseViewsOrderOnScreen()
        }
    }

    private fun reverseViewsOrderOnScreen() {
        val lastIndex: Int = mViews.size - 1
        for (i in 0 until mViews.size) {
            val reversedIndex: Int = lastIndex - i
            mViews[reversedIndex].bringToFront()
        }
    }
    //endregion
}