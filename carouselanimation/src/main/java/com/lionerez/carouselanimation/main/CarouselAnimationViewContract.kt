package com.lionerez.carouselanimation.main

import android.view.View

interface CarouselAnimationViewContract {
    fun bindView(index: Int, view: View) : View
}