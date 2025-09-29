package com.example.lab_week_05.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Breed(
    @Json(name = "id")
    val id: String?,

    @Json(name = "name")
    val name: String?,

    @Json(name = "temperament")
    val temperament: String?,

    @Json(name = "origin")
    val origin: String?,

    @Json(name = "description")
    val description: String?
)
