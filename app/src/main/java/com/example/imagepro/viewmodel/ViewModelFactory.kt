package com.example.imagepro.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.imagepro.helper.Injection
import com.example.imagepro.repository.ModelRepository

class ViewModelFactory private constructor(private val repository: ModelRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SibiViewModel::class.java)) {
            return SibiViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(BisindoViewModel::class.java)) {
            return BisindoViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory {
            return instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideModelRepository(context))
            }.also { instance = it }
        }
    }
}