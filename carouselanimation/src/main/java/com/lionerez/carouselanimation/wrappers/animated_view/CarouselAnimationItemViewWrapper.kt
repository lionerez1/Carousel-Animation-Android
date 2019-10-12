package com.lionerez.carouselanimation.wrappers.animated_view

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.lionerez.carouselanimation.animations.secondary_scale.CarouselAnimationSecondaryViewAnimation
import com.lionerez.carouselanimation.extensions.isGreaterThanZero
import com.lionerez.carouselanimation.models.CarouselAnimationViewValues
import com.lionerez.carouselanimation.handlers.animations.CarouselAnimationWrapperAnimationsHandler
import com.lionerez.carouselanimation.handlers.animations.CarouselAnimationWrapperAnimationsHandlerContract
import com.lionerez.carouselanimation.transformers.CarouselAnimationNextMovementWrapperTransformer
import com.lionerez.carouselanimation.transformers.CarouselAnimationPreviousMovementWrapperTransformer
import kotlin.math.abs

internal class CarouselAnimationItemViewWrapper(context: Context, wrappedView: View) : FrameLayout(context) {
    //region Members
    private var mWrappedView: View = wrappedView
    private val mNextMovementTransformer: CarouselAnimationNextMovementWrapperTransformer
    private val mPreviousMovementTransformer: CarouselAnimationPreviousMovementWrapperTransformer
    private var mAnimationValues: CarouselAnimationViewValues? = null
    //endregion

    init {
        id = View.generateViewId()
        addView(mWrappedView)
        mNextMovementTransformer = CarouselAnimationNextMovementWrapperTransformer(this)
        mPreviousMovementTransformer = CarouselAnimationPreviousMovementWrapperTransformer(this)
    }
    //endregion

    //region Getters
    fun getWrappedView(): View {
        return mWrappedView
    }
    //endregion

    //region Setters
    fun setWrappedView(view: View) {
        removeAllViews()
        mWrappedView = view
        addView(mWrappedView)
    }

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