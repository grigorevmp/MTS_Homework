package com.mikhailgrigorev.mts_home.genreData

import android.app.Application
import android.content.ContentValues
import android.os.AsyncTask


class GenreModelPreview(
    private val genresDataSource:GenreDataSourceImpl
) {

    fun getGenres() = genresDataSource.getGenres()

    fun getGenre(id: Int): String? {
        for (genre in genresDataSource.getGenres()){
            if (genre.id == id)
                return genre.genre
        }
        return null
    }
}

class GenreModel : GenreModelApi {

    override fun loadGenre(callback: LoadGenreByIdCallback?, id: Int) {
        val loadGenreTask = LoadGenreTask(callback, id)
        loadGenreTask.execute()
    }

    override fun loadGenres(callback: LoadGenreCallback?) {
        val loadMoviesTask = LoadGenresTask(callback)
        loadMoviesTask.execute()
    }

    override fun addMovie(contentValues: ContentValues?, callback: CompleteCallback?) {
        val addGenreTask = AddGenreTask(callback)
        addGenreTask.execute(contentValues)
    }

    override fun clearMovies(completeCallback: CompleteCallback?) {
        val clearGenresTask = ClearGenresTask(completeCallback)
        clearGenresTask.execute()
    }

    interface LoadGenreCallback {
        fun onLoad(genres: List<Genre>?)
    }

    interface LoadGenreByIdCallback {
        fun onLoad(genre: Genre?)
    }

    interface CompleteCallback {
        fun onComplete()
    }
}

class LoadGenreTask(
    private val callback: GenreModel.LoadGenreByIdCallback?,
    private val id: Int
) :
    AsyncTask<Void?, Void?, Genre>() {
    override fun doInBackground(vararg params: Void?): Genre {
        val genreRepo = GenreRepository(Application())
        return genreRepo.getGenreById(id)
    }

    override fun onPostExecute(genre: Genre) {
        callback?.onLoad(genre)
    }
}

class LoadGenresTask(
    private val callback: GenreModel.LoadGenreCallback?,
) :
    AsyncTask<Void?, Void?, List<Genre>>() {
    override fun doInBackground(vararg params: Void?): List<Genre> {
        val genreRepo = GenreRepository(Application())
        return genreRepo.getAllGenres()
    }


    override fun onPostExecute(genres: List<Genre>) {
        callback?.onLoad(genres)
    }
}

class AddGenreTask(
    private val callback: GenreModel.CompleteCallback?
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

class ClearGenresTask(
    private val callback: GenreModel.CompleteCallback?
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

interface GenreModelApi {
    fun loadGenres(callback: GenreModel.LoadGenreCallback?)
    fun loadGenre(callback: GenreModel.LoadGenreByIdCallback?, id: Int)
    fun addMovie(contentValues: ContentValues?, callback: GenreModel.CompleteCallback?)
    fun clearMovies(completeCallback: GenreModel.CompleteCallback?)
}