package com.cb.myshowcase.data.remote.dto.images

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LxImageDto(
    @SerializedName("images")
    @Expose
    val images: List<LxImage>
)