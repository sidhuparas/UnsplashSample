package com.parassidhu.unsplashsample.api

import com.parassidhu.unsplashsample.BuildConfig
import com.parassidhu.unsplashsample.model.PhotosResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiClient {

    @GET("/photos")
    suspend fun getPhotos(
        @Query("page") page: Int = 1,
        @Query("client_id") clientId: String = BuildConfig.API_KEY
    ): List<PhotosResponse>
}