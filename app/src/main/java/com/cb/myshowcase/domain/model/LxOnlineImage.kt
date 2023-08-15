package com.cb.myshowcase.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LxOnlineImage(
    val gallery: String,
    val grid: Boolean,
    val guidance: Int,
    val height: Int,
    val id: String,
    val model: String,
    val nsfw: Boolean,
    val prompt: String,
    val promptid: String,
    val seed: String,
    val src: String,
    val srcSmall: String,
    val width: Int
) :Parcelable