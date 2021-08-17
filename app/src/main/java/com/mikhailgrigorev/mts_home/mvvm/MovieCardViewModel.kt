package com.mikhailgrigorev.mts_home.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mikhailgrigorev.mts_home.App
import com.mikhailgrigorev.mts_home.MoviesDetailFragment
import com.mikhailgrigorev.mts_home.api.MovieOneResponse
import com.mikhailgrigorev.mts_home.api.MovieWithActorsResponse
import com.mikhailgrigorev.mts_home.movieData.MoviesModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

typealias MoviesDetailsFragmentViewState = MoviesDetailFragment.ViewState

class MovieCardViewModel : ViewModel(){

    private val model = MoviesModel()

    val viewState: LiveData<MoviesDetailsFragmentViewState> get() = _viewState
    private val _viewState = MutableLiveData<MoviesDetailsFragmentViewState>()

    val currentMovie: LiveData<MovieWithActorsResponse> get() = _currentMovie
    private val _currentMovie = MutableLiveData<MovieWithActorsResponse>()


    fun loadMovie(id: Int) {
        App.instance.apiService.getMovieByIdWithActors(id.toString())
            .enqueue(object : Callback<MovieWithActorsResponse> {
                override fun onResponse(
                    call: Call<MovieWithActorsResponse>,
                    response: Response<MovieWithActorsResponse>
                ) {
                    val movie = response.body()!!
                    _currentMovie.postValue(movie)
                    _viewState.postValue(MoviesDetailsFragmentViewState(isDownloaded = false))
                }

                override fun onFailure(call: Call<MovieWithActorsResponse>, t: Throwable) {
                    t.printStackTrace()
                    _viewState.postValue(MoviesDetailsFragmentViewState(isDownloaded = true))
                }

            })
    }

    fun add(userData: MovieWithActorsResponse) {
    }

    fun clear() {
    }
}