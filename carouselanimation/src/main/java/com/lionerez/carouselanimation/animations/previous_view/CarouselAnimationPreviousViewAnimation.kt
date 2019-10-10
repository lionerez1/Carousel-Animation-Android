package com.lionerez.carouselanimation.animations.previous_view

import android.content.Context
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationSet
import com.lionerez.carouselanimation.animations.CarouselAnimationYTranslationAnimation
import com.lionerez.carouselanimation.animations.base.CarouselAnimationViewAnimation
import com.lionerez.carouselanimation.models.CarouselAnimationViewValues

class CarouselAnimationPreviousViewAnimation (context: Context, view: View, toScaleModel: CarouselAnimationViewValues, contract: CarouselAnimationPreviousViewAnimationContract) : CarouselAnimationViewAnimation(context, view) {
    //region Members
    private val mContract: CarouselAnimationPreviousViewAnimationContract = contract
    private val mToScaleModel: CarouselAnimationViewValues = toScaleModel
    private var mCurrentAnimationStep: Int = 0
    //endregion

    //region Implementations
    private class AnimationStepListener(callback: () -> Unit) : Animation.AnimationListener {
        private val mCallback = callback

        override fun onAnimationRepeat(p0: Animation?) {
        }

        override fun onAnimationEnd(p0: Animation?) {
            mCallback()
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
        val animationSet: AnimationSet = createAnimationSet()
        val translateAnimationSet: CarouselAnimationYTranslationAnimation = createTranslateAnimation(200f, mDuration)
        val rotateAnimation = createXRotateAnimation(60f, mDuration)
        animationSet.addAnimation(translateAnimationSet)
        animationSet.addAnimation(rotateAnimation)
        animationSet.setAnimationListener(AnimationStepListener(::firstStepCompleted))
        mView.startAnimation(animationSet)
    }

    private fun firstStepCompleted() {
        mView.bringToFront()
        mContract.onPreviousAnimationFirstStepCompleted()
        mCurrentAnimationStep++
        playAnimationStep()
    }

    private fun playSecondAnimationStep() {
        val animationSet: AnimationSet = createAnimationSet()
        val translateAnimationSet: CarouselAnimationYTranslationAnimation =
            createTranslateAnimation(0f, mDuration)
        val rotateAnimation = createXRotateAnimation(177f, mDuration)
        val scaleAnimation =
            createScaleAnimation(mToScaleModel.getScaleX(), mToScaleModel.getScaleY(), mDuration)
        animationSet.addAnimation(translateAnimationSet)
        animationSet.addAnimation(rotateAnimation)
        animationSet.addAnimation(scaleAnimation)
        animationSet.setAnimationListener(AnimationStepListener(::secondStepCompleted))
        mView.startAnimation(animationSet)
    }

    private fun secondStepCompleted() {
        mCurrentAnimationStep++
        playAnimationStep()
    }

    private fun playThirdAnimationStep() {
        val animationSet: AnimationSet = createAnimationSet()
        val rotateAnimation = createXRotateAnimation(-3f, 0)
        animationSet.addAnimation(rotateAnimation)
        animationSet.setAnimationListener(AnimationStepListener(::thirdStepCompleted))
        mView.startAnimation(animationSet)
    }

    private fun thirdStepCompleted() {
        mCurrentAnimationStep = 0
        mContract.onPreviousAnimationCompleted()
    }
    //endregion
}