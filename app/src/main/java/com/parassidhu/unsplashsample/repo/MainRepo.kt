package com.parassidhu.unsplashsample.repo

import com.parassidhu.unsplashsample.api.ApiClient
import com.parassidhu.unsplashsample.model.PhotosResponse
import com.parassidhu.unsplashsample.network.NetworkResult
import com.parassidhu.unsplashsample.util.safeApiCall

class MainRepo(private val apiClient: ApiClient) {

    suspend fun getPhotos(page: Int = 1): NetworkResult<List<PhotosResponse>> {
        var result: NetworkResult<List<PhotosResponse>>? = null

        safeApiCall({ apiClient.getPhotos(page) },
            { result = it },
            { result = it }
        )

        return result!!
    }
}