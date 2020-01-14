package com.lionerez.carouselanimation.handlers.touch

internal interface CarouselAnimationViewTouchHandlerContract {
    fun handleNextMovement(distance: Int)

    fun handlePreviousMovement(distance: Int)

    fun handleNextSwipe(distance: Int)

    fun handlePreviousSwipe(distance: Int)

    fun resetNextAnimation()

    fun resetPreviousAnimation()
}