package com.lionerez.carouselanimationexample

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import com.lionerez.carouselanimation.main.CarouselAnimationView
import com.lionerez.carouselanimation.main.CarouselAnimationViewContract
import com.lionerez.carouselanimation.main.CarouselAnimationViewModel
import com.lionerez.carouselanimationexample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), CarouselAnimationViewContract {
    //region Members
    private val mNumberOfViewsInCarousel: Int = 3
    private val mTotalNumberOfItemsInCarousel: Int = 5
    private lateinit var binding: ActivityMainBinding
    //endregion

    //region Overrides
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        init()
    }
    //endregion

    //region Private Methods
    private fun init() {
        createCarouselAnimationView()
    }

    private fun createCarouselAnimationView() {
        val carouselAnimationViewModel = CarouselAnimationViewModel(mNumberOfViewsInCarousel, mTotalNumberOfItemsInCarousel)
        binding.carouselViewContainer.initialize(carouselAnimationViewModel, this)
        createBottomShadowForCarousel(binding.carouselViewContainer)
    }

    private fun createBottomShadowForCarousel(carouselAnimationView: CarouselAnimationView) {
        val shadowDrawable: Drawable? = getDrawable(R.mipmap.view_cards_pager_bottom_shadow)
        if (shadowDrawable != null) {
            val shadowImageView = ImageView(this)
            shadowImageView.setImageDrawable(shadowDrawable)
            carouselAnimationView.setBottomShadow(shadowImageView)
        }
    }
    //endregion

    //region Implementations
    override fun bindView(index: Int, view: View): View {
        return if (view is ExampleTestView) {
            val textView: ExampleTestView = view
            textView.setNewPosition(index)
            textView
        } else {
            ExampleTestView(applicationContext, null, index)
        }
    }
    //endregion
}
