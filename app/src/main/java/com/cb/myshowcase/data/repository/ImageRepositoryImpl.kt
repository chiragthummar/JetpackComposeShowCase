package com.cb.myshowcase.data.repository

import com.cb.myshowcase.data.mapper.toInfiniteOnlineImage
import com.cb.myshowcase.data.mapper.toLxOnlineImage
import com.cb.myshowcase.data.remote.ImageApi
import com.cb.myshowcase.data.remote.InfiniteImageApi
import com.cb.myshowcase.domain.model.InfiniteOnlineImage
import com.cb.myshowcase.domain.model.LxOnlineImage
import com.cb.myshowcase.domain.repository.ImageRepository
import com.cb.myshowcase.common.Resources
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageRepositoryImpl @Inject constructor(
    private val imageApi: ImageApi,
    private val infiniteImageApi: InfiniteImageApi
) : ImageRepository {
    override fun getLxImagesFromAI(prompt: String): Flow<Resources<List<LxOnlineImage>>> {
        return flow {
            emit(Resources.Loading(true))

            val imageList = try {
                val result = imageApi.getAIImage(prompt)
                result.images
            } catch (e: Exception) {
                try {
                    val result = imageApi.getAIImage(prompt)
                    result.images
                } catch (e: Exception) {
                    emit(Resources.Error("Exception Occurred In Fetching Image"))
                    e.printStackTrace()
                    emit(Resources.Loading(false))
                    null
                }
            }


            if (imageList == null) {
                emit(Resources.Loading(false))
            }


            imageList?.let { image ->
                emit(Resources.Success(data = image.map { it.toLxOnlineImage() }))
                emit(Resources.Loading(false))
            }

        }
    }

    override fun getInfiniteImage(text: String): Flow<Resources<List<InfiniteOnlineImage>>> {
        return flow {
            emit(Resources.Loading(true))

            val imageList = try {
                val result = infiniteImageApi.getInfiniteApiImages(text)
                result.images
            } catch (e: Exception) {
                try {
                    val result = infiniteImageApi.getInfiniteApiImages(text)
                    result.images
                } catch (e: Exception) {
                    emit(Resources.Error("getInfiniteApiImages Exception Occurred In Fetching Image"))
                    e.printStackTrace()
                    emit(Resources.Loading(false))
                    null
                }
            }


            if (imageList == null) {
                emit(Resources.Loading(false))
            }


            imageList?.let { image ->
                emit(Resources.Success(data = image.map { it.toInfiniteOnlineImage() }))
                emit(Resources.Loading(false))
            }

        }
    }


}