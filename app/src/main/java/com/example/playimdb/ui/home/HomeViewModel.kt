package com.example.playimdb.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playimdb.data.model.Movie
import com.example.playimdb.data.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class HomeState(
    val trending: List<Movie> = emptyList(),
    val topRated: List<Movie> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

class HomeViewModel : ViewModel() {
    private val repository = MovieRepository()

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state

    init {
        loadMovies()
    }

    fun loadMovies() {
        viewModelScope.launch {
            _state.value = HomeState(isLoading = true)
            try {
                val trending = repository.getTrendingMovies()
                val topRated = repository.getTopRatedMovies()
                _state.value = HomeState(
                    trending = trending,
                    topRated = topRated,
                    isLoading = false
                )
            } catch (e: Exception) {
                _state.value = HomeState(isLoading = false, error = e.message ?: "Unknown error")
            }
        }
    }
}
