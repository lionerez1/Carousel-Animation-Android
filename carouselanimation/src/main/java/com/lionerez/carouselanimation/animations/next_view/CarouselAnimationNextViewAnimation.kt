package com.lionerez.carouselanimation.animations.next_view

import android.content.Context
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import com.lionerez.carouselanimation.animations.CarouselAnimationXRotateAnimation
import com.lionerez.carouselanimation.animations.base.CarouselAnimationViewAnimation
import com.lionerez.carouselanimation.models.CarouselAnimationViewValues

internal class CarouselAnimationNextViewAnimation(context: Context, view: View, lastViewValues: CarouselAnimationViewValues ,contract: CarouselAnimationNextViewAnimationContract) :
    CarouselAnimationViewAnimation(context, view) {
    //region Members
    private val mContract: CarouselAnimationNextViewAnimationContract = contract
    private val mLastViewValues: CarouselAnimationViewValues = lastViewValues
    private val mScaleAnimationDelay: Long = mDuration / 4
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
            4 -> playFourthAnimationStep()
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
        mView.startAnimation(animationSet)
    }

    private fun secondStepDone() {
        mCurrentAnimationStep++
        playAnimationStep()
    }

    private fun playThirdAnimationStep() {
        mContract.onNextAnimationSecondStepCompleted()
        val animationSet = createAnimationSet()
        val translateAnimation = createTranslateAnimation(mLastViewValues.getYTranslation(), mDuration)
        val scaleAnimation = createScaleAnimation(mLastViewValues.getScaleX(), mLastViewValues.getScaleY(), mDuration)
        animationSet.addAnimation(translateAnimation)
        animationSet.addAnimation(scaleAnimation)
        animationSet.setAnimationListener(AnimationStepListener(::thirdStepDone))
        mView.startAnimation(animationSet)
    }

    private fun thirdStepDone() {
        mCurrentAnimationStep++
        playAnimationStep()
    }

    private fun playFourthAnimationStep() {
        val animationSet = createAnimationSet()
        val xRotationAnimation: CarouselAnimationXRotateAnimation = createXRotateAnimation(-3f, 0)
        animationSet.addAnimation(xRotationAnimation)
        animationSet.setAnimationListener(AnimationStepListener(::fourthStepDone))
        mView.startAnimation(animationSet)
    }

    private fun fourthStepDone() {
        mCurrentAnimationStep = 0
        mContract.onNextAnimationCompleted()
    }
    //endregion
}