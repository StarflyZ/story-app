package com.dicoding.picodiploma.loginwithanimation.view.add

import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.loginwithanimation.data.StoryRepository
import java.io.File

class AddViewModel(private val repository: StoryRepository) : ViewModel() {
    fun uploadStory(file: File, description: String) = repository.uploadStory(file, description)
}