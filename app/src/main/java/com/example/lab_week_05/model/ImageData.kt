package com.example.lab_week_05.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImageData(
    @Json(name = "id")
    val id: String?,

    // CRITICAL: This property must match how you access it.
    // The Cat API uses "url" for the direct image link.
    // If you want to call it imageUrl in your code, that's fine,
    // but the @Json annotation must match the API's key if it's different.
    // Let's assume the API returns "url" and you want to use "imageUrl" in your Kotlin code.
    @Json(name = "url")
    val imageUrl: String?, // This is where "imageUrl" comes from

    @Json(name = "width")
    val width: Int?,

    @Json(name = "height")
    val height: Int?,

    @Json(name = "breeds")
    val breeds: List<Breed>? // This is where "breeds" comes from
)