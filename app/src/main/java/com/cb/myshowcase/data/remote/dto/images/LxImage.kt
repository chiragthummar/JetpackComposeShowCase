package com.cb.myshowcase.data.remote.dto.images

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LxImage(
    @SerializedName("gallery")
    @Expose
    val gallery: String,
    @SerializedName("grid")
    @Expose
    val grid: Boolean,
    @SerializedName("guidance")
    @Expose
    val guidance: Int,
    @SerializedName("height")
    @Expose
    val height: Int,
    @SerializedName("id")
    @Expose
    val id: String,
    @SerializedName("model")
    @Expose
    val model: String,
    @SerializedName("nsfw")
    @Expose
    val nsfw: Boolean,
    @SerializedName("prompt")
    @Expose
    val prompt: String,
    @SerializedName("promptid")
    @Expose
    val promptid: String,
    @SerializedName("seed")
    @Expose
    val seed: String,
    @SerializedName("src")
    @Expose
    val src: String,
    @SerializedName("srcSmall")
    @Expose
    val srcSmall: String,
    @SerializedName("width")
    @Expose
    val width: Int
)