package com.lionerez.carouselanimation.main

import org.junit.Assert
import org.junit.Test

class CarouselAnimationViewModelUnitTest {

    @Test
    fun assert_model_created_as_provided() {
        val numberOfVisibleViews = 3
        val totalNumberOfViews = 3
        val model = CarouselAnimationViewModel(numberOfVisibleViews, totalNumberOfViews)

        //Testing number of views is same
        Assert.assertEquals(model.mNumberOfViews, numberOfVisibleViews)

        //Testing total number is same
        Assert.assertEquals(model.mTotalSize, totalNumberOfViews)
    }

    @Test
    fun assert_model_validate_number_of_views() {
        val numberOfVisibleViews = 5
        val totalNumberOfViews = 3
        val model = CarouselAnimationViewModel(numberOfVisibleViews, totalNumberOfViews)

        //Testing model handle when number of views bigger than total
        Assert.assertEquals(model.mNumberOfViews, totalNumberOfViews)
    }
}