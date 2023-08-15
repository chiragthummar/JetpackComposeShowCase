package com.cb.myshowcase.domain.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class InfiniteOnlineImage(
    val id: String,
    val promptid: String,
    val width: Int,
    val height: Int,
    val upscaled_width: Int,
    val upscaled_height: Int,
    val userid: String
) : Parcelable
