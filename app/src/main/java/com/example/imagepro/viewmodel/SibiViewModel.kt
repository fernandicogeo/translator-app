package com.example.imagepro.viewmodel

import androidx.lifecycle.ViewModel
import com.example.imagepro.repository.ModelRepository

class SibiViewModel(private val repository: ModelRepository) : ViewModel() {
    fun getSibiVideo(text: String) = repository.getSibiVideo(text)
}