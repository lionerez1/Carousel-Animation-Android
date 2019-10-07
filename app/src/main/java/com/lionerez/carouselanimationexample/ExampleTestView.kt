package com.lionerez.carouselanimationexample

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.lionerez.carouselanimation.extensions.toPx
import kotlin.random.Random

class ExampleTestView(context: Context?, attrs: AttributeSet?, position: Int) : LinearLayout(context, attrs) {
    private var mPosition: Int = position
    private val mPositionTextView: TextView = TextView(context)

    init {
        setLayoutParams()
        setRandomBackgroundColor()
        setPositionTextView()
        addView(mPositionTextView)
    }

    fun setNewPoistion(newPosition: Int) {
        mPosition = newPosition
        setPositionTextView()
        setRandomBackgroundColor()
    }

    private fun setLayoutParams() {
        val layoutParams: ConstraintLayout.LayoutParams = ConstraintLayout.LayoutParams(310.toPx(), 200.toPx())
        setLayoutParams(layoutParams)
        gravity = Gravity.CENTER
    }

    private fun setRandomBackgroundColor() {
        val rnd = Random
        val color: Int = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
        setBackgroundColor(color)
    }

    private fun setPositionTextView() {
        mPositionTextView.text = "$mPosition"
    }
}