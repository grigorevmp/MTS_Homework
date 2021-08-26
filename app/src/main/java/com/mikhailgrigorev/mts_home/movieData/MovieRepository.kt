package com.mikhailgrigorev.mts_home.movieData

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieRepository {

    private var movieDao: MovieDao

    private val database = MovieDatabase.getInstance()

    init {
        movieDao = database.movieDao()!!
    }

    fun insert(movie: Movie) {
        CoroutineScope(Dispatchers.IO).launch {
            movieDao.insert(movie)
        }
    }

    fun update(movie: Movie) {
        CoroutineScope(Dispatchers.IO).launch {
            movieDao.update(movie)
        }
    }

    fun delete(movie: Movie) {
        CoroutineScope(Dispatchers.IO).launch {
            movieDao.delete(movie)
        }
    }

    fun deleteAllMovies() {
        CoroutineScope(Dispatchers.IO).launch {
            movieDao.deleteAll()
        }
    }

    suspend fun getAllMovies(): List<Movie> {
        return movieDao.getAll()
    }

    suspend fun getMovieById(id: Long): Movie {
        return movieDao.getById(id)!!
    }



}