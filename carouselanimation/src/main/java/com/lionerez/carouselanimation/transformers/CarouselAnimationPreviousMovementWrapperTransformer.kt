package com.lionerez.carouselanimation.transformers

import com.lionerez.carouselanimation.wrappers.animated_view.CarouselAnimationItemViewWrapper

internal class CarouselAnimationPreviousMovementWrapperTransformer(parent: CarouselAnimationItemViewWrapper) {
    //region Members
    private val mParent: CarouselAnimationItemViewWrapper = parent
    //endregion

    //region Public Methods
    fun handleEvent(distance: Int) {
        setYTranslationByDistance(distance)
    }
    //endregion

    //region Private Methods
    private fun calculateYTranslationFromDistanceForPrevious(distance: Int): Float {
        return distance.toFloat() / 50
    }

    private fun getNewYTranslationByDistanceForPrevious(distance: Int): Float {
        return calculateYTranslationFromDistanceForPrevious(distance)
    }

    private fun setYTranslationByDistance(distance: Int) {
        mParent.translationY += getNewYTranslationByDistanceForPrevious(distance)
    }
    //endregion
}