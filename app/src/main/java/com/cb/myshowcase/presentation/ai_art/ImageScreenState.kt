package com.cb.myshowcase.presentation.ai_art

import com.cb.myshowcase.domain.model.LxOnlineImage

data class ImageScreenState(
    val images: List<LxOnlineImage> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = "",
    val isDownloading : Boolean = false,
    val imageSaved : Boolean = false
)