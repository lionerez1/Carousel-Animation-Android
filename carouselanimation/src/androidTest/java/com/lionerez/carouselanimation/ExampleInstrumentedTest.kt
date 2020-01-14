package com.lionerez.carouselanimation

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import androidx.test.runner.intent.IntentStubberRegistry
import com.lionerez.carouselanimation.main.CarouselAnimationView

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.lionerez.carouselanimation.test", appContext.packageName)
    }
}
