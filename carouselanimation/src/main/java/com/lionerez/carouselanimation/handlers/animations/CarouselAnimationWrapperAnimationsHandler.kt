package com.lionerez.carouselanimation.handlers.animations

import android.content.Context
import android.util.Log
import com.lionerez.carouselanimation.animations.next_view.CarouselAnimationNextViewAnimation
import com.lionerez.carouselanimation.animations.next_view.CarouselAnimationNextViewAnimationContract
import com.lionerez.carouselanimation.animations.previous_view.CarouselAnimationPreviousViewAnimation
import com.lionerez.carouselanimation.animations.previous_view.CarouselAnimationPreviousViewAnimationContract
import com.lionerez.carouselanimation.animations.previous_view.CarouselAnimationPreviousViewAnimationModel
import com.lionerez.carouselanimation.animations.secondary_scale.CarouselAnimationSecondaryViewAnimation
import com.lionerez.carouselanimation.models.CarouselAnimationViewValues
import com.lionerez.carouselanimation.wrappers.CarouselAnimationItemViewWrapper

internal class CarouselAnimationWrapperAnimationsHandler(context: Context, view: CarouselAnimationItemViewWrapper, contract: CarouselAnimationWrapperAnimationsHandlerContract) :
    CarouselAnimationNextViewAnimationContract,
    CarouselAnimationPreviousViewAnimationContract {
    //region Members
    private val mContext: Context = context
    private val mView: CarouselAnimationItemViewWrapper = view
    private val mContract: CarouselAnimationWrapperAnimationsHandlerContract = contract
    //endregion

    //region Implementations
    override fun onNextAnimationFirstStepCompleted() {
        mContract.startSecondaryAnimations(true)
    }

    override fun onNextAnimationSecondStepCompleted() {
        mContract.onNextAnimationSecondaryAnimationsCompleted()
    }

    override fun onNextAnimationCompleted() {
        mContract.onAnimationDone(true)
    }

    override fun onPreviousAnimationFirstStepCompleted() {
        mContract.startSecondaryAnimations(false)
    }

    override fun onPreviousAnimationCompleted() {
        mContract.onAnimationDone(false)
    }
    //endregion

    //region Public Methods
    fun playNextViewAnimation(toViewScale: CarouselAnimationViewValues) {
        Log.v("liolog", "next animation x scale value = ${toViewScale.getScaleX()}")
        Log.v("liolog", "next animation y scale value = ${toViewScale.getScaleY()}")
        Log.v("liolog", "next animation transaction value = ${toViewScale.getYTranslation()}")
        val nextAnimation = CarouselAnimationNextViewAnimation(mContext, mView, toViewScale,this)
        nextAnimation.play()
    }

    fun playPreviousViewAnimation(currentViewScale: CarouselAnimationViewValues ,toViewScale: CarouselAnimationViewValues) {
        val previousAnimationModel = CarouselAnimationPreviousViewAnimationModel(currentViewScale, toViewScale);
        val previousViewAnimation = CarouselAnimationPreviousViewAnimation(mContext, mView, previousAnimationModel, this)
        previousViewAnimation.play()
    }

    fun playSecondaryAnimation(wrapper: CarouselAnimationItemViewWrapper, toViewScale: CarouselAnimationViewValues) {
        val animation = CarouselAnimationSecondaryViewAnimation(mContext, wrapper, toViewScale)
        animation.play()
        wrapper.bringToFront()
    }
    //endregion
}