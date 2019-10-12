package com.lionerez.carouselanimation.animations.secondary_scale

import android.content.Context
import android.view.View
import com.lionerez.carouselanimation.animations.base.CarouselAnimationViewAnimation
import com.lionerez.carouselanimation.models.CarouselAnimationViewValues

internal class CarouselAnimationSecondaryViewAnimation(context: Context, view: View, toScaleModel: CarouselAnimationViewValues): CarouselAnimationViewAnimation(context, view) {
    //region Members
    private val mToScaleModel: CarouselAnimationViewValues = toScaleModel
    //endregion

    //region Public Methods
    fun play() {
        playAnimation()
    }
    //endregion

    //region Private Methods
    private fun playAnimation() {
        val animationSet = createAnimationSet()
        val translateAnimation = createTranslateAnimation(mToScaleModel.getYTranslation(), mDuration)
        val scaleAnimation = createScaleAnimation(mToScaleModel.getScaleX(), mToScaleModel.getScaleY(), mDuration)
        animationSet.addAnimation(translateAnimation)
        animationSet.addAnimation(scaleAnimation)
        mView.startAnimation(animationSet)
    }
    //endregion
}