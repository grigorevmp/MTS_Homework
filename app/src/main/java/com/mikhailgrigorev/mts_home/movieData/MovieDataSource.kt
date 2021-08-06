package com.mikhailgrigorev.mts_home.movieData

interface MoviesDataSource {
    fun getMovies(): List<MovieData>
}