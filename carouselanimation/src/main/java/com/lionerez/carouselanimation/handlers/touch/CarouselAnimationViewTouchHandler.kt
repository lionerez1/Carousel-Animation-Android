package com.lionerez.carouselanimation.handlers.touch

import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs

internal class CarouselAnimationViewTouchHandler(sideTouchEventMaxDistance: Int ,contract: CarouselAnimationViewTouchHandlerContract) : View.OnTouchListener {
    //region Members
    private val mContract: CarouselAnimationViewTouchHandlerContract = contract
    private var mYDistance: Float = 0f
    private var mXDistance: Float = 0f
    private var mStartingYPoint: Float = 0f
    private var mStartingXPoint: Float = 0f
    private var mShouldNotifySwipe = true
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
        mContract.onYMoved(mYDistance.toInt())
        notifyXDistanceIfNeeded()
    }

    private fun stopTracking() {
        mContract.onTouchEnd(mYDistance.toInt())
    }

    private fun notifyXDistanceIfNeeded() {
        if (mShouldNotifySwipe) {
            val yPositiveDistance = abs(mYDistance)
            if (yPositiveDistance < 20) {
                val xPositiveDistance = abs(mXDistance)
                if (xPositiveDistance > mSideTouchEventMaxDistance) {
                    mShouldNotifySwipe = false
                }
                mContract.onXMoved(mXDistance.toInt())
            }
        }
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