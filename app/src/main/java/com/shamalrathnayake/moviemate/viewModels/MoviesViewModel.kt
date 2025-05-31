package com.shamalrathnayake.moviemate.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shamalrathnayake.moviemate.api.ApiService
import com.shamalrathnayake.moviemate.api.models.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(private val apiService: ApiService) : ViewModel()  {

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies

    private val _searchResults = MutableLiveData<List<Movie>>()
    val searchResults: LiveData<List<Movie>> = _searchResults

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private var currentPage = 1
    private var isLastPage = false
    private var isLoadingMore = false
    private var currentCategory = "now_playing"
    private var currentQuery: String? = null


    fun loadMovies(category: String, isRefresh: Boolean = false) {
        if (isRefresh) {
            currentPage = 1
            isLastPage = false
            _movies.value = emptyList()
        }

        if (isLastPage || isLoadingMore) return

        currentCategory = category
        isLoadingMore = true
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val response = when (category) {
                    "now_playing" -> apiService.getNowPlayingMovies(page = currentPage)
                    "popular" -> apiService.getPopularMovies(page = currentPage)
                    "top_rated" -> apiService.getTopRatedMovies(page = currentPage)
                    "upcoming" -> apiService.getUpcomingMovies(page = currentPage)
                    else -> apiService.getMoviesByGenre(page = currentPage, genre = category.toInt())
                }

                val newMovies = response.results
                isLastPage = currentPage >= response.totalPages

                val currentList = _movies.value?.toMutableList() ?: mutableListOf()
                currentList.addAll(newMovies)
                _movies.value = currentList

                currentPage++
            } catch (e: Exception) {
                _error.value = e.message ?: "An error occurred"
            } finally {
                _isLoading.value = false
                isLoadingMore = false
            }
        }
    }


    fun searchMovies(searchText: String) {



        if (currentQuery != searchText) {
            currentPage = 1
            isLastPage = false
            _searchResults.value = emptyList()
            currentQuery = searchText
        }

        if (isLastPage || isLoadingMore) return


        isLoadingMore = true
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val response =  apiService.searchMovies(query = searchText, page = currentPage)


                val newMovies = response.results
                isLastPage = currentPage >= response.totalPages

                val currentList = _searchResults.value?.toMutableList() ?: mutableListOf()
                currentList.addAll(newMovies)
                _searchResults.value = currentList

                currentPage++
            } catch (e: Exception) {
                _error.value = e.message ?: "An error occurred"
            } finally {
                _isLoading.value = false
                isLoadingMore = false
            }
        }
    }

    fun refreshMovies() {
        loadMovies(currentCategory, true)
    }

}