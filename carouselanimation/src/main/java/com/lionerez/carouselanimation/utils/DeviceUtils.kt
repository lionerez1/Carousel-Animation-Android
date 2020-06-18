package com.lionerez.carouselanimation.utils

internal final class DeviceUtils {

    companion object {
        fun isHuaweiDevice(): Boolean {
            val manufacturer: String = android.os.Build.MANUFACTURER
//            return manufacturer.toLowerCase().equals("huawei")
            return false;
        }
    }

}