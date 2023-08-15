package com.cb.myshowcase.data.remote

import com.cb.myshowcase.data.remote.dto.infinite.InfiniteImageDto
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface InfiniteImageApi {

    @FormUrlEncoded
    @POST("infinite-prompts")
    suspend fun getInfiniteApiImages(@Field("text") text : String,
                                     @Field("searchMode") searchMode : String = "images",
                                     @Field("source") source : String = "search",
                                     @Field("model") model : String ="lexica-aperture-v2") : InfiniteImageDto


    companion object {
        const val INFINITE_IMAGE_BASE_URL = "https://lexica.art/api/"
        const val IMAGE_BASE_URL = "https://lexica.art/api/v1/"
    }
}