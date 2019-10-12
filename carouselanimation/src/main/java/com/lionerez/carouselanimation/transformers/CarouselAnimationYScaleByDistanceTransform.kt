package com.lionerez.carouselanimation.transformers

import com.lionerez.carouselanimation.wrappers.animated_view.CarouselAnimationItemViewWrapper

internal class CarouselAnimationYScaleByDistanceTransform(view: CarouselAnimationItemViewWrapper): CarouselAnimationTransformer(view) {
    override val mDistanceDivider: Float get() = 200f
    //endregion

    //region Implementations
    override fun setTransform(distance: Int) {
        val newScaleY: Float = getNewYScaleForViewByDistance(distance)
        mView.scaleY = newScaleY
    }
    //endregion

    //region Private Methods
    private fun calculateYScaleFromDistance(distance: Int): Float {
        return distance.toFloat() / mDistanceDivider
    }

    private fun getNewYScaleForViewByDistance(distance: Int): Float {
        return 1f - calculateYScaleFromDistance(distance)
    }
    //endregion
}