package com.example.mobileapplicationdevelopment2025.network

import com.example.mobileapplicationdevelopment2025.model.CloudinaryUploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface CloudinaryApiService {
    @Multipart
    @POST("upload")
    suspend fun uploadImage(
        @Part("upload_preset") uploadPreset: RequestBody,
        @Part image: MultipartBody.Part
    ): Response<CloudinaryUploadResponse>
}
