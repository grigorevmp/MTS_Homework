package com.mikhailgrigorev.mts_home.movieData

import kotlinx.coroutines.delay
import kotlin.random.Random

class MoviesModel(
    private val moviesDataSource: MoviesDataSource
) {

    fun getMovies() = moviesDataSource.getMovies()

    suspend fun getRandomMovies(): List<MovieData> {
        delay(2000)
        val start = Random.nextInt(0, 3)
        return getMovies().subList(start, getMovies().size - 3 + start)
    }


}