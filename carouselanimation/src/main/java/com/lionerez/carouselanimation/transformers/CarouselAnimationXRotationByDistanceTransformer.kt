package com.lionerez.carouselanimation.transformers

import com.lionerez.carouselanimation.wrappers.animated_view.CarouselAnimationItemViewWrapper

class CarouselAnimationXRotationByDistanceTransformer(view: CarouselAnimationItemViewWrapper) : CarouselAnimationTransformer(view) {
    //region Members
    override val mDistanceDivider: Float get() = 20f
    //endregion

    //region Implementations
    override fun setTransform(distance: Int) {
        val newXRotation: Float = getNewXRotationForViewByDistance(distance)
        mView.rotationX = newXRotation
    }
    //endregion

    //region Private Methods
    private fun calculateXRotationFromDistance(distance: Int): Float {
        return distance.toFloat() / mDistanceDivider
    }

    private fun getNewXRotationForViewByDistance(distance: Int): Float {
        return -3f - calculateXRotationFromDistance(distance)
    }
    //endregion
}