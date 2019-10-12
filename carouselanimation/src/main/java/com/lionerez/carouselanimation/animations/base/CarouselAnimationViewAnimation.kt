package com.lionerez.carouselanimation.animations.base

import android.content.Context
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationSet
import com.lionerez.carouselanimation.animations.CarouselAnimationScaleAnimation
import com.lionerez.carouselanimation.animations.CarouselAnimationXRotateAnimation
import com.lionerez.carouselanimation.animations.CarouselAnimationXScaleAnimation
import com.lionerez.carouselanimation.animations.CarouselAnimationYTranslationAnimation

internal abstract class CarouselAnimationViewAnimation(context: Context, view: View) {
    //region Members
    private val mContext: Context = context
    protected val mView: View = view
    protected val mDuration: Long = 200
    //endregion

    //region Protected Methods
    protected fun createAnimationSet(): AnimationSet {
        val animationSet = AnimationSet(mContext, null)
        animationSet.fillAfter = true
        animationSet.interpolator = AccelerateDecelerateInterpolator()
        return animationSet
    }

    protected fun createTranslateAnimation(toY: Float, duration: Long, delay: Long = 0): CarouselAnimationYTranslationAnimation {
        val translateAnimation = CarouselAnimationYTranslationAnimation(mContext, null, mView, toY)
        translateAnimation.duration = duration
        translateAnimation.startOffset = delay
        return translateAnimation
    }

    protected fun createXRotateAnimation(xRotate: Float, duration: Long, delay: Long = 0): CarouselAnimationXRotateAnimation {
        val rotateAnimation =
            CarouselAnimationXRotateAnimation(
                mContext,
                null,
                mView,
                xRotate
            )
        rotateAnimation.duration = duration
        rotateAnimation.startOffset = delay
        return rotateAnimation
    }

    protected fun createScaleXAnimation(duration: Long, delay: Long = 0): CarouselAnimationXScaleAnimation {
        val scaleAnimation =
            CarouselAnimationXScaleAnimation(
                mContext,
                null,
                mView
            )
        scaleAnimation.duration = duration
        scaleAnimation.startOffset = delay
        return scaleAnimation
    }

    protected fun createScaleAnimation(toX: Float, toY: Float, duration: Long, delay: Long = 0): CarouselAnimationScaleAnimation {
        val animation = CarouselAnimationScaleAnimation(
            mContext,
            null,
            mView,
            toX,
            toY
        )
        animation.duration = duration
        animation.startOffset = delay
        return animation
    }
    //endregion
}