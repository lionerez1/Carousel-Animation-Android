package com.lionerez.carouselanimation.main

import android.content.Context
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CarouselAnimationViewUnitTest {

    private lateinit var carouselAnimationView: CarouselAnimationView
    private lateinit var carouselAnimationViewModel: CarouselAnimationViewModel

    @Mock
    private lateinit var mockContext: Context

    @Mock
    private lateinit var mockContract: CarouselAnimationViewContract

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        mockContext = mock(Context::class.java)
        carouselAnimationView = CarouselAnimationView(mockContext, null)
        carouselAnimationViewModel = CarouselAnimationViewModel(3,3)
        carouselAnimationView.initialize(carouselAnimationViewModel, mockContract)
    }

    @Test
    fun assert_carousel_animation_created() {
        assertNotNull(carouselAnimationView)
    }

    @Test
    fun assert_view_model_created() {
        assertNotNull(carouselAnimationViewModel)
    }
}