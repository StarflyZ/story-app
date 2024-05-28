package com.dicoding.picodiploma.loginwithanimation.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserPreference
import com.dicoding.picodiploma.loginwithanimation.response.DetailStoryResponse
import com.dicoding.picodiploma.loginwithanimation.response.Story
import com.dicoding.picodiploma.loginwithanimation.response.StoryResponse
import com.dicoding.picodiploma.loginwithanimation.response.UploadStoryResponse
import com.dicoding.picodiploma.loginwithanimation.retrofit.ApiService
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class StoryRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout(){
        userPreference.logout()
    }

    fun getStories(): LiveData<ResultState<StoryResponse>> = liveData{
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.getStories()
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, StoryResponse::class.java)
            emit(ResultState.Error(errorResponse.message ?: "Error"))
        }
    }

    fun getStoryDetail(id: String): LiveData<ResultState<Story>> = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.getStoryDetail(id)
            emit(ResultState.Success(successResponse.story))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, DetailStoryResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        }
    }

    fun uploadStory(imageFile: File, description: String) = liveData {
        emit(ResultState.Loading)
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )
        try {
            val successResponse = apiService.uploadStory(multipartBody, requestBody)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, UploadStoryResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        }

    }

    companion object {
        fun getInstance(apiService: ApiService, userPreference: UserPreference) = StoryRepository(apiService, userPreference)
    }
}