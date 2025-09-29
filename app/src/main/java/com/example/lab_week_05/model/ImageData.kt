package com.example.lab_week_05.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImageData(
    @Json(name = "id")
    val id: String?,

    @Json(name = "url")
    val url: String?,

    @Json(name = "width")
    val width: Int?,

    @Json(name = "height")
    val height: Int?

)
