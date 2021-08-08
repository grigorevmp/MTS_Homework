package com.mikhailgrigorev.mts_home.movieData

import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import kotlinx.coroutines.delay
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
        addMovieTask.execute(contentValues)
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
) :
    AsyncTask<Void?, Void?, Movie>() {
    override fun doInBackground(vararg params: Void?): Movie {
        val movieRepo = MovieRepository(Application())
        return movieRepo.getMovieById(id)
    }

    override fun onPostExecute(movie: Movie) {
        callback?.onLoad(movie)
    }
}

class LoadMoviesTask(
    private val callback: MoviesModel.LoadMovieCallback?,
) :
    AsyncTask<Void?, Void?, List<Movie>>() {
    override fun doInBackground(vararg params: Void?): List<Movie> {
        val movieRepo = MovieRepository(Application())

        return movieRepo.getAllMovies()
    }


    override fun onPostExecute(movies: List<Movie>) {
        callback?.onLoad(movies)
    }
}

class AddMovieTask(
    private val callback: MoviesModel.CompleteCallback?
) :
    AsyncTask<ContentValues?, Void?, Void?>() {
    override fun doInBackground(vararg params: ContentValues?): Void? {
        return null
    }

    override fun onPostExecute(aVoid: Void?) {
        super.onPostExecute(aVoid)
        callback?.onComplete()
    }
}

class ClearMoviesTask(
    private val callback: MoviesModel.CompleteCallback?
) :
    AsyncTask<Void?, Void?, Void?>() {
    override fun doInBackground(vararg params: Void?): Void? {
        return null
    }

    override fun onPostExecute(aVoid: Void?) {
        super.onPostExecute(aVoid)
        callback?.onComplete()
    }
}

interface MoviesModelApi {
    fun loadMovies(callback: MoviesModel.LoadMovieCallback?)
    fun loadMovie(callback: MoviesModel.LoadMovieByIdCallback?, id: Long)
    fun addMovie(contentValues: ContentValues?, callback: MoviesModel.CompleteCallback?)
    fun clearMovies(completeCallback: MoviesModel.CompleteCallback?)
}