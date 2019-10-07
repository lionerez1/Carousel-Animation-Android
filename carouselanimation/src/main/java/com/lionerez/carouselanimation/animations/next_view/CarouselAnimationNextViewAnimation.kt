package com.lionerez.carouselanimation.animations.next_view

import android.content.Context
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import com.lionerez.carouselanimation.animations.base.CarouselAnimationViewAnimation
import com.lionerez.carouselanimation.models.CarouselAnimationViewValues

class CarouselAnimationNextViewAnimation(
    context: Context,
    view: View,
    contract: CarouselAnimationNextViewAnimationContract
) :
    CarouselAnimationViewAnimation(context, view) {
    //region Members
    private val mScaleAnimationDelay: Long = mDuration / 4
    private val mContract: CarouselAnimationNextViewAnimationContract = contract
    private var mCurrentAnimationStep: Int = 0
    //endregion

    //region Implementations
    private class AnimationStepListener(callback: () -> Unit) : Animation.AnimationListener {
        private val mCallBack = callback


        override fun onAnimationRepeat(p0: Animation?) {
        }

        override fun onAnimationEnd(p0: Animation?) {
            mCallBack()
        }

        override fun onAnimationStart(p0: Animation?) {
        }

    }
    //endregion

    //region Public Methods
    fun play() {
        mCurrentAnimationStep++
        playAnimationStep()
    }
    //endregion

    //region Private Methods
    private fun playAnimationStep() {
        when (mCurrentAnimationStep) {
            1 -> playFirstAnimationStep()
            2 -> playSecondAnimationStep()
            3 -> playThirdAnimationStep()
        }
    }

    private fun playFirstAnimationStep() {
        val animationSet = createAnimationSet()
        val translateAnimation = createTranslateAnimation(300f, mDuration)
        val rotateAnimation = createXRotateAnimation(-120f, mDuration)
        val scaleAnimation = createScaleXAnimation(mDuration - mScaleAnimationDelay, mScaleAnimationDelay)
        animationSet.addAnimation(translateAnimation)
        animationSet.addAnimation(rotateAnimation)
        animationSet.addAnimation(scaleAnimation)
        animationSet.setAnimationListener(AnimationStepListener(::firstStepDone))
        animationSet.interpolator = AccelerateDecelerateInterpolator()
        mView.startAnimation(animationSet)
    }

    private fun firstStepDone() {
        mContract.onNextAnimationFirstStepCompleted()
        mCurrentAnimationStep++
        playAnimationStep()
    }

    private fun playSecondAnimationStep() {
        val animationSet = createAnimationSet()
        val translateAnimation = createTranslateAnimation(50f, mDuration)
        val xRotateAnimation = createXRotateAnimation(-180f, mDuration)
        animationSet.addAnimation(translateAnimation)
        animationSet.addAnimation(xRotateAnimation)
        animationSet.setAnimationListener(AnimationStepListener(::secondStepDone))
        animationSet.interpolator = AccelerateDecelerateInterpolator()
        mView.startAnimation(animationSet)
    }

    private fun secondStepDone() {
        mCurrentAnimationStep++
        playAnimationStep()
    }

    private fun playThirdAnimationStep() {
        val lastViewValues: CarouselAnimationViewValues = mContract.onNextAnimationSecondStepCompleted()
        val animationSet = createAnimationSet()
        val translateAnimation = createTranslateAnimation(lastViewValues.getYTranslation(), mDuration)
        val scaleAnimation = createScaleAnimation(lastViewValues.getScaleX(), lastViewValues.getScaleY(), mDuration)
        animationSet.addAnimation(translateAnimation)
        animationSet.addAnimation(scaleAnimation)
        animationSet.setAnimationListener(AnimationStepListener(::thirdStepDone))
        animationSet.interpolator = AccelerateDecelerateInterpolator()
        mView.startAnimation(animationSet)
    }

    private fun thirdStepDone() {
        mCurrentAnimationStep = 0
        mContract.onNextAnimationCompleted()
    }
    //endregion
}