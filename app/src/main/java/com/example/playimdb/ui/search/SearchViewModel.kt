package com.example.playimdb.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playimdb.data.model.Movie
import com.example.playimdb.data.repository.MovieRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class SearchState(
    val query: String = "",
    val movies: List<Movie> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class SearchViewModel : ViewModel() {
    private val repository = MovieRepository()

    private val _state = MutableStateFlow(SearchState())
    val state: StateFlow<SearchState> = _state

    private var searchJob: Job? = null

    fun onQueryChange(query: String) {
        _state.value = _state.value.copy(query = query, error = null)

        searchJob?.cancel()

        if (query.isBlank()) {
            _state.value = _state.value.copy(movies = emptyList(), isLoading = false)
            return
        }

        searchJob = viewModelScope.launch {
            delay(500)
            _state.value = _state.value.copy(isLoading = true)
            try {
                val results = repository.searchMovies(query)
                _state.value = _state.value.copy(movies = results, isLoading = false)
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Search failed"
                )
            }
        }
    }
}
