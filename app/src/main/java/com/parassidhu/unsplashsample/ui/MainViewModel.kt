package com.parassidhu.unsplashsample.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.parassidhu.unsplashsample.model.PhotosResponse
import com.parassidhu.unsplashsample.network.NetworkResult
import com.parassidhu.unsplashsample.repo.MainRepo
import kotlinx.coroutines.launch

class MainViewModel(private val repo: MainRepo): ViewModel() {

    private var currentPage = 1
    private var canRequestMore = false

    private val _photosResponse = MutableLiveData<List<PhotosResponse>>()
    val photosResponse: LiveData<List<PhotosResponse>>
        get() = _photosResponse

    private val _errorLiveData = MutableLiveData<Boolean>()
    val errorLiveData: LiveData<Boolean>
        get() = _errorLiveData

    private val totalList = mutableListOf<PhotosResponse>()

    init {
        getPhotos()
    }

    fun getPhotos() {
        canRequestMore = false
        viewModelScope.launch {
            val response = repo.getPhotos(currentPage)

            when(response) {
                is NetworkResult.Success -> {
                    val data = response.data
                    _photosResponse.value = data
                    totalList.addAll(data)
                    currentPage++
                    canRequestMore = true
                }

                is NetworkResult.Error -> {
                    _errorLiveData.value = true
                }
            }
        }
    }

    fun canRequestMore() = canRequestMore

    fun getTotalList() = totalList

    fun clear() {
        _photosResponse.value = mutableListOf()
    }
}