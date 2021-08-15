package com.mikhailgrigorev.mts_home.MovieResponse

import android.content.ContentValues
import android.os.AsyncTask
import com.mikhailgrigorev.mts_home.App
import com.mikhailgrigorev.mts_home.api.MovieResponse
import com.mikhailgrigorev.mts_home.api.ObjectResponse
import com.mikhailgrigorev.mts_home.movieData.MoviesDataSource
import kotlinx.serialization.SerialName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BaseMoviesModel(
    private val moviesDataSource: MoviesDataSource
) {

    private fun getMovies() = moviesDataSource.getMovies()

    // suspend fun getRandomMovies(): List<MovieResponse> {
    //     delay(2000)
    //     val start = Random.nextInt(0, 3)
    //     return getMovies().subList(start, getMovies().size - 3 + start)
    // }


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
        fun onLoad(movies: List<MovieResponse>?)
    }

    interface LoadMovieByIdCallback {
        fun onLoad(movie: MovieResponse)
    }

    interface CompleteCallback {
        fun onComplete()
    }
}

class LoadMovieTask(
    private val callback: MoviesModel.LoadMovieByIdCallback?,
    private val id: Int
) :
    AsyncTask<Void?, Void?, MovieResponse>() {
    override fun doInBackground(vararg params: Void?): MovieResponse {
        // var resultMovie: MovieResponse? = null
        // val allMovies = MoviesDataSourceImpl().getMovies()
        // for (movie in allMovies){
        //     if (movie.id == id)
        //         resultMovie = movie
        // }
        // return resultMovie!!
        return MovieResponse(0, "2",  "r", "f", 4, 17, arrayListOf(4, 5),
            false,
            "9",
            "9",
           false,
            6F,
            6,
            "58",)
    }

    override fun onPostExecute(movie: MovieResponse) {
        callback?.onLoad(movie)
    }
}

class LoadMoviesTask(
    private val callback: MoviesModel.LoadMovieCallback?
) :
    AsyncTask<Void?, Void?, List<MovieResponse>>() {
    override fun doInBackground(vararg params: Void?): List<MovieResponse> {
        var result: List<MovieResponse> = arrayListOf()
        App.instance.apiService.getMovies().enqueue(object : Callback<ObjectResponse> {
            override fun onResponse(
                call: Call<ObjectResponse>,
                response: Response<ObjectResponse>
            ) {
                result = response.body()?.results ?: emptyList()
            }

            override fun onFailure(call: Call<ObjectResponse>, t: Throwable) {
            }
        })
        return result
    }


    // return MoviesDataSourceImpl().getMovies()


    override fun onPostExecute(result: List<MovieResponse>?) {
        callback?.onLoad(result)
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