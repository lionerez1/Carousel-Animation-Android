package com.lionerez.carouselanimation.handlers.touch

interface CarouselAnimationViewTouchHandlerContract {
    fun onMoved(newDistance: Int)

    fun onTouchEnd(lastCalculatedDistance: Int)
}