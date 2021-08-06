package com.mikhailgrigorev.mts_home.movieData

class MoviesModel(
    private val moviesDataSource: MoviesDataSource
) {

    fun getMovies() = moviesDataSource.getMovies()
}