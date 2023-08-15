package com.cb.myshowcase.data.remote.dto.infinite

import androidx.annotation.Keep

@Keep
data class InfiniteImage(
    val id : String,
    val promptid : String,
    val width : Int,
    val height : Int,
    val upscaled_width : Int,
    val upscaled_height : Int,
    val userid : String
)
