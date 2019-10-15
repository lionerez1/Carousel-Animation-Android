package com.lionerez.carouselanimation.transformers

import com.lionerez.carouselanimation.wrappers.CarouselAnimationItemViewWrapper

internal class CarouselAnimationYTranslationByDistanceTransformer(view: CarouselAnimationItemViewWrapper) : CarouselAnimationTransformer(view) {
    //region Members
    override val mDistanceDivider: Float get() = 1f
    //endregion

    //region Implementations
    override fun setTransform(distance: Int) {
        val newYTranslation: Float =  getNewYTranslationByDistance(distance)
        mView.translationY = newYTranslation
    }
    //endregion

    //region Private Methods
    private fun calculateYTranslationFromDistance(distance: Int): Float {
        return distance.toFloat() / mDistanceDivider
    }

    private fun getNewYTranslationByDistance(distance: Int): Float {
        return calculateYTranslationFromDistance(distance)
    }
    //endregion
}