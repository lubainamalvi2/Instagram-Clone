package com.example.mobileapplicationdevelopment2025.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CloudinaryRetrofitInstance {
    private const val BASE_URL = "https://api.cloudinary.com/v1_1/dtak8hvhi/"

    val api: CloudinaryApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CloudinaryApiService::class.java)
    }
}
