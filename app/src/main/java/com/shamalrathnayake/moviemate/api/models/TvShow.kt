package com.shamalrathnayake.moviemate.api.models

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class TvShow(
    val id: Int,
    val name: String,
    val overview: String,
    @Json(name = "poster_path")
    val posterPath: String?,
    @Json(name = "backdrop_path")
    val backdropPath: String?,
    @Json(name = "first_air_date")
    val firstAirDate: String,
    @Json(name = "vote_average")
    val voteAverage: Double,
    @Json(name = "genre_ids")
    val genreIds: List<Int>,
    @Json(name = "vote_count")
    val voteCount: Int,
    val popularity: Double,
    @Json(name = "is_favorite")
    var isFavorite: Boolean = false,
    @Json(name = "original_language")
    var language: String,
) : Parcelable 