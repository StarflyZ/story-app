package com.dicoding.picodiploma.loginwithanimation.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.picodiploma.loginwithanimation.data.StoryRepository
import com.dicoding.picodiploma.loginwithanimation.di.Injection
import com.dicoding.picodiploma.loginwithanimation.view.add.AddViewModel
import com.dicoding.picodiploma.loginwithanimation.view.detail.DetailStoryActivity
import com.dicoding.picodiploma.loginwithanimation.view.detail.DetailViewModel
import com.dicoding.picodiploma.loginwithanimation.view.main.MainViewModel

class ViewModelFactory(private val storyRepository: StoryRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(storyRepository) as T
        }
        if (modelClass.isAssignableFrom(AddViewModel::class.java)) {
            return AddViewModel(storyRepository) as T
        }
        if(modelClass.isAssignableFrom(DetailStoryActivity::class.java)){
            return DetailViewModel(storyRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepositoryStory(context))
            }
    }
}