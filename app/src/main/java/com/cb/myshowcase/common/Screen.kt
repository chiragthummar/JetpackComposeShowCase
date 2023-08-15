package com.cb.myshowcase.common

sealed class Screen(val route: String) {
    object ImageScreen : Screen("images")
    object ImageFullScreen : Screen("{position}/image_full") {
        fun createRoute(position: Int) = "$position/image_full"
    }
}
