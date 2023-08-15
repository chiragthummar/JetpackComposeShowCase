package com.cb.myshowcase.presentation.ai_art

import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.media.MediaScannerConnection
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.cb.myshowcase.common.Constants
import com.cb.myshowcase.common.Resources
import com.cb.myshowcase.domain.repository.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.apache.commons.io.FilenameUtils
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import javax.inject.Inject


@HiltViewModel
class ImageViewModel @Inject constructor(
    private val imageRepository: ImageRepository,
    private val application: Application,
) : ViewModel() {


    var state by mutableStateOf(ImageScreenState())

    init {
        val list = listOf(
            "Future World",
            "Natural Landscape",
            "Future Car",
            "Future Robots",
            "Sports"
        )
        onEvent(ImageEvent.SearchImage(list[(0..4).random()]))
    }

    fun onEvent(event: ImageEvent) {
        when (event) {
            is ImageEvent.SearchImage -> {
                getImageResponse(event.prompt)
            }

            is ImageEvent.DownloadImage -> {
                downloadImageFromUrl(event.context, event.url)
            }

            is ImageEvent.ShareImage -> {
                shareImageFile(event.context, event.file)
            }
        }
    }

    private fun persistImage(context: Context, file: File, bitmap: Bitmap) {
        val os: OutputStream
        try {
            os = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
            os.flush()
            os.close()

            MediaScannerConnection.scanFile(
                context, arrayOf(file.toString()),
                null, null
            )

            state = state.copy(isDownloading = false)
            state = state.copy(imageSaved = true)
        } catch (e: Exception) {
            Log.e(javaClass.simpleName, "Error writing bitmap", e)
        }
    }

    private fun downloadImageFromUrl(context: Context, url: String) {
        viewModelScope.launch(Dispatchers.IO) {
            state = state.copy(isDownloading = true)
            /*  val loader = ImageLoader(context)
              val req = ImageRequest.Builder(context)
                  .data(url)
                  .target { result ->
                      val bitmap = (result as BitmapDrawable).bitmap
                      persistImage(context, bitmap, FilenameUtils.getName(url))
                      state = state.copy(isDownloading = false)
                  }
                  .build()

              val disposable = loader.enqueue(req)*/

            val loader = ImageLoader(context)
            val request = ImageRequest.Builder(context)
                .data(url)
                .allowHardware(false) // Disable hardware bitmaps.
                .build()

            val result = (loader.execute(request) as SuccessResult).drawable
            val bitmap = (result as BitmapDrawable).bitmap

            val path =
                File(application.externalMediaDirs[0].toString() + "/" + Constants.FOLDER_NAME)

            if (!path.exists())
                path.mkdirs()


            val imageFile = File(path, "${FilenameUtils.getName(url)}.jpg")
            if (imageFile.exists()) {
                state = state.copy(isDownloading = false)
//                state = state.copy(imageSaved = true)
            } else {
                persistImage(context, imageFile, bitmap)

            }
        }
    }


    private fun getImageResponse(
        prompt: String,
    ) {
        viewModelScope.launch {
            imageRepository.getLxImagesFromAI(prompt)
                .collect { result ->
                    when (result) {
                        is Resources.Success -> {
                            result.data?.let { listings ->
                                state = state.copy(
                                    images = listings
                                )
                            }
                        }

                        is Resources.Error -> {

                        }

                        is Resources.Loading -> {
                            state = state.copy(isLoading = result.isLoading)
                        }

                    }

                }
        }
    }

    private fun shareImageFile(context: Context, file: File) {

        val pdfUri: Uri =
            FileProvider.getUriForFile(context, context.packageName + ".provider", file)
        val share = Intent()
        share.action = Intent.ACTION_SEND
        share.type = "image/*"
        share.putExtra(Intent.EXTRA_STREAM, pdfUri)
        ContextCompat.startActivity(context, Intent.createChooser(share, "Share"), null)
    }
}