package com.lionerez.carouselanimation.wrappers.animated_view

import com.lionerez.carouselanimation.models.CarouselAnimationViewValues

interface CarouselAnimationItemViewWrapperContract {
    fun onNextAnimationStart()

    fun startPlayingNextAnimationSecondaryAnimations()

    fun onNextAnimationSecondaryAnimationsCompleted()

    fun onNextAnimationDone()

    fun onPreviousAnimationStarted()

    fun startPlayingPreviousAnimationSecondaryAnimations()

    fun onPreviousAnimationDone()

    fun getFirstViewScaleModel(): CarouselAnimationViewValues

    fun getLastViewScaleModel(): CarouselAnimationViewValues
}