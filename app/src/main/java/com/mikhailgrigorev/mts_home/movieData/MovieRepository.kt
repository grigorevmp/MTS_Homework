package com.mikhailgrigorev.mts_home.movieData

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieRepository(context: Context) {

    private var movieDao: MovieDao
    private var allMovies: List<Movie>

    private val database = MovieDatabase.getInstance(context)

    init {
        movieDao = database.movieDao()!!
        allMovies = movieDao.getAll()
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

    fun getAllMovies(): List<Movie> {
        return allMovies
    }

    fun getMovieById(id: Long): Movie {
        return movieDao.getById(id)!!
    }



}