package com.lionerez.carouselanimation.extensions

import android.content.res.Resources

internal fun Int.toDp(): Int {
    return (this / Resources.getSystem().displayMetrics.density).toInt()
}

internal fun Int.getNextScale(): Int {
    return (this * 0.85).toInt()
}