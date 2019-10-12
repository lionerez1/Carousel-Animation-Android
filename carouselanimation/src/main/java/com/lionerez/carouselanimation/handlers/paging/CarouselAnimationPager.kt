package com.lionerez.carouselanimation.handlers.paging

import com.lionerez.carouselanimation.main.CarouselAnimationViewModel

internal class CarouselAnimationPager(viewModel: CarouselAnimationViewModel) {
    //region Members
    private val mTotalSize: Int = viewModel.mTotalSize
    private val mViewsSize: Int = viewModel.mNumberOfViews
    private var mIsPagerNeeded: Boolean = false
    private var mCurrentFirstViewIndex = 0
    //endregion

    init {
        mIsPagerNeeded = mTotalSize > mViewsSize
    }

    //region Getters
    fun isNeedPaging(): Boolean {
        return mIsPagerNeeded
    }

    fun getCurrentFirstViewIndex(): Int {
        return mCurrentFirstViewIndex
    }

    //endregion

    //region Public Methods
    fun getLastVisibleItemIndex(): Int {
        val index: Int = mCurrentFirstViewIndex + mViewsSize - 1
        return if(index < mTotalSize) {
            index
        } else {
            index - mTotalSize
        }

    }

    fun next() {
        mCurrentFirstViewIndex++
        validateNext()
    }

    fun previous() {
        mCurrentFirstViewIndex--
        validatePrevious()
    }
    //endregion

    //region Private Methods
    private fun validateNext() {
        if (mCurrentFirstViewIndex == mTotalSize) {
            mCurrentFirstViewIndex = 0
        }
    }

    private fun validatePrevious() {
        if (mCurrentFirstViewIndex < 0) {
            mCurrentFirstViewIndex = mTotalSize - 1
        }
    }
    //endregion
}