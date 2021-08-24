package com.mikhailgrigorev.mts_home.movieData

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieRepository {

    private var movieDao: MovieDao
    private var allMovies: List<Movie>

    private val database = MovieDatabase.getInstance()

    init {
        movieDao = database.movieDao()!!
        allMovies = movieDao.getAll()
    }

    fun insert(movie: Movie) {
        CoroutineScope(Dispatchers.IO).launch {
            movieDao.insert(movie)
        }
    }

    fun insertAll(movies: List<Movie>) {
        CoroutineScope(Dispatchers.IO).launch {
            movieDao.insertAll(movies)
        }
    }

    fun update(movie: Movie) {
        CoroutineScope(Dispatchers.IO).launch {
            movieDao.update(movie)
        }
    }

    fun updateSpecial(actors_names: String, actors_paths: String, genre_ids: String, age_restriction: Int, id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            movieDao.updateSpecial(actors_names, actors_paths, genre_ids, age_restriction, id)
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

    fun getMovieById(id: Int): Movie? {
        return movieDao.getById(id)
    }



}