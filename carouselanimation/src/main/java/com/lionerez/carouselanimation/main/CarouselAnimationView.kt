package com.lionerez.carouselanimation.main

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.lionerez.carouselanimation.extensions.toDp
import com.lionerez.carouselanimation.handlers.paging.CarouselAnimationPager
import com.lionerez.carouselanimation.handlers.touch.CarouselAnimationViewTouchHandler
import com.lionerez.carouselanimation.handlers.touch.CarouselAnimationViewTouchHandlerContract
import com.lionerez.carouselanimation.models.CarouselAnimationViewValues
import com.lionerez.carouselanimation.models.CarouselAnimationViewsValues
import com.lionerez.carouselanimation.wrappers.animated_view.CarouselAnimationItemViewWrapper
import com.lionerez.carouselanimation.wrappers.animated_view.CarouselAnimationItemViewWrapperContract
import com.lionerez.carouselanimation.wrappers.shadow.CarouselAnimationShadowImageViewWrapper

class CarouselAnimationView(context: Context?, attrs: AttributeSet?) :
    ConstraintLayout(context, attrs),
    CarouselAnimationViewTouchHandlerContract,
    CarouselAnimationItemViewWrapperContract {
    //region Members
    private lateinit var mContract: CarouselAnimationViewContract
    private val mViews = ArrayList<CarouselAnimationItemViewWrapper>()
    private lateinit var mViewModel: CarouselAnimationViewModel
    private lateinit var mViewsValues: CarouselAnimationViewsValues
    private lateinit var mPager: CarouselAnimationPager
    private var mShadowImageView: CarouselAnimationShadowImageViewWrapper? = null
    //endregion

    init {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    //region Implementations

    //region CarouselAnimationViewTouchHandlerContract
    override fun onMoved(newDistance: Int) {
        if (newDistance < 0) {
            val firstWrapper: CarouselAnimationItemViewWrapper = mViews[0]
            firstWrapper.handleMoveEvent(newDistance.toDp())
            if (mShadowImageView != null) {
                mShadowImageView!!.handleMoveEvent(newDistance)
            }
        } else {
            val lastWrapper: CarouselAnimationItemViewWrapper = mViews[mViews.size - 1]
            lastWrapper.handleMoveEvent(newDistance.toDp())
        }
    }

    override fun onTouchEnd(lastCalculatedDistance: Int) {
        if (lastCalculatedDistance < 0) {
            val firstWrapper: CarouselAnimationItemViewWrapper = mViews[0]
            firstWrapper.resetMoveEventTransforms()
            if (mShadowImageView != null) {
                mShadowImageView!!.resetMoveEvent()
            }
        } else {
            val lastWrapper: CarouselAnimationItemViewWrapper = mViews[mViews.size - 1]
            lastWrapper.resetPreviousMoveEventTransforms()
        }
    }
    //endregion

    //region CarouselAnimationItemViewWrapperContract
    override fun onNextAnimationStart() {
        removeViewTouchListener()
        if (mShadowImageView != null) {
            mShadowImageView!!.playAnimation()
        }
    }

    override fun startPlayingNextAnimationSecondaryAnimations() {
        startPlayingNextSecondaryAnimation()
    }

    override fun onNextAnimationSecondaryAnimationsCompleted() {
        val wrapper: CarouselAnimationItemViewWrapper = mViews[0]
        mPager.next()
        val newView: View = mContract.bindView(mPager.getLastVisibleItemIndex(), wrapper.getWrappedView())
        wrapper.setWrappedView(newView)
    }

    override fun onNextAnimationDone() {
        reOrderViewsAfterNextCompleted()
        setViewTouchListener()
        if (mShadowImageView != null) {
            mShadowImageView!!.resetMoveEvent()
        }
    }

    override fun onPreviousAnimationStarted() {
        removeViewTouchListener()
        if (mPager.isNeedPaging()) {
            mPager.previous()
            val newView: View = mContract.bindView(mPager.getCurrentFirstViewIndex(), mViews[mViews.size - 1].getWrappedView())
            val lastWrapper: CarouselAnimationItemViewWrapper = mViews[mViews.size - 1]
            lastWrapper.setWrappedView(newView)
        }
    }

    override fun startPlayingPreviousAnimationSecondaryAnimations() {
        val lastWrapper: CarouselAnimationItemViewWrapper = mViews[mViews.size - 1]
        for (i in mViews.size - 1 downTo 0) {
            val currentWrapper = mViews[i]
            if (currentWrapper != lastWrapper) {
                var nextScaleIndex = i + 1
                if (nextScaleIndex >= mViews.size) {
                    nextScaleIndex -= mViews.size
                }
                val nextScaleModel = mViewsValues.getAnimationViewValuesByPosition(nextScaleIndex)
                currentWrapper.playSecondaryAnimation(nextScaleModel)
                currentWrapper.bringToFront()
            }
        }
        lastWrapper.bringToFront()
    }

    override fun onPreviousAnimationDone() {
        reOrderViewsAfterPreviousCompleted()
        setViewTouchListener()
    }

    override fun getFirstViewScaleModel(): CarouselAnimationViewValues {
        return mViewsValues.getFirstViewValues()
    }

    override fun getLastViewScaleModel(): CarouselAnimationViewValues {
        return mViewsValues.getLastViewValues()
    }
    //endregion

    //endregion

    //region Public Methods
    fun initialize(viewModel: CarouselAnimationViewModel, contract: CarouselAnimationViewContract) {
        mViewModel = viewModel
        mContract = contract
        init()
    }

    fun setBottomShadow(shadowView: View) {
        mShadowImageView = CarouselAnimationShadowImageViewWrapper(context, shadowView)
        createBottomShadow()
    }
    //endregion

    //region Private Methods
    private fun init() {
        mPager = CarouselAnimationPager(mViewModel)
        createSubViews()
        post {
            handleViewReady()
        }
    }

    private fun createSubViews() {
        for (i in 0 until mViewModel.getNumberOfViews()) {
            val view: View = mContract.bindView(i,View(context))
            val wrapper = CarouselAnimationItemViewWrapper(context, view, this)
            mViews.add(wrapper)
            addView(wrapper)
            reverseViewsOrderOnScreen()
        }
    }

    private fun reverseViewsOrderOnScreen() {
        val lastIndex: Int = mViews.size - 1
        for (i in 0 until mViews.size) {
            val reversedIndex: Int = lastIndex - i
            mViews[reversedIndex].bringToFront()
        }
    }

    private fun handleViewReady() {
        initializeValues()
        setViewTouchListener()
        for (i in 0 until mViewModel.getNumberOfViews()) {
            handleItem(mViews[i], mViewsValues.getAnimationViewValuesByPosition(i))
        }
    }

    private fun initializeValues() {
        val wrapper: CarouselAnimationItemViewWrapper = mViews[0]
        mViewsValues = CarouselAnimationViewsValues(wrapper.width, wrapper.height, mViewModel.getNumberOfViews())
    }

    private fun handleItem(wrapper: CarouselAnimationItemViewWrapper, valuesModel: CarouselAnimationViewValues) {
        wrapper.setCenterConstraints(this, mViewsValues.getVerticalMargins(), mViewsValues.getHorizontalMargins())
        wrapper.setViewTransforms(valuesModel)
    }

    private fun createBottomShadow() {
        val firstWrapper: CarouselAnimationItemViewWrapper = mViews[0]
        firstWrapper.post {
            addView(mShadowImageView)
            mShadowImageView!!.setConstraints(this, firstWrapper.id, mViewsValues.getVerticalMargins())
        }
    }

    private fun startPlayingNextSecondaryAnimation() {
        for (i in mViews.size - 1 downTo 1) {
            val currentWrapper = mViews[i]
            val nextScaleIndex = i - 1
            val nextScaleModel = mViewsValues.getAnimationViewValuesByPosition(nextScaleIndex)
            currentWrapper.playSecondaryAnimation(nextScaleModel)
            currentWrapper.bringToFront()
        }
    }

    private fun reOrderViewsAfterNextCompleted() {
        val lastItemIndex: Int = mViews.size - 1
        var tempView: CarouselAnimationItemViewWrapper = mViews[0]
        for (i in 0 until mViews.size - 1) {
            val nextViewIndex: Int = i + 1
            val nextView: CarouselAnimationItemViewWrapper = mViews[nextViewIndex]
            val newViewValues: CarouselAnimationViewValues = mViewsValues.getAnimationViewValuesByPosition(i)
            mViews[i] = nextView
            mViews[i].setViewAnimationValues(newViewValues)
            if (nextViewIndex == lastItemIndex) {
                val nextViewNewValues: CarouselAnimationViewValues = mViewsValues.getAnimationViewValuesByPosition(i)
                mViews[nextViewIndex] = tempView
                mViews[nextViewIndex].setViewAnimationValues(nextViewNewValues)
            }
        }
    }

    private fun reOrderViewsAfterPreviousCompleted() {
        val tempView: CarouselAnimationItemViewWrapper = mViews[mViews.size - 1]
        for (i in mViews.size - 1 downTo 1) {
            val nextViewIndex: Int = i - 1
            val nextView: CarouselAnimationItemViewWrapper = mViews[nextViewIndex]
            val newViewValues: CarouselAnimationViewValues = mViewsValues.getAnimationViewValuesByPosition(i)
            mViews[i] = nextView
            mViews[i].setViewAnimationValues(newViewValues)
            if (nextViewIndex == 0) {
                val nextViewNewValues: CarouselAnimationViewValues = mViewsValues.getAnimationViewValuesByPosition(i)
                mViews[nextViewIndex] = tempView
                mViews[nextViewIndex].setViewAnimationValues(nextViewNewValues)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setViewTouchListener() {
        val wrapper: CarouselAnimationItemViewWrapper = mViews[0]
        wrapper.setOnTouchListener(CarouselAnimationViewTouchHandler(this))
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun removeViewTouchListener() {
        val firstWrapper: CarouselAnimationItemViewWrapper = mViews[0]
        firstWrapper.setOnTouchListener(null)
    }
    //endregion
}