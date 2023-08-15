package com.cb.myshowcase.presentation.ai_art

import android.content.Context
import java.io.File

sealed class ImageEvent {
    data class SearchImage(val prompt: String) : ImageEvent()
    data class DownloadImage(val context : Context, val url: String) : ImageEvent()
    data class ShareImage(val context : Context, val file : File) : ImageEvent()

}