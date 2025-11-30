package com.example.test_lab_week_12

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test_lab_week_12.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class DetailsViewModel(private val repository: MovieRepository) : ViewModel() {

    private val _movieDetails = MutableStateFlow<Movie?>(null)
    val movieDetails: StateFlow<Movie?> = _movieDetails

    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    fun loadDetails(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.fetchMovieDetails(movieId)
                .catch { _error.value = "Error: ${it.message}" }
                .collect { movie ->
                    _movieDetails.value = movie
                }
        }
    }
}
