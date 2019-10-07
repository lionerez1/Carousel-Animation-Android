package com.lionerez.carouselanimation.adapter

import com.lionerez.carouselanimation.models.CarouselAnimationViewValues
import com.lionerez.carouselanimation.wrappers.animated_view.CarouselAnimationItemViewWrapper

interface CarouselAnimationViewAdapterContract {
    fun bindView(index: Int, wrapper: CarouselAnimationItemViewWrapper): CarouselAnimationItemViewWrapper

    fun createView(index: Int): CarouselAnimationItemViewWrapper

    fun getViewValuesByPosition(index: Int): CarouselAnimationViewValues
}