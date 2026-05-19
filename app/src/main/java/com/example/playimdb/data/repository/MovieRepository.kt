package com.example.playimdb.data.repository

import com.example.playimdb.data.api.RetrofitClient
import com.example.playimdb.data.model.Movie
import com.example.playimdb.data.model.MovieDetail

class MovieRepository {
    private val api = RetrofitClient.api

    suspend fun getTrendingMovies(): List<Movie> = api.getTrendingMovies().results

    suspend fun getTopRatedMovies(): List<Movie> = api.getTopRatedMovies().results

    suspend fun searchMovies(query: String): List<Movie> = api.searchMovies(query).results

    suspend fun getMovieDetail(id: Int): MovieDetail = api.getMovieDetail(id)
}
