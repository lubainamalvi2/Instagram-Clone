package com.example.mobileapplicationdevelopment2025.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileapplicationdevelopment2025.network.ApiService
import com.example.mobileapplicationdevelopment2025.network.CloudinaryApiService
import com.example.mobileapplicationdevelopment2025.network.CloudinaryRetrofitInstance
import com.example.mobileapplicationdevelopment2025.network.RetrofitInstance
import com.example.mobileapplicationdevelopment2025.room.UserDao
import com.example.mobileapplicationdevelopment2025.room.UserDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream

class CreatePostViewModel(
    application: Application,
    private val cloudinaryApi: CloudinaryApiService = CloudinaryRetrofitInstance.api,
    private val api: ApiService = RetrofitInstance.api,
    private val userDao: UserDao = UserDatabase.getDatabase(application).userDao()
) : AndroidViewModel(application) {

    private val _caption = MutableStateFlow("")
    val caption: StateFlow<String> = _caption.asStateFlow()

    private val _selectedImage = MutableStateFlow<String?>(null)
    val selectedImage: StateFlow<String?> = _selectedImage.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private var currentAccountId: String = ""

    init {
        viewModelScope.launch {
            val user = userDao.getCurrentUser()
            currentAccountId = user?.id ?: ""
        }
    }

    fun onCaptionChanged(newCaption: String) {
        _caption.value = newCaption
    }

    fun setSelectedImagePath(path: String?) {
        _selectedImage.value = path
    }

    private fun uriToFile(uri: Uri, context: Context): File? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val tempFile = File.createTempFile("upload", ".jpg", context.cacheDir)
            val outputStream = FileOutputStream(tempFile)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            tempFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private suspend fun uploadImageToCloudinary(imagePart: MultipartBody.Part): String {
        val uploadPreset: RequestBody = "ml_default".toRequestBody("text/plain".toMediaTypeOrNull())
        val response = cloudinaryApi.uploadImage(
            uploadPreset = uploadPreset,
            image = imagePart
        )
        if (response.isSuccessful) {
            return response.body()?.secureUrl ?: throw Exception("Missing secure_url in response")
        } else {
            throw Exception("Cloudinary upload error: ${response.code()}")
        }
    }

    // Function to create a post.
    fun createPost(onSuccess: () -> Unit) {
        val imagePath = selectedImage.value
        if (imagePath.isNullOrEmpty()) {
            _errorMessage.value = "Please select or take an image."
            return
        }
        if (currentAccountId.isEmpty()) {
            _errorMessage.value = "Current user information unavailable."
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {

                val file: File = if (imagePath.startsWith("content://")) {
                    uriToFile(Uri.parse(imagePath), getApplication()) ?: throw Exception("Error converting image uri to file")
                } else {
                    File(imagePath)
                }
                val requestFile: RequestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
                val imagePart: MultipartBody.Part =
                    MultipartBody.Part.createFormData("file", file.name, requestFile)

                val cloudinaryUrl = uploadImageToCloudinary(imagePart)

                val data = mapOf(
                    "account_id" to currentAccountId,
                    "caption" to caption.value,
                    "image_url" to cloudinaryUrl
                )

                val response = api.createPost(data)
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    _errorMessage.value = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error occurred: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}