package com.lionerez.carouselanimation.wrappers

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.lionerez.carouselanimation.models.CarouselAnimationViewValues
import com.lionerez.carouselanimation.transformers.CarouselAnimationNextMovementTransformer
import com.lionerez.carouselanimation.transformers.CarouselAnimationPreviousMovementTransformer
import kotlin.math.abs

internal class CarouselAnimationItemViewWrapper(context: Context, wrappedView: View) : FrameLayout(context) {
    //region Members
    var mWrappedView: View = wrappedView
    private val mNextMovementTransformer: CarouselAnimationNextMovementTransformer
    private val mPreviousMovementTransformer: CarouselAnimationPreviousMovementTransformer
    private var mAnimationValues: CarouselAnimationViewValues? = null
    //endregion

    init {
        id = View.generateViewId()
        addView(mWrappedView)
        mNextMovementTransformer = CarouselAnimationNextMovementTransformer(this)
        mPreviousMovementTransformer = CarouselAnimationPreviousMovementTransformer(this)
    }

    //region Setters
    fun setViewAnimationValues(animationValues: CarouselAnimationViewValues) {
        mAnimationValues = animationValues
    }
    //endregion

    //region Public Methods
    fun setCenterConstraints(parent: ConstraintLayout, marginsVertical: Int, marginsHorizontal: Int) {
        val constraintSet = ConstraintSet()
        constraintSet.clone(parent)
        constraintSet.connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, marginsVertical)
        constraintSet.connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, marginsVertical)
        constraintSet.connect(id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, marginsHorizontal)
        constraintSet.connect(id, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, marginsHorizontal)
        constraintSet.applyTo(parent)
    }

    fun setViewTransforms(viewValues: CarouselAnimationViewValues) {
        mAnimationValues = viewValues
        rotationX = -3f
        scaleX = viewValues.getScaleX()
        scaleY = viewValues.getScaleY()
        translationY = viewValues.getYTranslation()
    }

    fun handleNextMoveEvent(distance: Int) {
        val positiveDistance: Int = abs(distance)
        mNextMovementTransformer.handleEvent(positiveDistance)
    }

    fun handlePreviousMoveEvent(distance: Int) {
        mPreviousMovementTransformer.handleEvent(distance)
    }

    fun resetMoveEventTransforms() {
        if (mAnimationValues != null) {
            scaleY = mAnimationValues!!.getScaleY()
            rotationX = -3f
            translationY = 0f
        }
    }

    fun resetPreviousMoveEventTransforms() {
        if (mAnimationValues != null) {
            translationY = mAnimationValues!!.getYTranslation()
        }
    }
    //endregion
}