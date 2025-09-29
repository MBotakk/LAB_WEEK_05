package com.example.lab_week_05.api

import com.example.lab_week_05.model.ImageData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CatApiService {
    @GET("images/search") // Make sure this is the correct endpoint path
    fun searchImages(
        @Query("limit") limit: Int,
        @Query("size") size: String, // Changed 'format' to 'size' to match your MainActivity and common API terms
        // If you need to pass an API key, uncomment and add the line below
        // @Query("api_key") apiKey: String
        @Query("has_breeds") hasBreeds: Boolean = true // Example of another common parameter for TheCatAPI
    ): Call<List<ImageData>> // This assumes ImageData is a data class matching the JSON response
}