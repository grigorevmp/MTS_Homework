package com.mikhailgrigorev.mts_home.genreData

import android.content.ContentValues
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikhailgrigorev.mts_home.App
import com.mikhailgrigorev.mts_home.api.GenresResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response


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
        addGenreTask.execute()
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
) : ViewModel() {
    fun execute() = viewModelScope.launch {
        val result = doInBackground()
        onPostExecute(result)
    }

    private suspend fun doInBackground(): Genre = withContext(Dispatchers.IO) {
        val genreRepo = GenreRepository()
        return@withContext genreRepo.getGenreById(id)
    }

    private fun onPostExecute(genre: Genre) {
        callback?.onLoad(genre)
    }
}


class LoadGenresTask(
    private val callback: GenreModel.LoadGenreCallback?,
) : ViewModel() {
    fun execute() = viewModelScope.launch {
        val result = doInBackground()
        onPostExecute(result)
    }

    private suspend fun doInBackground(): List<Genre> = withContext(Dispatchers.IO) {
        val genreRepo = GenreRepository()
        App.instance.apiService.getGenres()
            .enqueue(object : retrofit2.Callback<GenresResponse> {
                override fun onResponse(
                    call: Call<GenresResponse>,
                    response: Response<GenresResponse>
                ) {

                    val genres: MutableList<Genre> = arrayListOf()


                    val genresResponse = response.body()!!.genres
                    for (genre in genresResponse) {
                        genres.add(Genre(genre.id, genre.genre))
                    }


                    genreRepo.insertAll(genres)

                }

                override fun onFailure(call: Call<GenresResponse>, t: Throwable) {
                    t.printStackTrace()
                }

            })
        return@withContext genreRepo.getAllGenres()
    }

    private fun onPostExecute(genres: List<Genre>) {
        callback?.onLoad(genres)
    }
}


class AddGenreTask(
    private val callback: GenreModel.CompleteCallback?
) : ViewModel() {
    fun execute() = viewModelScope.launch {
        doInBackground()
        onPostExecute()
    }

    private suspend fun doInBackground() = withContext(Dispatchers.IO) {
        return@withContext null
    }

    private fun onPostExecute() {
        callback?.onComplete()
    }
}

class ClearGenresTask(
    private val callback: GenreModel.CompleteCallback?
) : ViewModel() {
    fun execute() = viewModelScope.launch {
        doInBackground()
        onPostExecute()
    }

    private suspend fun doInBackground() = withContext(Dispatchers.IO) {
        return@withContext null
    }

    private fun onPostExecute() {
        callback?.onComplete()
    }
}

interface GenreModelApi {
    fun loadGenres(callback: GenreModel.LoadGenreCallback?)
    fun loadGenre(callback: GenreModel.LoadGenreByIdCallback?, id: Int)
    fun addMovie(contentValues: ContentValues?, callback: GenreModel.CompleteCallback?)
    fun clearMovies(completeCallback: GenreModel.CompleteCallback?)
}