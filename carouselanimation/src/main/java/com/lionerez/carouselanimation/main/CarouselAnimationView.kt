package com.lionerez.carouselanimation.main

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.lionerez.carouselanimation.extensions.isGreaterThanZero
import com.lionerez.carouselanimation.extensions.toDp
import com.lionerez.carouselanimation.handlers.animations.CarouselAnimationWrapperAnimationsHandler
import com.lionerez.carouselanimation.handlers.animations.CarouselAnimationWrapperAnimationsHandlerContract
import com.lionerez.carouselanimation.handlers.paging.CarouselAnimationPager
import com.lionerez.carouselanimation.handlers.touch.CarouselAnimationViewTouchHandler
import com.lionerez.carouselanimation.handlers.touch.CarouselAnimationViewTouchHandlerContract
import com.lionerez.carouselanimation.models.CarouselAnimationViewValues
import com.lionerez.carouselanimation.models.CarouselAnimationValues
import com.lionerez.carouselanimation.wrappers.CarouselAnimationItemViewWrapper
import com.lionerez.carouselanimation.wrappers.CarouselAnimationShadowImageViewWrapper
import kotlin.math.abs

class CarouselAnimationView(context: Context?, attrs: AttributeSet?) :
    ConstraintLayout(context, attrs),
    CarouselAnimationViewTouchHandlerContract,
    CarouselAnimationWrapperAnimationsHandlerContract {
    //region Members
    private val mViews = ArrayList<CarouselAnimationItemViewWrapper>()
    private lateinit var mAnimationsHandler: CarouselAnimationWrapperAnimationsHandler
    private lateinit var mTouchHandler: CarouselAnimationViewTouchHandler
    private lateinit var mContract: CarouselAnimationViewContract
    private lateinit var mPager: CarouselAnimationPager
    private lateinit var mViewModel: CarouselAnimationViewModel
    private lateinit var mViewsValues: CarouselAnimationValues
    private var mShadowImageView: CarouselAnimationShadowImageViewWrapper? = null
    private var isAnimationPlaying = false
    private var nextAnimationsWaiting = 0
    private var previousAnimationsWaiting = 0
    //endregion

    //region Implementations

    //region CarouselAnimationViewTouchHandlerContract
    override fun onYMoved(newDistance: Int) {
        if (!isAnimationPlaying) {
            if (isNextMovement(newDistance)) {
                val positiveDistance: Int = abs(newDistance)
                handleNextMoveEvent(positiveDistance.toDp())
            } else {
                handlePreviousMoveEvent(newDistance.toDp())
            }
        }
    }

    override fun onXMoved(newDistance: Int) {
        if (isNextMovement(newDistance)) {
            handleNextSwipe(newDistance)
        } else {
            handlePreviousSwipe(newDistance)
        }
    }

    override fun onTouchEnd(lastCalculatedDistance: Int) {
        if (!isAnimationPlaying) {
            if (isNextMovement(lastCalculatedDistance)) {
                getFirstWrapper().resetMoveEventTransforms()
                if (mShadowImageView != null) {
                    mShadowImageView!!.resetMoveEvent()
                }
            } else {
                getLastWrapper().resetPreviousMoveEventTransforms()
            }
        }
    }
    //endregion

    //region CarouselAnimationWrapperAnimationsHandlerContract
    override fun startSecondaryAnimations(isNextAnimation: Boolean) {
        if (isNextAnimation) {
            startPlayingNextSecondaryAnimation()
        } else {
            startPlayingPreviousSecondaryAnimation()
        }
    }

    override fun onNextAnimationSecondaryAnimationsCompleted() {
        val wrapper: CarouselAnimationItemViewWrapper = getFirstWrapper()
        mPager.next()
        val newView: View = mContract.bindView(mPager.getLastVisibleItemIndex(), wrapper.mWrappedView)
        wrapper.mWrappedView = newView
    }

    override fun onAnimationDone(isNextAnimation: Boolean) {
        if (isNextAnimation) {
            handleNextAnimationDone()
        } else {
            handlePreviousAnimationDone()
        }
    }
    //endregion

    //endregion

    //region Public Methods
    fun initialize(viewModel: CarouselAnimationViewModel, contract: CarouselAnimationViewContract) {
        mViewModel = viewModel
        mContract = contract
        init()
    }

    fun setBottomShadow(shadowView: View) {
        mShadowImageView =
            CarouselAnimationShadowImageViewWrapper(
                context,
                shadowView
            )
        createBottomShadow()
    }
    //endregion

    //region Private Methods
    private fun init() {
        mPager = CarouselAnimationPager(mViewModel)
        createSubViews()
        post {
            handleViewReady()
        }
    }

    private fun createSubViews() {
        for (i in 0 until mViewModel.mNumberOfViews) {
            val view: View = mContract.bindView(i,View(context))
            val wrapper = CarouselAnimationItemViewWrapper(
                context,
                view
            )
            mViews.add(wrapper)
            addView(wrapper)
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

    private fun getFirstWrapper(): CarouselAnimationItemViewWrapper {
        return mViews[0]
    }

    private fun getLastWrapper(): CarouselAnimationItemViewWrapper {
        return mViews[mViews.size - 1]
    }

    private fun handleViewReady() {
        initializeValues()
        setViewTouchListener()
        for (i in 0 until mViewModel.mNumberOfViews) {
            handleItem(mViews[i], mViewsValues.mViewAnimationValues[i])
        }
    }

    private fun initializeValues() {
        val wrapper: CarouselAnimationItemViewWrapper = getFirstWrapper()
        mViewsValues = CarouselAnimationValues(wrapper.width, wrapper.height, mViewModel.mNumberOfViews)
    }

    private fun handleItem(wrapper: CarouselAnimationItemViewWrapper, valuesModel: CarouselAnimationViewValues) {
        wrapper.setCenterConstraints(this, mViewsValues.mVerticalMargins, mViewsValues.mHorizontalMargins)
        wrapper.setViewTransforms(valuesModel)
    }

    private fun createBottomShadow() {
        val firstWrapper: CarouselAnimationItemViewWrapper = getFirstWrapper()
        firstWrapper.post {
            addView(mShadowImageView)
            mShadowImageView!!.setConstraints(this, firstWrapper.id, mViewsValues.mVerticalMargins)
        }
    }

    private fun startPlayingNextSecondaryAnimation() {
        for (i in mViews.size - 1 downTo 1) {
            val currentWrapper = mViews[i]
            val nextScaleIndex = i - 1
            val nextScaleModel = mViewsValues.mViewAnimationValues[nextScaleIndex]
            mAnimationsHandler.playSecondaryAnimation(currentWrapper, nextScaleModel)
        }
    }

    private fun startPlayingPreviousSecondaryAnimation() {
        val lastWrapper: CarouselAnimationItemViewWrapper = getLastWrapper()
        for (i in mViews.size - 2 downTo 0) {
            val currentWrapper = mViews[i]
            if (currentWrapper != lastWrapper) {
                var nextScaleIndex = i + 1
                if (nextScaleIndex >= mViews.size) {
                    nextScaleIndex -= mViews.size
                }
                val nextScaleModel = mViewsValues.mViewAnimationValues[nextScaleIndex]
                mAnimationsHandler.playSecondaryAnimation(currentWrapper, nextScaleModel)
            }
        }
        lastWrapper.bringToFront()
    }

    private fun handleNextMoveEvent(distance: Int) {
        if (mViewsValues.isDistanceInNextMovementEventRange(distance)) {
            getFirstWrapper().handleNextMoveEvent(distance)
            if (mShadowImageView != null) {
                mShadowImageView!!.handleMoveEvent(distance)
            }
        } else if (distance > mViewsValues.mTouchEventNextMaximumDistance) {
            playNextAnimation()
        }
    }

    private fun handlePreviousMoveEvent(distance: Int) {
        if (mViewsValues.isDistanceInPreviousMovementEventRange(distance)) {
            getLastWrapper().handlePreviousMoveEvent(distance)
        } else if (distance > mViewsValues.mTouchEventPreviousMaximumDistance) {
            playPreviousAnimation()
        }
    }

    private fun handleNextSwipe(distance: Int) {
        if (mViewsValues.isXDistanceGreaterThanMaximum(distance)) {
            if (isAnimationPlaying) {
                 nextAnimationsWaiting++
            } else {
                playNextAnimation()
            }
        }
    }

    private fun handlePreviousSwipe(distance: Int) {
        if (mViewsValues.isXDistanceGreaterThanMaximum(distance)) {
            if (isAnimationPlaying) {
                previousAnimationsWaiting++
            } else {
                playPreviousAnimation()
            }
        }
    }

    private fun playNextAnimation() {
        isAnimationPlaying = true
        mAnimationsHandler = CarouselAnimationWrapperAnimationsHandler(context, getFirstWrapper(), this)
        mAnimationsHandler.playNextViewAnimation(mViewsValues.getLastViewValues())
        if (mShadowImageView != null) {
            mShadowImageView!!.playAnimation()
        }
    }

    private fun playPreviousAnimation() {
        isAnimationPlaying = true
        mAnimationsHandler = CarouselAnimationWrapperAnimationsHandler(context, getLastWrapper(), this)
        mAnimationsHandler.playPreviousViewAnimation(mViewsValues.getFirstViewValues())
        if (mPager.isNeedPaging()) {
            mPager.previous()
            val newView: View = mContract.bindView(mPager.getCurrentFirstViewIndex(), getLastWrapper().mWrappedView)
            getLastWrapper().mWrappedView = newView
        }
    }

    private fun handleNextAnimationDone() {
        reOrderViewsAfterNextCompleted()
        if (playNextWaitingAnimationIfExist()) {
            return
        }

        if (playPreviousWaitingAnimationIfExist()) {
            return
        }

        isAnimationPlaying = false
        if (mShadowImageView != null) {
            mShadowImageView!!.resetMoveEvent()
        }
    }

    private fun handlePreviousAnimationDone() {
        reOrderViewsAfterPreviousCompleted()
        if (playNextWaitingAnimationIfExist()) {
            return
        }

        if (playPreviousWaitingAnimationIfExist()) {
            return
        }
        isAnimationPlaying = false
    }

    private fun isNextMovement(distance: Int): Boolean {
        return !distance.isGreaterThanZero()
    }

    private fun reOrderViewsAfterNextCompleted() {
        val lastItemIndex: Int = mViews.size - 1
        val tempView: CarouselAnimationItemViewWrapper = getFirstWrapper()
        for (i in 0 until mViews.size - 1) {
            val nextViewIndex: Int = i + 1
            val nextView: CarouselAnimationItemViewWrapper = mViews[nextViewIndex]
            val newViewValues: CarouselAnimationViewValues = mViewsValues.mViewAnimationValues[i]
            mViews[i] = nextView
            mViews[i].setViewAnimationValues(newViewValues)
            if (nextViewIndex == lastItemIndex) {
                val nextViewNewValues: CarouselAnimationViewValues = mViewsValues.mViewAnimationValues[nextViewIndex]
                mViews[nextViewIndex] = tempView
                mViews[nextViewIndex].setViewAnimationValues(nextViewNewValues)
            }
        }
    }

    private fun reOrderViewsAfterPreviousCompleted() {
        val tempView: CarouselAnimationItemViewWrapper = getLastWrapper()
        for (i in mViews.size - 1 downTo 1) {
            val nextViewIndex: Int = i - 1
            val nextView: CarouselAnimationItemViewWrapper = mViews[nextViewIndex]
            val newViewValues: CarouselAnimationViewValues = mViewsValues.mViewAnimationValues[i]
            mViews[i] = nextView
            mViews[i].setViewAnimationValues(newViewValues)
            if (nextViewIndex == 0) {
                val nextViewNewValues: CarouselAnimationViewValues = mViewsValues.mViewAnimationValues[nextViewIndex]
                mViews[nextViewIndex] = tempView
                mViews[nextViewIndex].setViewAnimationValues(nextViewNewValues)
            }
        }
    }

    private fun playNextWaitingAnimationIfExist(): Boolean {
        if (nextAnimationsWaiting > 0) {
            nextAnimationsWaiting--
            playNextAnimation()
            return true
        }
        return false
    }

    private fun playPreviousWaitingAnimationIfExist(): Boolean {
        if (previousAnimationsWaiting > 0) {
            previousAnimationsWaiting--
            playPreviousAnimation()
            return true
        }
        return false
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setViewTouchListener() {
        mTouchHandler = CarouselAnimationViewTouchHandler(mViewsValues.mSideTouchEventMaxDistance, this)
        setOnTouchListener(mTouchHandler)
    }
    //endregion
}