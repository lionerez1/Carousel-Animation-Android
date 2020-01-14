package com.lionerez.carouselanimation.transformers

import com.lionerez.carouselanimation.utils.DeviceUtils
import com.lionerez.carouselanimation.wrappers.CarouselAnimationItemViewWrapper

internal class CarouselAnimationNextMovementTransformer(parent: CarouselAnimationItemViewWrapper) {
    //region Members
    private val mYScaleTransformer = CarouselAnimationYScaleByDistanceTransform(parent)
    private val mXRotationTransformer = CarouselAnimationXRotationByDistanceTransformer(parent)
    private val mYTranslationTransformer = CarouselAnimationYTranslationByDistanceTransformer(parent)
    //endregion

    //region Public Methods
    fun handleEvent(distance: Int) {
        setYScaleByDistance(distance)
        if (!DeviceUtils.isHuaweiDevice()) {
            setXRotationByDistance(distance)
        }
        setYTranslationByDistance(distance)
    }
    //endregion

    //region Private Methods
    private fun setYScaleByDistance(distance: Int) {
        mYScaleTransformer.setTransform(distance)
    }

    private fun setXRotationByDistance(distance: Int) {
        mXRotationTransformer.setTransform(distance)
    }

    private fun setYTranslationByDistance(distance: Int) {
        mYTranslationTransformer.setTransform(distance)
    }
    //endregion
}