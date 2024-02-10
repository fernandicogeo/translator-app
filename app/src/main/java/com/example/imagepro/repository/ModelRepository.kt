package com.example.imagepro.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.imagepro.api.ApiService
import com.example.imagepro.helper.ResultCondition
import okhttp3.ResponseBody

class ModelRepository (private val apiService: ApiService) {
    fun getSibiVideo(text: String): LiveData<ResultCondition<ResponseBody>> = liveData {
        emit(ResultCondition.LoadingState)
        try {
            val response = apiService.getSibiVideo(text)

            if (response.isSuccessful) {
                val uploadResponse = response.body()
                emit(ResultCondition.SuccessState(uploadResponse!!))
            } else {
                emit(ResultCondition.ErrorState("Request failed with code: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(ResultCondition.ErrorState(e.message.toString()))
        }
    }

    fun getBisindoVideo(text: String): LiveData<ResultCondition<ResponseBody>> = liveData {
        emit(ResultCondition.LoadingState)
        try {
            val response = apiService.getBisindoVideo(text)

            if (response.isSuccessful) {
                val uploadResponse = response.body()
                emit(ResultCondition.SuccessState(uploadResponse!!))
            } else {
                emit(ResultCondition.ErrorState("Request failed with code: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(ResultCondition.ErrorState(e.message.toString()))
        }
    }

    companion object {
        @Volatile
        private var instanceRepo: ModelRepository? = null
        fun getInstance(api: ApiService): ModelRepository =
            instanceRepo ?: synchronized(this) {
                instanceRepo ?: ModelRepository(api)
            }.also {
                instanceRepo = it
            }
    }
}