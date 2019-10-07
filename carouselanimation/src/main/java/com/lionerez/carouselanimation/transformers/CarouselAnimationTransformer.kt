package com.lionerez.carouselanimation.transformers

import com.lionerez.carouselanimation.wrappers.animated_view.CarouselAnimationItemViewWrapper

abstract class CarouselAnimationTransformer(view: CarouselAnimationItemViewWrapper) {
    //region Members
    protected val mView: CarouselAnimationItemViewWrapper = view
    protected abstract val mDistanceDivider: Float
    //endregion

    //region Public Methods
    abstract fun setTransform(distance: Int)
    //endregion
}