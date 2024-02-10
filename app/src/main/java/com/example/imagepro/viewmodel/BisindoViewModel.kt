package com.example.imagepro.viewmodel

import androidx.lifecycle.ViewModel
import com.example.imagepro.repository.ModelRepository

class BisindoViewModel(private val repository: ModelRepository) : ViewModel() {
    fun getBisindoVideo(text: String) = repository.getBisindoVideo(text)
}