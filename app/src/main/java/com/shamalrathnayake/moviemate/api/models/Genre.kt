package com.shamalrathnayake.moviemate.api.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Genre(
    val id: Int,
    val name: String,
    val type: String
)