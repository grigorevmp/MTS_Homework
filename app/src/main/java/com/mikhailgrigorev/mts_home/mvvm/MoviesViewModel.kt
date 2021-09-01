package com.mikhailgrigorev.mts_home.mvvm

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mikhailgrigorev.mts_home.App
import com.mikhailgrigorev.mts_home.MoviesFragment
import com.mikhailgrigorev.mts_home.api.ObjectResponse
import com.mikhailgrigorev.mts_home.movieData.Movie
import com.mikhailgrigorev.mts_home.movieData.MovieRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

typealias MoviesFragmentViewState = MoviesFragment.ViewState

class MoviesViewModel : ViewModel() {


    val viewState: LiveData<MoviesFragmentViewState> get() = _viewState
    private val _viewState = MutableLiveData<MoviesFragmentViewState>()

    val dataList: LiveData<List<Movie>> get() = _dataList
    private val _dataList = MutableLiveData<List<Movie>>()

    private val movieRepository = MovieRepository()

    fun loadMovies() {
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
                movieRepository.insertAll(moviesForDb)
                _dataList.postValue(moviesForDb)
                _viewState.postValue(MoviesFragmentViewState(isDownloading = false))
            }

            override fun onFailure(call: Call<ObjectResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })

    }

    fun add(userData: Movie) {
    }

    fun clear(context: Context) {
        _viewState.postValue(MoviesFragmentViewState(isDownloading = true))
    }
}