package com.mikhailgrigorev.mts_home.movieData

import com.mikhailgrigorev.mts_home.App
import com.mikhailgrigorev.mts_home.api.MovieWithActorsResponse
import com.mikhailgrigorev.mts_home.api.ObjectResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

    suspend fun loadAndInsertAll(): List<Movie> {
        val job = CoroutineScope(Dispatchers.IO).launch {
            App.instance.apiService.getMovies().enqueue(
                object : Callback<ObjectResponse> {
                    override fun onResponse(
                        call: Call<ObjectResponse>,
                        response: Response<ObjectResponse>
                    ) {
                        val moviesForDb: MutableList<Movie> = arrayListOf()
                        val movies = response.body()?.results ?: emptyList()
                        movies.map { movie ->
                            moviesForDb.add(
                                Movie.movieFromResponse(movie)
                            )
                        }

                        insertAll(moviesForDb)
                    }

                    override fun onFailure(call: Call<ObjectResponse>, t: Throwable) {
                        t.printStackTrace()
                    }
                })
        }
        job.join()
        return getAllMovies()
    }

    private var tempMovie: MovieWithActorsResponse = MovieWithActorsResponse()

    fun insertTempMovie(movie: MovieWithActorsResponse) {
        tempMovie = movie.clone()
    }

    fun getMovieTemp(): MovieWithActorsResponse {
        return tempMovie
    }

    suspend fun loadAndReturn(id: Long) {
        val job = CoroutineScope(Dispatchers.IO).launch {
            App.instance.apiService.getMovieByIdWithActors(id.toString()).enqueue(
                object : Callback<MovieWithActorsResponse> {
                    override fun onResponse(
                        call: Call<MovieWithActorsResponse>,
                        response: Response<MovieWithActorsResponse>
                    ) {
                        val movie = response.body()
                        insertTempMovie(movie!!)

                    }

                    override fun onFailure(call: Call<MovieWithActorsResponse>, t: Throwable) {
                        t.printStackTrace()
                    }

                })
        }
        job.join()
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

    private fun getAllMovies(): List<Movie> {
        return movieDao.getAll()
    }

    fun getMovieById(id: Long): Movie {
        return movieDao.getById(id)!!
    }

}