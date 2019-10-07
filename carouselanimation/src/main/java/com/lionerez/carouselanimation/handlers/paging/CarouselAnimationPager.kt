package com.lionerez.carouselanimation.handlers.paging

import com.lionerez.carouselanimation.main.CarouselAnimationViewModel

class CarouselAnimationPager(viewModel: CarouselAnimationViewModel) {
    //region Members
    private val mTotalSize: Int = viewModel.getSize()
    private val mViewsSize: Int = viewModel.getNumberOfViews()
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

    //region Setters
    fun setNextFirstViewIndex() {
        mCurrentFirstViewIndex++
        validateCurrentFirstViewIndex(true)
    }

    fun setPreviousFirstViewIndex() {
        mCurrentFirstViewIndex--
        validateCurrentFirstViewIndex(false)
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
    //endregion

    //region Private Methods
    private fun validateCurrentFirstViewIndex(isAfterNextAction: Boolean) {
        if (isAfterNextAction) {
            if (mCurrentFirstViewIndex == mTotalSize) {
                mCurrentFirstViewIndex = 0
            }
        } else {
            if (mCurrentFirstViewIndex < 0) {
                mCurrentFirstViewIndex = mTotalSize - 1
            }
        }
    }
    //endregion
}