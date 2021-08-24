package com.mikhailgrigorev.mts_home.movieData

import android.content.ContentValues
import android.os.AsyncTask
import com.mikhailgrigorev.mts_home.App
import com.mikhailgrigorev.mts_home.api.*
import kotlinx.coroutines.delay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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
        fun onLoad(movies: List<Movie>?)
    }

    interface LoadMovieByIdCallback {
        fun onLoad(movie: Movie)
    }

    interface CompleteCallback {
        fun onComplete()
    }
}

class LoadMovieTask(
    private val callback: MoviesModel.LoadMovieByIdCallback?,
    private val id: Int
) :
    AsyncTask<Void?, Void?, Movie>() {
    override fun doInBackground(vararg params: Void?): Movie {
        val movieRepo = MovieRepository()
        return movieRepo.getMovieById(id)!!
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
        val movieRepo = MovieRepository()

        App.instance.apiService.getMovies().enqueue(object : Callback<ObjectResponse> {
            override fun onResponse(
                call: Call<ObjectResponse>,
                response: Response<ObjectResponse>
            ) {
                val moviesForDb: MutableList<Movie> = arrayListOf()
                val movies = response.body()?.results ?: emptyList()
                for (movie in movies) {
                    moviesForDb.add(
                        Movie(
                            id = movie.id,
                            title = movie.title,
                            poster_path = movie.poster_path,
                            overview = movie.overview,
                            vote_average = movie.vote_average,
                            genre_ids = movie.genre_ids.joinToString(' '.toString()),
                            adult = movie.adult,
                            backdrop_path = movie.backdrop_path,
                            original_language = movie.original_language,
                            video = movie.video,
                            popularity = movie.popularity,
                            vote_count = movie.vote_count,
                            release_date = movie.release_date
                        )
                    )
                }
                movieRepo.insertAll(moviesForDb)
            }

            override fun onFailure(call: Call<ObjectResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })

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
    fun loadMovie(callback: MoviesModel.LoadMovieByIdCallback?, id: Int)
    fun addMovie(contentValues: ContentValues?, callback: MoviesModel.CompleteCallback?)
    fun clearMovies(completeCallback: MoviesModel.CompleteCallback?)
}