package com.example.playimdb.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.playimdb.data.model.MovieDetail
import com.example.playimdb.data.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class DetailState(
    val movie: MovieDetail? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)

class DetailViewModel(private val movieId: Int) : ViewModel() {
    private val repository = MovieRepository()

    private val _state = MutableStateFlow(DetailState())
    val state: StateFlow<DetailState> = _state

    init {
        loadDetail()
    }

    private fun loadDetail() {
        viewModelScope.launch {
            _state.value = DetailState(isLoading = true)
            try {
                val movie = repository.getMovieDetail(movieId)
                _state.value = DetailState(movie = movie, isLoading = false)
            } catch (e: Exception) {
                _state.value = DetailState(isLoading = false, error = e.message ?: "Failed to load")
            }
        }
    }
}

class DetailViewModelFactory(private val movieId: Int) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailViewModel(movieId) as T
    }
}
