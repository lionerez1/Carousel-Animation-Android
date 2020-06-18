package com.lionerez.carouselanimation.wrappers

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.view.View
import android.widget.FrameLayout

internal class CarouselAnimationShadowImageViewWrapper(context: Context, wrappedView: View) : FrameLayout(context) {
    //region Members
    private val mChild: View = wrappedView
    //endregion

    init {
        id = View.generateViewId()
        addView(mChild)
    }

    //region Public Methods
    fun setConstraints(parent: ConstraintLayout, constraintToViewId: Int, topMargin: Int) {
        val constraintSet = ConstraintSet()
        constraintSet.clone(parent)
        constraintSet.connect(id, ConstraintSet.LEFT, constraintToViewId, ConstraintSet.LEFT)
        constraintSet.connect(id, ConstraintSet.RIGHT, constraintToViewId, ConstraintSet.RIGHT)
        constraintSet.connect(id, ConstraintSet.TOP, constraintToViewId, ConstraintSet.BOTTOM, topMargin)
        constraintSet.applyTo(parent)
    }

    fun handleMoveEvent(distance: Int) {
        val calculatedDistance: Float = distance.toFloat() / 1000
        val newXScale = 1f + calculatedDistance
        scaleX = newXScale
    }

    fun playAnimation() {
        animate().scaleX(0f).duration = 100
    }

    fun resetMoveEvent() {
        scaleX = 1f
    }
    //endregion
}