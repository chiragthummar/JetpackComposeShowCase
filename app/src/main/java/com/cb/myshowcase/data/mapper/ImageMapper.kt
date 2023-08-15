package com.cb.myshowcase.data.mapper

import com.cb.myshowcase.domain.model.LxOnlineImage
import com.cb.myshowcase.data.remote.dto.images.LxImage
import com.cb.myshowcase.data.remote.dto.infinite.InfiniteImage
import com.cb.myshowcase.domain.model.InfiniteOnlineImage


fun LxImage.toLxOnlineImage(): LxOnlineImage {
    return LxOnlineImage(
        gallery = gallery,
        grid = grid,
        guidance = guidance,
        height = height,
        model = model,
        id = id,
        nsfw = nsfw,
        prompt = prompt,
        promptid = prompt,
        seed = seed,
        src = src,
        srcSmall = srcSmall,
        width = width
    )
}




fun InfiniteImage.toInfiniteOnlineImage() : InfiniteOnlineImage {
    return InfiniteOnlineImage(
        id = id,
        promptid = promptid,
        width =width,
        height = height,
        upscaled_width = upscaled_width,
        upscaled_height = upscaled_height,
        userid = userid
    )
}


