package com.example.test_lab_week_12

import com.example.test_lab_week_12.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class MovieRepository(private val movieService: MovieService) {

    private val apiKey = "4f3099dea795f1050f22e95e26c29e4e" // kamu sudah isi 👍

    fun fetchMovies(): Flow<List<Movie>> {
        return flow {
            emit(movieService.getPopularMovies(apiKey).results)
        }.flowOn(Dispatchers.IO)
    }

    fun fetchMovieDetails(movieId: Int): Flow<Movie> {
        return flow {
            emit(movieService.getMovieDetails(movieId, apiKey))
        }.flowOn(Dispatchers.IO)
    }

}
