package com.lionerez.carouselanimation.handlers.touch

internal interface CarouselAnimationViewTouchHandlerContract {
    fun onMoved(newDistance: Int)

    fun onTouchEnd(lastCalculatedDistance: Int)
}