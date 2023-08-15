package com.cb.myshowcase.domain.repository

import com.cb.myshowcase.domain.model.InfiniteOnlineImage
import com.cb.myshowcase.domain.model.LxOnlineImage
import com.cb.myshowcase.common.Resources
import kotlinx.coroutines.flow.Flow

interface ImageRepository {

    fun getLxImagesFromAI(
        prompt: String
    ): Flow<Resources<List<LxOnlineImage>>>

    fun getInfiniteImage(
        text: String
    ): Flow<Resources<List<InfiniteOnlineImage>>>

}