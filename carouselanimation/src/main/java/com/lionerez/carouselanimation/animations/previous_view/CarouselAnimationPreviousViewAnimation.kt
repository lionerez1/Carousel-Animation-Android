package com.lionerez.carouselanimation.animations.previous_view

import android.content.Context
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationSet
import com.lionerez.carouselanimation.animations.CarouselAnimationYTranslationAnimation
import com.lionerez.carouselanimation.animations.base.CarouselAnimationViewAnimation
import com.lionerez.carouselanimation.utils.DeviceUtils

internal class CarouselAnimationPreviousViewAnimation (context: Context, view: View, model: CarouselAnimationPreviousViewAnimationModel, contract: CarouselAnimationPreviousViewAnimationContract) : CarouselAnimationViewAnimation(context, view) {
    //region Members
    private val mContract: CarouselAnimationPreviousViewAnimationContract = contract
    private val mModel: CarouselAnimationPreviousViewAnimationModel = model
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
        }
    }

    private fun playFirstAnimationStep() {
        mView.z = -50f
        val animationSet: AnimationSet = createAnimationSet()
        val translateAnimationSet: CarouselAnimationYTranslationAnimation = createTranslateAnimation(500f, mSecondaryDuration)
        val scale = createScaleAnimation(0.7f, 0.4f, mSecondaryDuration)
        animationSet.addAnimation(translateAnimationSet)
        if (!DeviceUtils.isHuaweiDevice()) {
            val rotateAnimation = createXRotateAnimation(-50f, mSecondaryDuration)
            animationSet.addAnimation(rotateAnimation)
        }
        animationSet.addAnimation(scale)
        animationSet.setAnimationListener(AnimationStepListener(::firstStepCompleted))
        mView.startAnimation(animationSet)
    }

    private fun firstStepCompleted() {
        mContract.onPreviousAnimationFirstStepCompleted()
        mView.z = 40f
        mCurrentAnimationStep++
        playAnimationStep()
    }

    private fun playSecondAnimationStep() {
        val animationSet: AnimationSet = createAnimationSet()
        val translateAnimationSet: CarouselAnimationYTranslationAnimation =
            createTranslateAnimation(0f, mDuration)
        val scaleAnimation =
            createScaleAnimation(mModel.mToScaleModel.getScaleX(), mModel.mToScaleModel.getScaleY(), mDuration)
        animationSet.addAnimation(translateAnimationSet)
        if (!DeviceUtils.isHuaweiDevice()) {
            val rotateAnimation = createXRotateAnimation(-3f, mDuration)
            animationSet.addAnimation(rotateAnimation)
        }
        animationSet.addAnimation(scaleAnimation)
        animationSet.setAnimationListener(AnimationStepListener(::secondStepCompleted))
        mView.startAnimation(animationSet)
    }

    private fun secondStepCompleted() {
        mCurrentAnimationStep = 0
        mContract.onPreviousAnimationCompleted()
    }
    //endregion
}