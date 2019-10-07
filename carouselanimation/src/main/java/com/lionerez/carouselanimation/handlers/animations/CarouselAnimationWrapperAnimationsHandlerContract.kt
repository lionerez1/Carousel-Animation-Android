package com.lionerez.carouselanimation.handlers.animations

interface CarouselAnimationWrapperAnimationsHandlerContract {
    fun startSecondaryAnimations(isNextAnimation: Boolean)

    fun onNextAnimationSecondaryAnimationsCompleted()

    fun onAnimationDone(isNextAnimation: Boolean)
}