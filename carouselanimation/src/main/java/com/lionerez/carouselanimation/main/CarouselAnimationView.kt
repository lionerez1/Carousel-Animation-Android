package com.lionerez.carouselanimation.main

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.lionerez.carouselanimation.adapter.CarouselAnimationViewAdapter
import com.lionerez.carouselanimation.adapter.CarouselAnimationViewAdapterContract
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
    CarouselAnimationViewAdapterContract,
    CarouselAnimationViewTouchHandlerContract,
    CarouselAnimationItemViewWrapperContract {
    //region Members
    private lateinit var mViewModel: CarouselAnimationViewModel
    private lateinit var mContract: CarouselAnimationViewContract
    private lateinit var mAdapter: CarouselAnimationViewAdapter
    private lateinit var mViewsValues: CarouselAnimationViewsValues
    private lateinit var mPager: CarouselAnimationPager
    private var mShadowImageView: CarouselAnimationShadowImageViewWrapper? = null
    //endregion

    init {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    //region Implementations
    //region CarouselAnimationViewAdapterContract
    override fun bindView(index: Int, wrapper: CarouselAnimationItemViewWrapper): CarouselAnimationItemViewWrapper {
        val newView: View = mContract.bindView(index, wrapper.getWrappedView())
        wrapper.setWrappedView(newView)
        return wrapper
    }

    override fun createView(index: Int): CarouselAnimationItemViewWrapper {
        val view = mContract.bindView(index, View(context))
        val wrapper =
            CarouselAnimationItemViewWrapper(
                context,
                view,
                this
            )
        addView(wrapper)
        return wrapper
    }

    override fun getViewValuesByPosition(index: Int): CarouselAnimationViewValues {
        return mViewsValues.getAnimationViewValuesByPosition(index)
    }
    //endregion

    //region CarouselAnimationViewTouchHandlerContract
    override fun onMoved(newDistance: Int) {
        if (newDistance < 0) {
            val firstWrapper: CarouselAnimationItemViewWrapper = mAdapter.getFirstWrapper()
            firstWrapper.handleMoveEvent(newDistance.toDp())
            if (mShadowImageView != null) {
                mShadowImageView!!.handleMoveEvent(newDistance)
            }
        } else {
            val lastWrapper: CarouselAnimationItemViewWrapper = mAdapter.getLastWrapper()
            lastWrapper.handleMoveEvent(newDistance.toDp())
        }
    }

    override fun onTouchEnd(lastCalculatedDistance: Int) {
        if (lastCalculatedDistance < 0) {
            val firstWrapper: CarouselAnimationItemViewWrapper = mAdapter.getFirstWrapper()
            firstWrapper.resetMoveEventTransforms()
            if (mShadowImageView != null) {
                mShadowImageView!!.resetMoveEvent()
            }
        } else {
            val lastWrapper: CarouselAnimationItemViewWrapper = mAdapter.getLastWrapper()
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
        val wrapper: CarouselAnimationItemViewWrapper = mAdapter.getFirstWrapper()
        mPager.setNextFirstViewIndex()
        val newView: View = mContract.bindView(mPager.getLastVisibleItemIndex(), wrapper.getWrappedView())
        wrapper.setWrappedView(newView)
    }

    override fun onNextAnimationDone() {
        mAdapter.reOrderViewsAfterNextCompleted()
        setViewTouchListener()
        if (mShadowImageView != null) {
            mShadowImageView!!.resetMoveEvent()
        }
    }

    override fun onPreviousAnimationStarted() {
        removeViewTouchListener()
        if (mPager.isNeedPaging()) {
            mPager.setPreviousFirstViewIndex()
            val newView: View = mContract.bindView(mPager.getCurrentFirstViewIndex(), mAdapter.getLastWrapper().getWrappedView())
            val lastWrapper: CarouselAnimationItemViewWrapper = mAdapter.getLastWrapper()
            lastWrapper.setWrappedView(newView)
        }
    }

    override fun startPlayingPreviousAnimationSecondaryAnimations() {
        val lastWrapper: CarouselAnimationItemViewWrapper = mAdapter.getLastWrapper()
        for (i in mAdapter.getWrappers().size - 1 downTo 0) {
            val currentWrapper = mAdapter.getWrapperByPosition(i)
            if (currentWrapper != lastWrapper) {
                var nextScaleIndex = i + 1
                if (nextScaleIndex >= mAdapter.getWrappers().size) {
                    nextScaleIndex -= mAdapter.getWrappers().size
                }
                val nextScaleModel = mViewsValues.getAnimationViewValuesByPosition(nextScaleIndex)
                currentWrapper.playSecondaryAnimation(nextScaleModel)
                currentWrapper.bringToFront()
            }
        }
        lastWrapper.bringToFront()
    }

    override fun onPreviousAnimationDone() {
        mAdapter.reOrderViewsAfterPreviousCompleted()
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
        mAdapter = CarouselAnimationViewAdapter(mViewModel.getNumberOfViews(), this)
        mAdapter.getFirstWrapper().post {
            handleViewReady()
        }
    }

    private fun handleViewReady() {
        setViewTouchListener()
        val firstWrapper: CarouselAnimationItemViewWrapper = mAdapter.getFirstWrapper()
        initializeValues(firstWrapper)
        for (i in 0 until mViewModel.getNumberOfViews()) {
            handleItem(
                mAdapter.getWrapperByPosition(i),
                mViewsValues.getAnimationViewValuesByPosition(i)
            )
        }
    }

    private fun initializeValues(wrapper: CarouselAnimationItemViewWrapper) {
        mViewsValues = CarouselAnimationViewsValues(wrapper.width, wrapper.height, mViewModel.getNumberOfViews())
    }

    private fun handleItem(wrapper: CarouselAnimationItemViewWrapper, valuesModel: CarouselAnimationViewValues) {
        wrapper.setCenterConstraints(this, mViewsValues.getVerticalMargins(), mViewsValues.getHorizontalMargins())
        wrapper.setViewTransforms(valuesModel)
    }

    private fun createBottomShadow() {
        val firstWrapper: CarouselAnimationItemViewWrapper = mAdapter.getFirstWrapper()
        firstWrapper.post {
            addView(mShadowImageView)
            mShadowImageView!!.setConstraints(this, firstWrapper.id, mViewsValues.getVerticalMargins())
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setViewTouchListener() {
        val wrapper: CarouselAnimationItemViewWrapper = mAdapter.getFirstWrapper()
        wrapper.setOnTouchListener(CarouselAnimationViewTouchHandler(this))
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun removeViewTouchListener() {
        val firstWrapper: CarouselAnimationItemViewWrapper = mAdapter.getFirstWrapper()
        firstWrapper.setOnTouchListener(null)
    }

    private fun startPlayingNextSecondaryAnimation() {
        for (i in mAdapter.getWrappers().size - 1 downTo 1) {
            val currentWrapper = mAdapter.getWrapperByPosition(i)
            val nextScaleIndex = i - 1
            val nextScaleModel = mViewsValues.getAnimationViewValuesByPosition(nextScaleIndex)
            currentWrapper.playSecondaryAnimation(nextScaleModel)
            currentWrapper.bringToFront()
        }
    }
    //endregion


}