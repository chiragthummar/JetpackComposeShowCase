package com.cb.myshowcase.data.remote

import com.cb.myshowcase.data.remote.dto.images.LxImageDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ImageApi {

    @GET("search")
    suspend fun getAIImage(@Query("q") q : String) : LxImageDto
}