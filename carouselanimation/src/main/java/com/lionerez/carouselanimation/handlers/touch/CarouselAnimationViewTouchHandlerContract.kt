package com.lionerez.carouselanimation.handlers.touch

internal interface CarouselAnimationViewTouchHandlerContract {
    fun onYMoved(newDistance: Int)

    fun onXMoved(newDistance: Int)

    fun onTouchEnd(lastCalculatedDistance: Int)
}