package com.mikhailgrigorev.mts_home.movieData

import android.content.ContentValues
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

class BaseMoviesModel(
    private val allMovies: List<Movie>
) {

    private fun getMovies() = allMovies

    suspend fun getRandomMovies(): List<Movie> {
        delay(2000)
        val start = Random.nextInt(0, 3)
        return getMovies().subList(start, getMovies().size - 3 + start)
    }


}

class MoviesModel : MoviesModelApi {

    override fun loadMovie(callback: LoadMovieByIdCallback?, id: Long) {
        val loadMovieTask = LoadMovieTask(callback, id)
        loadMovieTask.execute()
    }

    override fun loadMovies(callback: LoadMovieCallback?) {
        val loadMoviesTask = LoadMoviesTask(callback)
        loadMoviesTask.execute()
    }

    override fun addMovie(contentValues: ContentValues?, callback: CompleteCallback?) {
        val addMovieTask = AddMovieTask(callback)
        addMovieTask.execute()
    }

    override fun clearMovies(completeCallback: CompleteCallback?) {
        val clearMoviesTask = ClearMoviesTask(completeCallback)
        clearMoviesTask.execute()
    }

    interface LoadMovieCallback {
        fun onLoad(movies: List<Movie>?)
    }

    interface LoadMovieByIdCallback {
        fun onLoad(movie: Movie?)
    }

    interface CompleteCallback {
        fun onComplete()
    }
}

class LoadMovieTask(
    private val callback: MoviesModel.LoadMovieByIdCallback?,
    private val id: Long
) : ViewModel() {
    fun execute() = viewModelScope.launch {
        val result = doInBackground()
        onPostExecute(result)
    }

    private suspend fun doInBackground(): Movie = withContext(Dispatchers.IO) {
        val movieRepo = MovieRepository()
        return@withContext movieRepo.getMovieById(id)
    }

    private fun onPostExecute(movie: Movie) {
        callback?.onLoad(movie)
    }
}


class LoadMoviesTask(
    private val callback: MoviesModel.LoadMovieCallback?,
) : ViewModel() {
    fun execute() = viewModelScope.launch {
        val result = doInBackground()
        onPostExecute(result)
    }

    private suspend fun doInBackground(): List<Movie> = withContext(Dispatchers.IO) {
        val movieRepo = MovieRepository()
        return@withContext movieRepo.getAllMovies()
    }

    private fun onPostExecute(movies: List<Movie>) {
        callback?.onLoad(movies)
    }
}


class AddMovieTask(
    private val callback: MoviesModel.CompleteCallback?
) : ViewModel() {
    fun execute() = viewModelScope.launch {
        doInBackground()
        onPostExecute()
    }

    private suspend fun doInBackground(): List<Movie>? = withContext(Dispatchers.IO) {
        return@withContext null
    }

    private fun onPostExecute() {
        callback?.onComplete()
    }
}

class ClearMoviesTask(
    private val callback: MoviesModel.CompleteCallback?
) : ViewModel() {
    fun execute() = viewModelScope.launch {
        doInBackground()
        onPostExecute()
    }

    private suspend fun doInBackground(): List<Movie>? = withContext(Dispatchers.IO) {
        return@withContext null
    }

    private fun onPostExecute() {
        callback?.onComplete()
    }
}

interface MoviesModelApi {
    fun loadMovies(callback: MoviesModel.LoadMovieCallback?)
    fun loadMovie(callback: MoviesModel.LoadMovieByIdCallback?, id: Long)
    fun addMovie(contentValues: ContentValues?, callback: MoviesModel.CompleteCallback?)
    fun clearMovies(completeCallback: MoviesModel.CompleteCallback?)
}