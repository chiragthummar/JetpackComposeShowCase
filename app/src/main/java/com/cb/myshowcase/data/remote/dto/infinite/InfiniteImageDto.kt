package com.cb.myshowcase.data.remote.dto.infinite

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
data class InfiniteImageDto(
    @SerializedName("images")
    @Expose
    val images: List<InfiniteImage>
)
