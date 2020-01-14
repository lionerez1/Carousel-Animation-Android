package com.lionerez.carouselanimation.handlers.touch

import android.view.MotionEvent
import android.view.View
import com.lionerez.carouselanimation.extensions.isGreaterThanZero
import com.lionerez.carouselanimation.extensions.toDp
import kotlin.math.abs

internal class CarouselAnimationViewTouchHandler(sideTouchEventMaxDistance: Int ,contract: CarouselAnimationViewTouchHandlerContract) : View.OnTouchListener {
    //region Members
    private val mContract: CarouselAnimationViewTouchHandlerContract = contract
    private var mYDistance: Float = 0f
    private var mXDistance: Float = 0f
    private var mStartingYPoint: Float = 0f
    private var mStartingXPoint: Float = 0f
    var mShouldNotifySwipe = true
    var mIsAnimationPlaying = false
    private val mSideTouchEventMaxDistance: Int = sideTouchEventMaxDistance
    //endregion

    //region Private Methods
    private fun startTracking(event: MotionEvent) {
        mYDistance = 0f
        mStartingYPoint = event.y
        mStartingXPoint = event.x
        mShouldNotifySwipe = true
    }

    private fun handleMoveAction(event: MotionEvent) {
        mYDistance = mStartingYPoint - event.y
        mXDistance = mStartingXPoint - event.x
        notifyYDistanceIfNeeded()
        notifyXDistanceIfNeeded()
    }

    private fun stopTracking() {
        if (isNextMovement(mYDistance.toInt())) {
            mContract.resetNextAnimation()
        } else {
            mContract.resetPreviousAnimation()
        }
    }

    private fun notifyYDistanceIfNeeded() {
        if (mShouldNotifySwipe) {
            handleYMovement(mYDistance.toInt())
        }
    }

    private fun notifyXDistanceIfNeeded() {
        if (mShouldNotifySwipe) {
            val yPositiveDistance = abs(mYDistance)
            if (yPositiveDistance < 20) {
                val xPositiveDistance = abs(mXDistance)
                if (xPositiveDistance > mSideTouchEventMaxDistance) {
                    mShouldNotifySwipe = false
                }
                handleXMovement(mXDistance.toInt())
            }
        }
    }

    private fun handleYMovement(distance: Int) {
        if (!mIsAnimationPlaying) {
            if (isNextMovement(distance)) {
                val positiveDistance: Int = abs(distance)
                mContract.handleNextMovement(positiveDistance.toDp())
            } else {
                mContract.handlePreviousMovement(distance.toDp())
            }
        }
    }

    private fun handleXMovement(distance: Int) {
        if (isNextMovement(distance)) {
            mContract.handleNextSwipe(distance)
        } else {
            mContract.handlePreviousSwipe(distance)
        }
    }

    private fun isNextMovement(distance: Int): Boolean {
        return !distance.isGreaterThanZero()
    }
    //endregion

    //region Implementations
    override fun onTouch(view: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN ->
                startTracking(event)
            MotionEvent.ACTION_MOVE ->
                handleMoveAction(event)
            MotionEvent.ACTION_UP ->
                stopTracking()
        }

        return true
    }
    //endregion
}