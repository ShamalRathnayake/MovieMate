package com.shamalrathnayake.moviemate.api.repository

import com.shamalrathnayake.moviemate.api.ApiService
import com.shamalrathnayake.moviemate.api.models.Movie
import com.shamalrathnayake.moviemate.api.models.MovieResponse
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MovieRepository @Inject constructor(
    private val api: ApiService
) {

    suspend fun getPopularMovies(page: Int = 1): MovieResponse {
        return api.getPopularMovies(page)
    }

    suspend fun getTopRatedMovies(page: Int = 1): MovieResponse {
        return api.getTopRatedMovies(page)
    }

    suspend fun getUpcomingMovies(page: Int = 1): MovieResponse {
        return api.getUpcomingMovies(page)
    }

    suspend fun getMovieDetails(movieId: Int): Movie {
        return api.getMovieDetails(movieId)
    }

    suspend fun searchMovies(query: String, page: Int = 1): MovieResponse {
        return api.searchMovies(query, page)
    }

    suspend fun getMoviesByGenre(genreId: Int, page: Int = 1): MovieResponse {
        return api.getMoviesByGenre(page, genreId)
    }

    suspend fun getNowPlayingMovies(page: Int = 1): MovieResponse {
        return api.getNowPlayingMovies(page)
    }




}