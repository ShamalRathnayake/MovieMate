package com.shamalrathnayake.moviemate.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shamalrathnayake.moviemate.api.ApiService
import com.shamalrathnayake.moviemate.api.models.TvShow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TvShowsViewModel @Inject constructor(private val apiService: ApiService) : ViewModel() {

    private val _tvShows = MutableLiveData<List<TvShow>>()
    val tvShows: LiveData<List<TvShow>> = _tvShows

    private val _searchResults = MutableLiveData<List<TvShow>>()
    val searchResults: LiveData<List<TvShow>> = _searchResults

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private var currentPage = 1
    private var isLastPage = false
    private var isLoadingMore = false
    private var currentCategory = "on_the_air"
    private var currentQuery: String? = null

    fun loadTvShows(category: String, isRefresh: Boolean = false) {
        if (isRefresh) {
            currentPage = 1
            isLastPage = false
            _tvShows.value = emptyList()
        }

        if (isLastPage || isLoadingMore) return

        currentCategory = category
        isLoadingMore = true
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val response = when (category) {
                    "on_the_air" -> apiService.getOnTheAirTvShows(page = currentPage)
                    "popular" -> apiService.getPopularTvShows(page = currentPage)
                    "top_rated" -> apiService.getTopRatedTvShows(page = currentPage)
                    "airing_today" -> apiService.getAiringTodayTvShows(page = currentPage)
                    else -> apiService.getTvShowsByGenre(page = currentPage, genre = category.toInt())
                }

                val newTvShows = response.results
                isLastPage = currentPage >= response.totalPages

                val currentList = _tvShows.value?.toMutableList() ?: mutableListOf()
                currentList.addAll(newTvShows)
                _tvShows.value = currentList

                currentPage++
            } catch (e: Exception) {
                _error.value = e.message ?: "An error occurred"
            } finally {
                _isLoading.value = false
                isLoadingMore = false
            }
        }
    }

    fun searchTvShows(searchText: String) {
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
                val response = apiService.searchTvShows(query = searchText, page = currentPage)

                val newTvShows = response.results
                isLastPage = currentPage >= response.totalPages

                val currentList = _searchResults.value?.toMutableList() ?: mutableListOf()
                currentList.addAll(newTvShows)
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

    fun refreshTvShows() {
        loadTvShows(currentCategory, true)
    }
} 