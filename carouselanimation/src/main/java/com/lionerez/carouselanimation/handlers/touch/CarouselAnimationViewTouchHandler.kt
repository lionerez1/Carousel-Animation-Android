package com.lionerez.carouselanimation.handlers.touch

import android.util.Log
import android.view.MotionEvent
import android.view.View

class CarouselAnimationViewTouchHandler(contract: CarouselAnimationViewTouchHandlerContract) : View.OnTouchListener {
    //region Members
    private val mContract: CarouselAnimationViewTouchHandlerContract = contract
    private var mDistance: Float = 0f
    private var mStartingYPoint: Float = 0f
    //endregion

    //region Private Methods
    private fun startTracking(event: MotionEvent) {
        mDistance = 0f
        mStartingYPoint = event.y
    }

    private fun handleMoveAction(event: MotionEvent) {
        mDistance = mStartingYPoint - event.y
        mContract.onMoved(mDistance.toInt())
    }

    private fun stopTracking() {
        mContract.onTouchEnd(mDistance.toInt())
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