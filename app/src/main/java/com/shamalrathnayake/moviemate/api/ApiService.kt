package com.shamalrathnayake.moviemate.api

import com.shamalrathnayake.moviemate.api.models.GenreResponse
import com.shamalrathnayake.moviemate.api.models.Movie
import com.shamalrathnayake.moviemate.api.models.MovieResponse
import com.shamalrathnayake.moviemate.api.models.TvShow
import com.shamalrathnayake.moviemate.api.models.TvShowResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int = 1
    ): MovieResponse

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("page") page: Int = 1
    ): MovieResponse

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("page") page: Int = 1
    ): MovieResponse

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("page") page: Int = 1
    ): MovieResponse

    @GET("discover/movie")
    suspend fun getMoviesByGenre(
        @Query("page") page: Int = 1,
        @Query("with_genres") genre: Int = 1
    ): MovieResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int
    ): Movie

    @GET("genre/movie/list")
    suspend fun getMovieGenres(): GenreResponse

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("page") page: Int = 1
    ): MovieResponse


    @GET("tv/popular")
    suspend fun getPopularTvShows(
        @Query("page") page: Int = 1
    ): TvShowResponse

    @GET("tv/top_rated")
    suspend fun getTopRatedTvShows(
        @Query("page") page: Int = 1
    ): TvShowResponse

    @GET("tv/on_the_air")
    suspend fun getOnTheAirTvShows(
        @Query("page") page: Int = 1
    ): TvShowResponse

    @GET("tv/airing_today")
    suspend fun getAiringTodayTvShows(
        @Query("page") page: Int = 1
    ): TvShowResponse

    @GET("discover/tv")
    suspend fun getTvShowsByGenre(
        @Query("page") page: Int = 1,
        @Query("with_genres") genre: Int = 1
    ): TvShowResponse

    @GET("tv/{tv_id}")
    suspend fun getTvShowDetails(
        @Path("tv_id") tvId: Int
    ): TvShow

    @GET("genre/tv/list")
    suspend fun getTvShowGenres(): GenreResponse

    @GET("search/tv")
    suspend fun searchTvShows(
        @Query("query") query: String,
        @Query("page") page: Int = 1
    ): TvShowResponse
}