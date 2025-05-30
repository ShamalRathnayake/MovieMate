package com.shamalrathnayake.moviemate.api.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GenreResponse(
    val genres: List<Genre>
)