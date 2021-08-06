package com.mikhailgrigorev.mts_home.movieData

import android.content.ContentValues
import android.os.AsyncTask

class BaseMoviesModel(
    private val moviesDataSource: MoviesDataSource
) {

    fun getMovies() = moviesDataSource.getMovies()
}

class MoviesModel : MoviesModelApi {


    override fun loadMovies(callback: LoadUserCallback?) {
        val loadUsersTask = LoadUsersTask(callback)
        loadUsersTask.execute()
    }

    override fun addMovie(contentValues: ContentValues?, callback: CompleteCallback?) {
        val addUserTask = AddUserTask(callback)
        addUserTask.execute(contentValues)
    }

    override fun clearMovies(completeCallback: CompleteCallback?) {
        val clearUsersTask = ClearUsersTask(completeCallback)
        clearUsersTask.execute()
    }

    interface LoadUserCallback {
        fun onLoad(movies: List<MovieData>?)
    }

    interface CompleteCallback {
        fun onComplete()
    }
}

class LoadUsersTask(
    private val callback: MoviesModel.LoadUserCallback?
) :
    AsyncTask<Void?, Void?, List<MovieData>>() {
    override fun doInBackground(vararg params: Void?): List<MovieData> {
        return MoviesDataSourceImpl().getMovies()
    }

    override fun onPostExecute(movies: List<MovieData>) {
        callback?.onLoad(movies)
    }
}

class AddUserTask(
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

class ClearUsersTask(
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
    fun loadMovies(callback: MoviesModel.LoadUserCallback?)
    fun addMovie(contentValues: ContentValues?, callback: MoviesModel.CompleteCallback?)
    fun clearMovies(completeCallback: MoviesModel.CompleteCallback?)
}