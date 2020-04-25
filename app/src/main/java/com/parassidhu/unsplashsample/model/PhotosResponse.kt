package com.parassidhu.unsplashsample.model

import com.google.gson.annotations.SerializedName

data class PhotosResponse(
    @SerializedName("id") val id: String,
    @SerializedName("width") val width: Int,
    @SerializedName("height") val height: Int,
    @SerializedName("urls") val urls: UnsplashURL
    )

data class UnsplashURL(
    @SerializedName("small") val imageUrl: String
)
