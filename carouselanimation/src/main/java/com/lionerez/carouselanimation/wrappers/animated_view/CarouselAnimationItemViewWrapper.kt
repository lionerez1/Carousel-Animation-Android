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

class CarouselAnimationItemViewWrapper(context: Context, wrappedView: View, contract: CarouselAnimationItemViewWrapperContract) :
    FrameLayout(context),
    CarouselAnimationWrapperAnimationsHandlerContract {
    //region Members
    private var mWrappedView: View = wrappedView
    private val mContract: CarouselAnimationItemViewWrapperContract = contract
    private val mNextMovementTransformer: CarouselAnimationNextMovementWrapperTransformer
    private val mPreviousMovementTransformer: CarouselAnimationPreviousMovementWrapperTransformer
    private val mAnimationsHandler: CarouselAnimationWrapperAnimationsHandler
    private var mAnimationValues: CarouselAnimationViewValues? = null
    //endregion

    init {
        id = View.generateViewId()
        addView(mWrappedView)
        mNextMovementTransformer = CarouselAnimationNextMovementWrapperTransformer(this)
        mPreviousMovementTransformer = CarouselAnimationPreviousMovementWrapperTransformer(this)
        mAnimationsHandler = CarouselAnimationWrapperAnimationsHandler(context, this, this)
    }

    //region Implementations
    override fun startSecondaryAnimations(isNextAnimation: Boolean) {
        if (isNextAnimation) {
            mContract.startPlayingNextAnimationSecondaryAnimations()
        } else {
            mContract.startPlayingPreviousAnimationSecondaryAnimations()
        }
    }

    override fun onNextAnimationSecondaryAnimationsCompleted() {
        mContract.onNextAnimationSecondaryAnimationsCompleted()
    }

    override fun onAnimationDone(isNextAnimation: Boolean) {
        if (isNextAnimation) {
            mContract.onNextAnimationDone()
        } else {
            mContract.onPreviousAnimationDone()
        }
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

    fun handleMoveEvent(distance: Int) {
        if (isNextMovement(distance)) {
            handleNextMoveEvent(distance)
        } else {
            handlePreviousMoveEvent(distance)
        }
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

    fun playSecondaryAnimation(toValuesModel: CarouselAnimationViewValues) {
        mAnimationValues = toValuesModel
        val animation = CarouselAnimationSecondaryViewAnimation(context, this, toValuesModel)
        animation.play()
    }
    //endregion

    //region Private Methods
    private fun isNextMovement(distance: Int): Boolean {
        return !distance.isGreaterThanZero()
    }

    private fun handleNextMoveEvent(distance: Int) {
        val positiveDistance: Int = abs(distance)
        if (positiveDistance in 1..100) {
            mNextMovementTransformer.handleEvent(positiveDistance)
        } else if (positiveDistance > 100) {
            mContract.onNextAnimationStart()
            mAnimationsHandler.playNextViewAnimation(mContract.getLastViewScaleModel())
        }
    }

    private fun handlePreviousMoveEvent(distance: Int) {
        if (distance in 1..200) {
            mPreviousMovementTransformer.handleEvent(distance)
        } else {
            mContract.onPreviousAnimationStarted()
            mAnimationsHandler.playPreviousViewAnimation(mContract.getFirstViewScaleModel())
        }
    }
    //endregion
}