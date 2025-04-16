package com.example.mobileapplicationdevelopment2025.fakes

import com.example.mobileapplicationdevelopment2025.model.CloudinaryUploadResponse
import com.example.mobileapplicationdevelopment2025.network.CloudinaryApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody

class FakeCloudinaryApiService : CloudinaryApiService {
    var simulateSuccess: Boolean = true
    var secureUrlToReturn: String = "https://res.cloudinary.com/dtak8hvhi/image/upload/v123456/sample.jpg"

    override suspend fun uploadImage(
        uploadPreset: RequestBody,
        image: MultipartBody.Part
    ): Response<CloudinaryUploadResponse> {
        return if (simulateSuccess) {
            Response.success(CloudinaryUploadResponse(secureUrl = secureUrlToReturn))
        } else {
            Response.error(
                400,
                "Upload failed".toResponseBody("text/plain".toMediaTypeOrNull())
            )
        }
    }
}