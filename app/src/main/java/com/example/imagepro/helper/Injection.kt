package com.example.imagepro.helper

import android.content.Context
import com.example.imagepro.api.ApiConfig
import com.example.imagepro.repository.ModelRepository

object Injection {
    fun provideModelRepository(context: Context): ModelRepository {
        val api = ApiConfig.getApiService()
        return ModelRepository.getInstance(api)
    }
}