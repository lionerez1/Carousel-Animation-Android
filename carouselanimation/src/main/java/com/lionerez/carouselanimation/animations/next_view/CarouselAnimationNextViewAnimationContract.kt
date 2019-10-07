package com.lionerez.carouselanimation.animations.next_view

import com.lionerez.carouselanimation.models.CarouselAnimationViewValues

interface CarouselAnimationNextViewAnimationContract {
    fun onNextAnimationFirstStepCompleted()

    fun onNextAnimationSecondStepCompleted(): CarouselAnimationViewValues

    fun onNextAnimationCompleted()
}