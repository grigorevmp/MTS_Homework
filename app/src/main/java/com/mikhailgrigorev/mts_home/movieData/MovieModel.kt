package com.mikhailgrigorev.mts_home.movieData

import android.content.ContentValues
import android.os.AsyncTask
import kotlinx.coroutines.delay
import kotlin.random.Random

class BaseMoviesModel(
    private val moviesDataSource: MoviesDataSource
) {

    private fun getMovies() = moviesDataSource.getMovies()

    suspend fun getRandomMovies(): List<MovieData> {
        delay(2000)
        val start = Random.nextInt(0, 3)
        return getMovies().subList(start, getMovies().size - 3 + start)
    }


}

class MoviesModel : MoviesModelApi {

    override fun loadMovie(callback: LoadMovieByIdCallback?, id: Int) {
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
        fun onLoad(movies: List<MovieData>?)
    }

    interface LoadMovieByIdCallback {
        fun onLoad(movie: MovieData?)
    }

    interface CompleteCallback {
        fun onComplete()
    }
}

class LoadMovieTask(
    private val callback: MoviesModel.LoadMovieByIdCallback?,
    private val id: Int
) :
    AsyncTask<Void?, Void?, MovieData>() {
    override fun doInBackground(vararg params: Void?): MovieData {
        var resultMovie: MovieData? = null
        val allMovies = MoviesDataSourceImpl().getMovies()
        for (movie in allMovies){
            if (movie.id == id)
                resultMovie = movie
        }
        return resultMovie!!
    }

    override fun onPostExecute(movie: MovieData) {
        callback?.onLoad(movie)
    }
}

class LoadMoviesTask(
    private val callback: MoviesModel.LoadMovieCallback?
) :
    AsyncTask<Void?, Void?, List<MovieData>>() {
    override fun doInBackground(vararg params: Void?): List<MovieData> {
        return MoviesDataSourceImpl().getMovies()
    }

    override fun onPostExecute(movies: List<MovieData>) {
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
    fun loadMovie(callback: MoviesModel.LoadMovieByIdCallback?, id: Int)
    fun addMovie(contentValues: ContentValues?, callback: MoviesModel.CompleteCallback?)
    fun clearMovies(completeCallback: MoviesModel.CompleteCallback?)

}